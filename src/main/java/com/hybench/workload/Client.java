package com.hybench.workload;
/**
 *
 * @time 2023-03-04
 * @version 1.0.0
 * @file Client.java
 * @description
 *   abstract class Client
 **/


import com.hybench.Constant;
import com.hybench.load.ConfigReader;
import com.hybench.stats.Histogram;
import com.hybench.stats.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.PriorityQueue;
import java.util.Set;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.io.*;
import com.hybench.ConfigLoader;

public abstract class Client {
    public static Logger logger = LogManager.getLogger(Client.class);

    protected boolean exitFlag = false;
    protected Properties prop = null;
    CompletionService<ClientResult> cs = null;
    Future<ClientResult>[] fs = null;
    int dbType = 0;
    Sqlstmts sqls = null;
    private int testDuration = 0;
    private String clientName = "";
    int threads = 0;
    static long tpTotalCount = 0L;
    static long apTotalCount = 0L;
    static long apTotalTime = 0L;
    Lock lock = new ReentrantLock();
    protected int taskType = 0; // 0 : xp ,1: tp,2 : ap , 3:xp ,4: htap
    ConfigReader CR = null;
    Result ret = null;
    Histogram hist = null;
    boolean verbose = true;
    int round = 1;

    Set<Integer> Blocked_ids = null;
    Set<Integer> Related_Blocked_Transfer_ids=null;
    Set<Integer> Related_Blocked_Checking_ids=null;
    Set<Integer> Related_Blocked_LoanApp_ids=null;
    double risk_rate=0;
    PriorityQueue<Integer> queue_ids= null;

    ExecutorService es = Executors.newFixedThreadPool(5);

    public void setVerbose(boolean verbose){
        this.verbose = verbose;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setSqls(Sqlstmts sqls) {
        this.sqls = sqls;
    }

    public void setRet(Result ret) {
        this.ret = ret;
    }

    public Result getRet() {
        return ret;
    }

    public void setDbType(int dbType){
        this.dbType = dbType;
    }

    public void setTestTime(int time) {
        testDuration = time;
    }
    public int getTestTime() {
        return testDuration;
    }

    public void setTaskType(int type){
        this.taskType = type;
    }
    public int getTaskType(){
        return taskType;
    }

    public void setClientName(String name){
        this.clientName = name;
    }

    public String getClientName(){
        return this.clientName;
    }

    protected void setTask_prop(Properties prop) {
        this.prop = prop;
    }

    // parameter handler including string value , int value and bool value
    protected String strParameter(String paraName) {
        return prop.containsKey(paraName) ? prop
                .getProperty(paraName) : null;
    }

    protected String strParameter(String paraName, String defaultValue) {
        return prop.containsKey(paraName) ? prop
                .getProperty(paraName) : defaultValue;
    }

    protected int intParameter(String paraName, int defaultValue) {
        String v = prop
                .getProperty(paraName, String.valueOf(defaultValue));
        return Integer.parseInt(v);
    }

    protected int intParameter(String paraName) {

        return intParameter(paraName, 0);
    }

    protected boolean boolParameter(String paraName, boolean defaultValue) {
        if (prop.containsKey(paraName)) {
            if ("true".equalsIgnoreCase(prop.getProperty(paraName))) {
                return true;
            }
            return false;
        }
        return defaultValue;
    }

    protected boolean boolParameter(String paraName) {
        return boolParameter(paraName, false);
    }

    // get db type, only mysql/oracle/pg supported
    public int getDbType(String db){

        if(db.equalsIgnoreCase("postgreSQL")){
            return Constant.DB_PG;
        }
        else if(db.equalsIgnoreCase("mysql")){
            return Constant.DB_MYSQL;
        }
        else if(db.equalsIgnoreCase("oracle")){
            return Constant.DB_ORACLE;
        }
        else{
            return Constant.DB_UNKNOW;
        }
    }
    // get data size and create thread pool according to client number
    public void doInit_wrapper(String clientName) {
        threads = intParameter("tpclient");
        CR = new ConfigReader(strParameter("sf","1x"));
        String db = strParameter("db");
        setDbType(getDbType(db));
        if(clientName.equals("APClient") ){
            if(taskType == 0 || taskType == 4){
                threads = intParameter("apclient");
            }
            else if(taskType == 2) {
                threads = 1;
            }
            else{
                threads = 0;
            }
        }

        if (threads > 0) {
            es = Executors.newFixedThreadPool(threads);
            cs = new ExecutorCompletionService<ClientResult>(es);
        }

        risk_rate = Double.valueOf(ConfigLoader.prop.getProperty("risk_rate","0.1"));
        queue_ids=new PriorityQueue<Integer>();
        try {
            // load the blocking-related transfer accounts
            String DataPath = "Data_" + ConfigLoader.prop.getProperty("sf") + "/";
            FileInputStream fi1 = new FileInputStream(new File(DataPath+"Related_transfer_bids.txt"));
            ObjectInputStream oi1 = new ObjectInputStream(fi1);
            // Read objects
            Related_Blocked_Transfer_ids = (Set<Integer>) oi1.readObject();
            oi1.close();
            fi1.close();

            // load the blocking-related checking accounts
            FileInputStream fi2 = new FileInputStream(new File(DataPath+"Related_checking_bids.txt"));
            ObjectInputStream oi2 = new ObjectInputStream(fi2);
            // Read objects
            Related_Blocked_Checking_ids = (Set<Integer>) oi2.readObject();
            oi2.close();
            fi2.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error initializing stream");
        }

        doInit();
    }

    public static Client initTask(Properties cfg,String name,int taskType) {
        Client client = null;
        try {
            client = (Client) Class.forName("com.hybench.workload." + name).getDeclaredConstructor().newInstance();
            client.setClientName(name);
            client.setTask_prop(cfg);
            client.setTaskType(taskType);
            client.doInit_wrapper(name);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return client;
    }
    // client work from here. A new thread named timer to output response time histogram every 1/10 duration
    public void startTask() {

        ClientResult _res = null;
        Thread timer = null;
        String db = strParameter("db");
        setDbType(getDbType(db));
        if(ret != null){
            hist = ret.getHist();
        }

        if(clientName.equalsIgnoreCase("APClient"))
            ret.setApclient(threads);
        if(clientName.equalsIgnoreCase("tpclient"))
            ret.setTpclient(threads);
	ret.setRiskRate(String.valueOf(risk_rate));

        final int _duration = intParameter("runMins");
        final int _fresh_interval = intParameter("fresh_interval",20);

        round = intParameter("apround",1);

        if(taskType == 2) {
            logger.info("Begin to run :" + clientName + ", Test is " + round + " round");
        }
        else{
            logger.info("Begin to run :" + clientName + ", Test Duration is "  + _duration + " mins");
        }

        setTestTime(intParameter("runMins"));
        if (_duration > 0) {
            timer = new Thread() {
                public void run() {
                    try {
                        long duration = _duration * 60 * 1000L;
                        long elpased_time = 0L;
                        for (int i = 0; i < 10; i++) {
                            Thread.sleep(_duration * 60 * 100L);
                            elpased_time += _duration * 60 * 100L;
                            if(verbose){
                                if(clientName.equalsIgnoreCase("APClient")) {
                                    for(int apidx = 0;apidx < 12;apidx++) {
                                        if(hist.getAPItem(apidx).getN() == 0)
                                            continue;
                                        logger.info("Query " + (apidx+1)
                                                + " max rt : " + hist.getAPItem(apidx).getMax()
                                                + " min rt :" + hist.getAPItem(apidx).getMin()
                                                + " avg rt : " + String.format("%.2f",hist.getAPItem(apidx).getMean())
                                                + " 95% rt : " + String.format("%.2f",hist.getAPItem(apidx).getPercentile(95))
                                                + " 99% rt : " + String.format("%.2f",hist.getAPItem(apidx).getPercentile(99)));
                                    }
                                    logger.info("Current " + (i + 1) + "/10 time AP QPS is " + String.format("%.2f", apTotalCount / (elpased_time / 1000.0)));
                                }
                                if(clientName.equalsIgnoreCase("TPClient")) {
                                    for(int tpidx = 0;tpidx < 16;tpidx++) {
                                        if(hist.getTPItem(tpidx).getN() == 0)
                                            continue;
                                        logger.info("Transaction " + (tpidx+1)
                                                + " max rt : " + hist.getTPItem(tpidx).getMax()
                                                + " min rt :" + hist.getTPItem(tpidx).getMin()
                                                + " avg rt : " + String.format("%.2f",hist.getTPItem(tpidx).getMean())
                                                + " 95% rt : " + String.format("%.2f",hist.getTPItem(tpidx).getPercentile(95))
                                                + " 99% rt : " + String.format("%.2f",hist.getTPItem(tpidx).getPercentile(99)));
                                    }
                                    logger.info("Current " + (i + 1) + "/10 time TP TPS is " + String.format("%.2f", tpTotalCount / (elpased_time / 1000.0)));
                                }
                            }

                        }
                        
                        stopTask();

                    } catch (InterruptedException e) {
                        logger.warn("Test Duration Timer was stopped in force");
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            timer.start();

        }
        int _num_thread = threads;

        fs = new Future[_num_thread];
        for (int i = 1; i <= _num_thread; i++) {
            final String threadId = "T" + i;

            Callable<ClientResult> r = new Callable<ClientResult>() {

                public ClientResult call() throws Exception {
                    // TODO Auto-generated method stub
                 
                    ClientResult result= execute();
                    return result;
                }
            };
            try {
                fs[i-1] = cs.submit(r);
            }  catch(Exception e) {
                logger.error("create thread failed " ,e );
            }

        }

        for (int i = 0; i < _num_thread; i++) {
            try {
                fs[i] = cs.take();
                if(fs[i] != null && !fs[i].isCancelled() && fs[i].isDone() ) {
                    _res = fs[i].get();
                }
            } catch (Exception e) {
                logger.error("Waiting for load worker", e);
            }
        }

        if (timer != null) {
            timer.interrupt();
        }

        if (!es.isShutdown() || !es.isTerminated()) {
            es.shutdownNow();
        }

        if(clientName.equalsIgnoreCase("APClient")){
            ret.setApTotal(apTotalCount);
            if(taskType == 2){
                ret.setQps(Double.valueOf(String.format("%.2f",apTotalCount/(apTotalTime/1000.0))));
            }
            else if(taskType == 0 || taskType == 4){
                ret.setXpqps(Double.valueOf(String.format("%.2f",apTotalCount/(testDuration * 60.0))));
            }
        }

        if(clientName.equalsIgnoreCase("TPClient")) {
            ret.setTpTotal(tpTotalCount);
            if (taskType == 1) {
                ret.setTps(Double.valueOf(String.format("%.2f", tpTotalCount / (testDuration * 60.0))));
            }
            else if(taskType == 0 || taskType == 4){
                ret.setXptps(Double.valueOf(String.format("%.2f",tpTotalCount/(testDuration * 60.0))));
            }
            else if(taskType == 5){
                ret.setIqps(Double.valueOf(String.format("%.2f",apTotalCount/(testDuration * 60.0))));
            }
            else if(taskType == 6){
                ret.setAtps(Double.valueOf(String.format("%.2f",tpTotalCount/(testDuration * 60.0))));
            }
        }

        logger.info( "Finished to execute " + clientName );
    }
    public abstract void doInit();

    public abstract ClientResult execute();

    public void stopTask() {
        exitFlag =  true;
        int p = 0;
        if(fs != null){
            for(Future ft:fs){
                ft.cancel(true);
            }
        }
    }
}
