package com.hybench.workload;
/**
 *
 * @time 2023-03-04
 * @version 1.0.0
 * @file Freshness.java
 * @description
 *   calc freshness.
 *   two async threads call the max timestamp in transfer table concurrently and compare the difference.
 **/

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Freshness {
    public static Logger logger = LogManager.getLogger(Freshness.class);
    int testid = Client.testid;;
    int dbType;
    Sqlstmts sqls =null;
    Connection conn_tp = null;
    Connection conn_ap = null;

    public Freshness(int dbType,Connection ctp,Connection cap,Sqlstmts sqls){
        this.dbType = dbType;
        conn_tp = ctp;
        conn_ap = cap;
        this.sqls = sqls;
    }

    public Long calcFreshness (){
        long freshness = 0;
        CompletableFuture<Long> queryAP =
                CompletableFuture.supplyAsync(() -> {return getMaxAPTs();});
        CompletableFuture<Long> queryTP =
                CompletableFuture.supplyAsync(() -> {return getMaxTPTs();});

        CompletableFuture.allOf(queryAP,queryTP).join();
        try {
            freshness = Long.valueOf(queryTP.get()) - Long.valueOf(queryAP.get());
            if(freshness < 0)
                freshness = 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("Current freshness : " + freshness);
        return freshness;
    }

    public Long getMaxTPTs(){
        PreparedStatement pstmt_tp = null;
        ResultSet rs_tp = null;
        Timestamp max_ts_tp = new Timestamp(System.currentTimeMillis());
        try {
            pstmt_tp = conn_tp.prepareStatement(sqls.fresh_iq1());
            pstmt_tp.setInt(1,testid);
            rs_tp = pstmt_tp.executeQuery();
            if(rs_tp.next()){
                Timestamp ret = rs_tp.getTimestamp(1);
                if( ret != null){
                    max_ts_tp = ret;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt_tp.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return max_ts_tp.getTime();
    }

    public Long getMaxAPTs(){
        PreparedStatement pstmt_ap = null;
        ResultSet rs_ap = null;
        Timestamp max_ts_ap = new Timestamp(System.currentTimeMillis());
        try {
            // TiDB freshness evaluation
//            String fresh_ap="select /*+ read_from_storage(tiflash[t,c]) */ t.fresh_ts, t.transfer_ts, t.amount, c.custid, c.name from transfer as t, customer as c\n" +
//                    " where t.targetid=? and t.targetid=c.custid and t.fresh_ts is not null\n" +
//                    " order by t.fresh_ts DESC limit 1";
//            pstmt_ap = conn_tp.prepareStatement(fresh_ap);
            pstmt_ap = conn_ap.prepareStatement(sqls.fresh_iq1());
            pstmt_ap.setInt(1,testid);
            rs_ap = pstmt_ap.executeQuery();
            if(rs_ap.next()){
                Timestamp ret = rs_ap.getTimestamp(1);
                if( ret != null){
                    max_ts_ap = ret;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt_ap.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return max_ts_ap.getTime();
    }
}
