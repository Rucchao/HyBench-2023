package com.hybench.workload;

/**
 *
 * @version 1.00
 * @time 2023-03-07
 * @file SqlReader.java
 **/

import com.moandjiezana.toml.Toml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;


public class SqlReader {
    String filePath = null;
    Sqlstmts sqls = null;


    public SqlReader(String filePath){
        this.filePath = filePath;
    }
    // read sqls from sql files
    public Sqlstmts loader(){
        sqls = new Sqlstmts();
        try{
            BufferedReader Br = new BufferedReader(new FileReader(filePath));
            Toml toml = new Toml().read(Br);

            
            sqls.setAp_q1(toml.getString("AP-1.sql"));
            sqls.setAp_q2(toml.getString("AP-2.sql"));
            sqls.setAp_q2_1(toml.getString("AP-2.1.sql"));
            sqls.setAp_q3(toml.getString("AP-3.sql"));
            sqls.setAp_q4(toml.getString("AP-4.sql"));
            sqls.setAp_q5(toml.getString("AP-5.sql"));
            sqls.setAp_q6(toml.getString("AP-6.sql"));
            sqls.setAp_q7(toml.getString("AP-7.sql"));
            sqls.setAp_q8(toml.getString("AP-8.sql"));
            sqls.setAp_q9(toml.getString("AP-9.sql"));
            sqls.setAp_q10(toml.getString("AP-10.sql"));
            sqls.setAp_q11(toml.getString("AP-11.sql"));
            sqls.setAp_q12(toml.getString("AP-12.sql"));
            sqls.setAp_q13(toml.getString("AP-13.sql"));

            sqls.setAp_iq1(toml.getString("IQ-1.sql"));
            sqls.setAp_iq2(toml.getString("IQ-2.sql"));
            sqls.setAp_iq3(toml.getString("IQ-3.sql"));
            sqls.setAp_iq4(toml.getString("IQ-4.sql"));
            sqls.setAp_iq5(toml.getString("IQ-5.sql"));
            sqls.setAp_iq5_1(toml.getString("IQ-5.1.sql"));
            sqls.setAp_iq6(toml.getString("IQ-6.sql"));

            sqls.setTp_at00(getSqlArrayFromList(toml.getList("AT-00.sql")));
            sqls.setTp_at0(getSqlArrayFromList(toml.getList("AT-0.sql")));
            sqls.setTp_at1(getSqlArrayFromList(toml.getList("AT-1.sql")));
            sqls.setTp_at2(getSqlArrayFromList(toml.getList("AT-2.sql")));
            sqls.setTp_at3(getSqlArrayFromList(toml.getList("AT-3.sql")));
            sqls.setTp_at3_1(toml.getString("AT-3.1.sql"));
            sqls.setTp_at4(getSqlArrayFromList(toml.getList("AT-4.sql")));
            sqls.setTp_at4_1(toml.getString("AT-4.1.sql"));
            sqls.setTp_at5(getSqlArrayFromList(toml.getList("AT-5.sql")));
            sqls.setTp_at5_1(toml.getString("AT-5.1.sql"));
            sqls.setTp_at6(getSqlArrayFromList(toml.getList("AT-6.sql")));
            sqls.setTp_at6_1(toml.getString("AT-6.1.sql"));

            sqls.setTp_txn1(toml.getString("TP-1.sql"));
            sqls.setTp_txn2(toml.getString("TP-2.sql"));
            sqls.setTp_txn3(toml.getString("TP-3.sql"));
            sqls.setTp_txn4(toml.getString("TP-4.sql"));
            sqls.setTp_txn5(toml.getString("TP-5.sql"));
            sqls.setTp_txn6(toml.getString("TP-6.sql"));
            sqls.setTp_txn7(toml.getString("TP-7.sql"));
            sqls.setTp_txn8(toml.getString("TP-8.sql"));
            sqls.setTp_txn9(getSqlArrayFromList(toml.getList("TP-9.sql")));
            sqls.setTp_txn10(getSqlArrayFromList(toml.getList("TP-10.sql")));
            sqls.setTp_txn11(getSqlArrayFromList(toml.getList("TP-11.sql")));
            sqls.setTp_txn12(getSqlArrayFromList(toml.getList("TP-12.sql")));
            sqls.setTp_txn13(getSqlArrayFromList(toml.getList("TP-13.sql")));
            sqls.setTp_txn14(getSqlArrayFromList(toml.getList("TP-14.sql")));
            sqls.setTp_txn15(getSqlArrayFromList(toml.getList("TP-15.sql")));
            sqls.setTp_txn16(getSqlArrayFromList(toml.getList("TP-16.sql")));
            sqls.setTp_txn17(getSqlArrayFromList(toml.getList("TP-17.sql")));
            sqls.setTp_txn18(getSqlArrayFromList(toml.getList("TP-18.sql")));

            sqls.setFresh_iq(toml.getString("fresh.sql"));
            sqls.setFresh_iq1(toml.getString("fresh-1.sql"));

        }catch(Exception e){
            e.printStackTrace();
        }
        return sqls;
    }

    public String[] getSqlArrayFromList(List<String> sqlList){
        String[] sqls = new String[sqlList.size()];
        for(int i = 0;i < sqlList.size();i++){
            sqls[i] = sqlList.get(i);
        }
        return sqls;
    }

    public Sqlstmts getSqls(){
        return this.sqls;
    }
}
