package com.hybench.workload;
/**
 *
 * @file Freshness2.java
 * @description
 *   calc freshness.
 **/

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Freshness2 {
    public static Logger logger = LogManager.getLogger(Freshness2.class);
    int testid1 = Client.testid1;
    int testid2 = Client.testid2;
    HashSet<Integer> delete_set1 = Client.delete_set1;
    HashSet<Integer> delete_set2 = Client.delete_set2;
    int dbType;
    Sqlstmts sqls =null;
    Connection conn_tp = null;
    Connection conn_ap = null;
    Timestamp startTime = null;
    long curfreshness = 0;
    long ts_Q1 =0;
    long ts_Q2 =0;
    long max_Q1 =0;
    long max_Q2 =0;

    public Freshness2(int dbType,Connection ctp,Connection cap,Sqlstmts sqls,Timestamp startTime){
        this.dbType = dbType;
        conn_tp = ctp;
        conn_ap = cap;
        this.sqls = sqls;
        this.startTime = startTime;
    }

    public Long calcFreshness (){
        long freshness1 = 0;
        long freshness2 = 0;
        logger.info("testid1 is : " + testid1);
        logger.info("testid2 is : " + testid2);
        CompletableFuture<HashMap<Integer,Long>> queryTP1 =
                CompletableFuture.supplyAsync(() -> {return getTPTsList1();});
        CompletableFuture<HashMap<Integer,Long>> queryAP1 =
                CompletableFuture.supplyAsync(() -> {return getAPTsList1();});

        CompletableFuture.allOf(queryAP1,queryTP1).join();
        try {
            HashMap<Integer,Long> ret_tp = queryTP1.get();
            HashMap<Integer,Long> ret_ap = queryAP1.get();

            // get the union of the keyset
            HashSet<Integer> union = new HashSet <Integer>();
            union.addAll(ret_tp.keySet());
            union.addAll(ret_ap.keySet());

            Iterator<Integer> IDIterator = union.iterator();

            while(IDIterator.hasNext()){
                Integer tid = IDIterator.next();
                long ts_ap = 0;
                long ts_tp = 0;
                long diff=0;

                // update
                if(ret_ap.containsKey(tid) && ret_tp.containsKey(tid)){
                     ts_ap = ret_ap.get(tid);
                     ts_tp = ret_tp.get(tid);
                     diff = ts_tp - ts_ap;
                    if (diff>0){
                        logger.info("TP Query starting time is : " + ts_Q1);
                        logger.info("Q1: this is an update case!");
                        logger.info("join id is "+tid);
                        logger.info("ts_tp is : " + ts_tp);
                        logger.info("ts_ap is : " + ts_ap);
                        logger.info("diff is : " + diff);
                    }
                }
                // insert
                else if(ret_tp.containsKey(tid) && !ret_ap.containsKey(tid)){
                    ts_tp = ret_tp.get(tid);
                    if(ts_tp>max_Q1){
                        logger.info("Q1: this is an insert case!");
                        diff = ts_Q1 - ts_tp;
                        logger.info("ts_tp is : " + ts_tp);
                        logger.info("ts_ap is : " + ts_ap);
                        logger.info("the insert diff is: "+diff);
                    }
                }
                // delete
                else if(ret_ap.containsKey(tid) && !ret_tp.containsKey(tid) && delete_set1.contains(tid)){
                    ts_ap = ret_ap.get(tid);
                    logger.info("Q1: this is an delete case!");
                    diff = ts_Q1 - ts_ap;
                    logger.info("the delete diff is: "+diff);
                }
                // max
                if(diff > freshness1)
                    freshness1 = diff;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        logger.info("Current freshness with test id 1: " + freshness1);

        CompletableFuture<HashMap<Integer,Long>> queryTP2 =
                CompletableFuture.supplyAsync(() -> {return getTPTsList2();});
        CompletableFuture<HashMap<Integer,Long>> queryAP2 =
                CompletableFuture.supplyAsync(() -> {return getAPTsList2();});

        CompletableFuture.allOf(queryAP2,queryTP2).join();

        try {
            HashMap<Integer,Long> ret_tp = queryTP2.get();
            HashMap<Integer,Long> ret_ap = queryAP2.get();

            // get the union of the keyset
            HashSet<Integer> union = new HashSet <Integer>();
            union.addAll(ret_tp.keySet());
            union.addAll(ret_ap.keySet());

            Iterator<Integer> IDIterator = union.iterator();

            while(IDIterator.hasNext()){
                Integer tid = IDIterator.next();
                long ts_ap = 0;
                long ts_tp = 0;
                long diff=0;

                if(ret_ap.containsKey(tid) && ret_tp.containsKey(tid)){
                    ts_ap = ret_ap.get(tid);
                    ts_tp = ret_tp.get(tid);
                    diff = ts_tp - ts_ap;
                    if(diff>0){
                        logger.info("TP Query starting time is : " + ts_Q1);
                        logger.info("Q2: this is an update case!");
                        logger.info("join id is "+tid);
                        logger.info("ts_tp is : " + ts_tp);
                        logger.info("ts_ap is : " + ts_ap);
                        logger.info("diff is : " + diff);
                    }
                }
                // insert
                else if(ret_tp.containsKey(tid) && !ret_ap.containsKey(tid)){
                    ts_tp = ret_tp.get(tid);
                    if(ts_tp>max_Q2){
                        logger.info("Q2: this is an insert case!");
                        logger.info("TP Query starting time is : " + ts_Q2);
                        logger.info("ts_tp is : " + ts_tp);
                        logger.info("ts_ap is : " + ts_ap);
                        diff = ts_Q2 - ts_tp;
                        logger.info("the insert diff is: "+diff);
                    }
                }
                // delete
                else if(ret_ap.containsKey(tid) && !ret_tp.containsKey(tid) && delete_set2.contains(tid)){
                    ts_ap = ret_ap.get(tid);
                    logger.info("Q2: this is an delete case!");
                    logger.info("TP Query starting time is : " + ts_Q2);
                    diff = ts_Q2 - ts_ap;
                    logger.info("the delete diff is: "+diff);
                }
                // max
                if(diff > freshness2)
                    freshness2 = diff;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        logger.info("Current freshness with test id 2: " + freshness2);

        return freshness1>freshness2 ? freshness1:freshness2;
    }

    public long getFresh(){
        return curfreshness;
    }

    public HashMap<Integer,Long> getTPTsList1(){
        HashMap<Integer,Long> list = new HashMap<Integer,Long>();
        PreparedStatement pstmt_tp = null;
        ResultSet rs_tp = null;
        Integer counter=0;
        try {
            pstmt_tp = conn_tp.prepareStatement(sqls.fresh_iq());
            pstmt_tp.setInt(1,testid1);
            ts_Q1 = System.currentTimeMillis();
            rs_tp = pstmt_tp.executeQuery();

          //  logger.info("fresh query is "+sqls.fresh_iq());
            while(rs_tp.next()){
                Timestamp ret = rs_tp.getTimestamp(2);
                if( ret != null){
                    list.put(rs_tp.getInt(1),rs_tp.getTimestamp(2).getTime());
                }
            }
            rs_tp.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt_tp.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public HashMap<Integer,Long> getAPTsList1(){
        HashMap<Integer,Long> list = new HashMap<Integer,Long>();
        PreparedStatement pstmt_ap = null;
        ResultSet rs_ap = null;
        try {
            pstmt_ap = conn_ap.prepareStatement(sqls.fresh_iq());
            pstmt_ap.setInt(1,testid1);
            rs_ap = pstmt_ap.executeQuery();

            while(rs_ap.next()){
                Timestamp ret = rs_ap.getTimestamp(2);
                if(rs_ap.isFirst()){
                    max_Q1=ret.getTime();
                }
                if( ret != null){
                    list.put(rs_ap.getInt(1),rs_ap.getTimestamp(2).getTime());
                }
            }
            rs_ap.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt_ap.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public HashMap<Integer,Long> getTPTsList2(){
        HashMap<Integer,Long> list = new HashMap<Integer,Long>();
        PreparedStatement pstmt_tp = null;
        ResultSet rs_tp = null;
        try {
            pstmt_tp = conn_tp.prepareStatement(sqls.fresh_iq1());
            pstmt_tp.setInt(1,testid2);
            ts_Q2 = System.currentTimeMillis();
            rs_tp = pstmt_tp.executeQuery();
            while(rs_tp.next()){
                Timestamp ret = rs_tp.getTimestamp(2);
                if( ret != null){
                    list.put(rs_tp.getInt(1),rs_tp.getTimestamp(2).getTime());
                }
            }
            rs_tp.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt_tp.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public HashMap<Integer,Long> getAPTsList2(){
        HashMap<Integer,Long> list = new HashMap<Integer,Long>();
        PreparedStatement pstmt_ap = null;
        ResultSet rs_ap = null;
        try {
            pstmt_ap = conn_ap.prepareStatement(sqls.fresh_iq1());
            pstmt_ap.setInt(1,testid2);
            rs_ap = pstmt_ap.executeQuery();
            while(rs_ap.next()){
                Timestamp ret = rs_ap.getTimestamp(2);
                if(rs_ap.isLast()){
                    max_Q2=ret.getTime();
                }
                if( ret != null){
                    list.put(rs_ap.getInt(1),rs_ap.getTimestamp(2).getTime());
                }
            }
            rs_ap.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt_ap.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

}
