package com.hybench.stats;
/**
 *
 * @time 2023-03-04
 * @version 1.0.0
 * @file Result.java
 * @description
 *   record test result and print summary after all workloads are done.
 **/

public class Result {
    private String dbType = null;
    private long tpTotal;
    private long apTotal;
    private double tps;
    private double qps;
    private double xptps;
    private double xpqps;
    private double atps;
    private double iqps;
    private String startTS ;
    private String endTs;
    private Histogram hist;
    private double freshness = 0;
    private int apclient ;
    private int tpclient;

    public Result(){
        hist = new Histogram();
    }

    public void setHist(Histogram hist) {
        this.hist = hist;
    }

    public Histogram getHist() {
        return hist;
    }

    public void setApTotal(long apTotal) {
        this.apTotal = apTotal;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public void setEndTs(String endTs) {
        this.endTs = endTs;
    }

    public void setQps(double qps) {
        this.qps = qps;
    }

    public void setStartTS(String startTS) {
        this.startTS = startTS;
    }

    public void setTps(double tps) {
        this.tps = tps;
    }

    public void setTpTotal(long tpTotal) {
        this.tpTotal = tpTotal;
    }

    public int getApclient() {
        return apclient;
    }

    public void setApclient(int apclient) {
        this.apclient = apclient;
    }

    public int getTpclient() {
        return tpclient;
    }

    public void setTpclient(int tpclient) {
        this.tpclient = tpclient;
    }

    public String getDbType() {
        return dbType;
    }

    public long getTpTotal() {
        return tpTotal;
    }

    public long getApTotal() {
        return apTotal;
    }

    public double getTps() {
        return tps;
    }

    public double getQps() {
        return qps;
    }

    public String getStartTS() {
        return startTS;
    }

    public String getEndTs() {
        return endTs;
    }

    public double getFresh(){
        return freshness;
    }

    public void setFresh(double freshness){
        this.freshness = freshness;
    }

    public void printResult(int type){
        System.out.println("====================Test Summary========================");
        System.out.println("Test starts at " + getStartTS());
        System.out.println("Test ends at " + getEndTs());
        System.out.println("AP Concurrency is " + getApclient());
        System.out.println("TP Concurrency is " + getTpclient());
        switch(type){
            case 0:
                System.out.println("Total amount of AP Queries is " + getApTotal());
                System.out.println("Total amount of TP Transaction is " + getTpTotal());
                System.out.println("XP-QPS is " + getXpqps());
                System.out.println("XP-TPS is " + getXptps());
                break;
            case 1:
                System.out.println("Total amount of TP Transaction is " + getTpTotal());
                System.out.println("TPS is " + getTps());
                break;
            case 2:
                System.out.println("Total amount of AP Queries is " + getApTotal());
                System.out.println("QPS is " + getQps());
                break;
            case 3:
                System.out.println("Total amount of TP Transaction is " + getTpTotal());
                System.out.println("TPS is " + getTps());
                System.out.println("Total amount of AP Queries is " + getApTotal());
                System.out.println("QPS is " + getQps());
                System.out.println("Total amount of AP Queries is " + getApTotal());
                System.out.println("Total amount of TP Transaction is " + getTpTotal());
                System.out.println("XP-QPS is " + getXpqps());
                System.out.println("XP-TPS is " + getXptps());
                break;
            case 4:
                System.out.println("Total amount of AP Queries is " + getApTotal());
                System.out.println("Total amount of TP Transaction is " + getTpTotal());
                System.out.println("Fresh-XP-QPS is " + getXpqps());
                System.out.println("Fresh-XP-TPS is " + getXptps());
                break;
            case 6:
                System.out.println("Total amount of TP Transaction is " + getTpTotal());
                System.out.println("TPS is " + getTps());
                System.out.println("Total amount of AP Queries is " + getApTotal());
                System.out.println("QPS is " + getQps());
                System.out.println("Total amount of AP Queries is " + getApTotal());
                System.out.println("Total amount of TP Transaction is " + getTpTotal());
                System.out.println("Fresh-XP-QPS is " + getXpqps());
                System.out.println("Fresh-XP-TPS is " + getXptps());
                break;
        }
        System.out.println("Query/Transaction response time(ms) histogram : ");
        if( type == 2 || type == 6) {
            System.out.println("------------AP-------------------");
            for (int apidx = 0; apidx < 12; apidx++) {
                System.out.printf("AP Query %2d max rt : %10.2f min rt : %10.2f avg rt : %10.2f 95%% rt : %10.2f 99%% rt : %10.2f \n",
                        (apidx + 1),
                        hist.getAPItem(apidx).getMax(),
                        hist.getAPItem(apidx).getMin(),
                        hist.getAPItem(apidx).getMean(),
                        hist.getAPItem(apidx).getPercentile(95),
                        hist.getAPItem(apidx).getPercentile(99));
            }
        }

        if(type == 1 || type == 6) {
            System.out.println("------------TP-------------------");
            for (int tpidx = 0; tpidx < 16; tpidx++) {
                System.out.printf("TP Transaction %2d max rt : %10.2f min rt : %10.2f avg rt : %10.2f 95%% rt : %10.2f 99%% rt : %10.2f \n",
                        (tpidx + 1),
                        hist.getTPItem(tpidx).getMax(),
                        hist.getTPItem(tpidx).getMin(),
                        hist.getTPItem(tpidx).getMean(),
                        hist.getTPItem(tpidx).getPercentile(95),
                        hist.getTPItem(tpidx).getPercentile(99));
            }
        }

        if(type == 0 || type == 4 || type == 6){
            System.out.println("-----------XP-IQ--------------------");
            for (int xpidx = 0; xpidx < 6; xpidx++) {
                System.out.printf("Interative Query %d max rt : %10.2f min rt : %10.2f avg rt : %10.2f 95%% rt : %10.2f 99%% rt : %10.2f \n",
                        (xpidx + 1),
                        hist.getXPIQItem(xpidx).getMax(),
                        hist.getXPIQItem(xpidx).getMin(),
                        hist.getXPIQItem(xpidx).getMean(),
                        hist.getXPIQItem(xpidx).getPercentile(95),
                        hist.getXPIQItem(xpidx).getPercentile(99));
            }
        }

        if(type == 0 || type == 4 || type == 6) {
            System.out.println("-----------XP-AT--------------------");
            for (int tpidx = 0; tpidx < 6; tpidx++) {
                System.out.printf("Analytical Transaction AT%d max rt : %10.2f min rt : %10.2f avg rt : %10.2f 95%% rt : %10.2f 99%% rt : %10.2f \n",
                        (tpidx + 1),
                        hist.getXPATItem(tpidx).getMax(),
                        hist.getXPATItem(tpidx).getMin(),
                        hist.getXPATItem(tpidx).getMean(),
                        hist.getXPATItem(tpidx).getPercentile(95),
                        hist.getXPATItem(tpidx).getPercentile(99));
            }
        }

        if( type == 3 ) {
            System.out.println("-----------HTAP-Summary--------------------");
            System.out.println("-----------AP-Part--------------------");
            System.out.printf("QPS : %10.2f \n",qps );
            System.out.println("-----------TP-Part--------------------");
            System.out.printf("TPS : %10.2f \n",tps );
            System.out.println("-----------XP-Part--------------------");
            System.out.printf("XP-QPS : %10.2f \n",xpqps );
            System.out.printf("XP-TPS : %10.2f \n",xptps );
            System.out.println("-----------HTAP-Score--------------------");
            System.out.printf("Geometric Mean : %10.2f \n", Math.pow(qps*tps*(xpqps+xptps), 1/3.0));
        }

        if( type == 6 ) {
            System.out.println("-----------HTAP-Summary--------------------");
            System.out.println("-----------AP-Part--------------------");
            System.out.printf("QPS : %10.2f \n",qps );
            System.out.println("-----------TP-Part--------------------");
            System.out.printf("TPS : %10.2f \n",tps );
            System.out.println("-----------XP-Part--------------------");
            System.out.printf("XP-QPS : %10.2f \n",xpqps );
            System.out.printf("XP-TPS : %10.2f \n",xptps );
            System.out.println("-----------Avg-Freshness-Score--------------------");
            System.out.printf("Freshness(ms) : %10.2f \n", getFresh() * 1.0);
            System.out.println("-----------HTAP-Score--------------------");
            System.out.printf("Geometric Mean : %10.2f \n", Math.pow(qps*tps*(xpqps+xptps), 1/3.0)/(1+getFresh()/1000));
        }

        if(type == 4){
            System.out.println("-----------Avg-Freshness-Score--------------------");
            System.out.printf("Freshness(ms) : %10.2f \n", getFresh() * 1.0);
        }

        System.out.println("====================Thank you!========================");
    }


    public double getXptps() {
        return xptps;
    }

    public void setXpqps(double xpqps) {
        this.xpqps = xpqps;
    }

    public double getXpqps() {
        return xpqps;
    }

    public void setXptps(double xptps) {
        this.xptps = xptps;
    }

    public double getAtps() {
        return atps;
    }

    public void setIqps(double iqps) {
        this.iqps = iqps;
    }

    public double getIqps() {
        return iqps;
    }

    public void setAtps(double atps) {
        this.atps = atps;
    }
}
