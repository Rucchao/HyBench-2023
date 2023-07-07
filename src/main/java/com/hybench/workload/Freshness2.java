package com.hybench.workload;
/**
 *
 * @time 2023-07-06
 * @version 1.0.0
 * @file Freshness.java
 * @description
 *   calc freshness.
 **/

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Freshness2 {
    public static Logger logger = LogManager.getLogger(Freshness2.class);
    int testid = Client.testid;;
    int dbType;
    Sqlstmts sqls =null;
    Connection conn_tp = null;
    Connection conn_ap = null;
    Timestamp startTime = null;
    long curfreshness = 0;

    public Freshness2(int dbType,Connection ctp,Connection cap,Sqlstmts sqls,Timestamp startTime){
        this.dbType = dbType;
        conn_tp = ctp;
        conn_ap = cap;
        this.sqls = sqls;
        this.startTime = startTime;
    }

    public Long calcFreshness (){
        long freshness = 0;
        CompletableFuture<HashMap<Integer,Long>> queryAP =
                CompletableFuture.supplyAsync(() -> {return getAPTsList();});
        CompletableFuture<HashMap<Integer,Long>> queryTP =
                CompletableFuture.supplyAsync(() -> {return getTPTsList();});

        CompletableFuture.allOf(queryAP,queryTP).join();
        try {
            HashMap<Integer,Long> ret_ap = queryAP.get();
            HashMap<Integer,Long> ret_tp = queryTP.get();

            for(Integer cid : ret_tp.keySet()){
                long ts_ap = ret_ap.get(cid);
                long ts_tp = ret_tp.get(cid);
                if(ts_ap != 0 && (ts_tp - ts_ap) > freshness )
                    freshness = ts_tp - ts_ap;
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        logger.info("Current freshness : " + freshness);
        return freshness;
    }

    public long getFresh(){
        return curfreshness;
    }

    public HashMap<Integer,Long> getTPTsList(){
        HashMap<Integer,Long> list = new HashMap<Integer,Long>();
        PreparedStatement pstmt_tp = null;
        ResultSet rs_tp = null;
        Timestamp max_ts_tp = startTime;
        try {
            pstmt_tp = conn_tp.prepareStatement(sqls.fresh_iq1());
            pstmt_tp.setInt(1,testid);
            rs_tp = pstmt_tp.executeQuery();
            if(rs_tp.next()){
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

    public HashMap<Integer,Long> getAPTsList(){
        HashMap<Integer,Long> list = new HashMap<Integer,Long>();
        PreparedStatement pstmt_ap = null;
        ResultSet rs_ap = null;
        Timestamp max_ts_ap = startTime;
        try {
            pstmt_ap = conn_ap.prepareStatement(sqls.fresh_iq1());
            pstmt_ap.setInt(1,testid);
            rs_ap = pstmt_ap.executeQuery();
            if(rs_ap.next()){
                Timestamp ret = rs_ap.getTimestamp(2);
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
