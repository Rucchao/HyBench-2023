package com.hybench.load;
/**
 *
 * @time 2023-03-04
 * @version 1.0.0
 * @file ConfigReader.java
 * @description
 *
 **/

import com.moandjiezana.toml.Toml;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfigReader {
    static final ClassLoader loader = ConfigReader.class.getClassLoader();
    public long customer_number;
    public long company_number;
    public int customer_age_lower;
    public int customer_age_upper;
    public String customer_datapath;
    public double customer_loanbalance;
    public double customer_savingbalance;
    public double customer_checkingbalance;
    public int company_size_lower;
    public int company_size_upper;
    public String company_datapath;
    public double company_loanbalance;
    public double company_savingbalance;
    public double company_checkingbalance;
    public int startYear;
    public int midPoint;
    public int endYear;
    public Date startDate;
    public Date midPointDate;
    public Date endDate;
    public Date loanDate;
    public String savingaccount_datapath;
    public String checkingaccount_datapath;
    public String transfer_datapath;
    public String checking_datapath;
    public String loanapps_datapath;
    public String loantrans_datapath;
    public long transfer_number;
    public long checking_number;
    public long loanapps_number;
    public long loantrans_number;
    public double cust_rate;
    public double loan_rate;
    public double prob_blocked;
    public ConfigReader(String scale_factor){
        load("parameters.toml", scale_factor);
    }

    private void load(String fileName, String scale_factor) {
        try {
            BufferedReader Br = new BufferedReader(
                    new InputStreamReader(loader.getResourceAsStream(fileName)));
            Toml toml = new Toml().read(Br);
            // general
            startYear = toml.getLong(scale_factor + ".startYear").intValue();
            midPoint = toml.getLong(scale_factor + ".midPoint").intValue();
            endYear = toml.getLong(scale_factor + ".endYear").intValue();
            String startDate_str = toml.getString(scale_factor + ".startDate");
            String midDate_str = toml.getString(scale_factor + ".midPointDate");
            String endDate_str = toml.getString(scale_factor + ".endDate");
            String loanDate_str = toml.getString(scale_factor + ".LoanDate");
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate_str);
            midPointDate = new SimpleDateFormat("yyyy-MM-dd").parse(midDate_str);
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate_str);
            loanDate = new SimpleDateFormat("yyyy-MM-dd").parse(loanDate_str);
            cust_rate = toml.getDouble(scale_factor + ".cust_rate").doubleValue();
            prob_blocked = toml.getDouble(scale_factor + ".prob_blocked").doubleValue();
            savingaccount_datapath = toml.getString(scale_factor + ".savingaccount_datapath");
            checkingaccount_datapath = toml.getString(scale_factor + ".checkingaccount_datapath");
            transfer_number = toml.getLong(scale_factor + ".transfer_number");
            checking_number = toml.getLong(scale_factor + ".checking_number");
            transfer_datapath = toml.getString(scale_factor + ".tranfer_datapath");
            checking_datapath = toml.getString(scale_factor + ".checking_datapath");
            // customer
            customer_number = toml.getLong(scale_factor + ".customer_number");
            customer_age_lower = toml.getLong(scale_factor + ".customer_age_lower").intValue();
            customer_age_upper = toml.getLong(scale_factor + ".customer_age_upper").intValue();
            customer_datapath = toml.getString(scale_factor + ".customer_datapath");
            customer_loanbalance = toml.getLong(scale_factor + ".customer_loanbalance").doubleValue();
            customer_savingbalance = toml.getLong(scale_factor + ".customer_savingbalance").doubleValue();
            customer_checkingbalance = toml.getLong(scale_factor + ".customer_checkingbalance").doubleValue();
            // company
            company_number = toml.getLong(scale_factor + ".company_number");
            company_size_lower = toml.getLong(scale_factor + ".company_size_lower").intValue();
            company_size_upper = toml.getLong(scale_factor + ".company_size_upper").intValue();
            company_datapath = toml.getString(scale_factor + ".company_datapath");
            company_loanbalance = toml.getLong(scale_factor + ".company_loanbalance").doubleValue();
            company_savingbalance = toml.getLong(scale_factor + ".company_savingbalance").doubleValue();
            company_checkingbalance = toml.getLong(scale_factor + ".company_checkingbalance").doubleValue();
            // loanapps
            loanapps_number = toml.getLong(scale_factor + ".loanapps_number").intValue();
            loanapps_datapath = toml.getString(scale_factor + ".loanapps_datapath");
            loan_rate = toml.getDouble(scale_factor + ".loan_rate").doubleValue();
            // loantrans
            loantrans_number = toml.getLong(scale_factor + ".loantrans_number").intValue();
            loantrans_datapath = toml.getString(scale_factor + ".loantrans_datapath");
        }
        catch (ParseException e){
            e.printStackTrace();
        }
    }
}
