package com.hybench.load;
/**
 * @time 2023-03-09
 * @version 1.0.0
 * @file DataGenerator_RiskControlling.java
 * @description
 *
 **/

import com.hybench.ConfigLoader;
import com.hybench.pojo.*;
import com.hybench.util.RandomGenerator;

import java.io.*;
import java.util.*;

public class DataGenerator_RiskControlling {
    private String sf = "1x";

    public DataGenerator_RiskControlling(String sf){
        this.sf = sf;
    }

    public Integer[] reservoir_sampling(String filename, Integer[] list) throws IOException {
        File f = new File(filename);
        BufferedReader b = new BufferedReader(new FileReader(f));

        String l;
        int c = 0, r;
        Random rg = new Random();

        while((l = b.readLine()) != null)
        {
            if (c < list.length)
                r = c++;
            else
                r = rg.nextInt(++c);

            if (r < list.length)
                list[r] = Integer.parseInt(l);
        }
        b.close();
        return list;
    }

    public void dataGenerator() {
        System.out.println("This is a data generator of HyBench, Version 0.1");
        System.out.println("----------------");
        System.out.println("----------------");
        System.out.println("----------------");
        System.out.println("Data is generating...");
        System.out.println("----------------");
        System.out.println("----------------");
        System.out.println("----------------");
        long millisStart = System.currentTimeMillis();

        DataSource DS = new DataSource();
        RandomGenerator RG = new RandomGenerator();
        // input the scale factor, e.g. 1x or 10x
        ConfigReader CR = new ConfigReader(sf);
        FileWriter cust_fileWriter = null;
        BufferedWriter cust_bufferedWriter = null;
        String cust_writePath = CR.customer_datapath;
        FileWriter company_fileWriter = null;
        BufferedWriter company_bufferedWriter = null;
        String company_writePath = CR.company_datapath;
        FileWriter savingAccount_fileWriter = null;
        BufferedWriter savingAccount_bufferedWriter = null;
        String savingAccount_writePath = CR.savingaccount_datapath;
        FileWriter checkingAccount_fileWriter = null;
        BufferedWriter checkingAccount_bufferedWriter = null;
        String checkingAccount_writePath = CR.checkingaccount_datapath;
        FileWriter transfer_fileWriter = null;
        BufferedWriter transfer_bufferedWriter = null;
        String transfer_writePath = CR.transfer_datapath;
        FileWriter checking_fileWriter = null;
        BufferedWriter checking_bufferedWriter = null;
        String checking_writePath = CR.checking_datapath;
        FileWriter loanapps_fileWriter = null;
        BufferedWriter loanapps_bufferedWriter = null;
        String loanapps_writePath = CR.loanapps_datapath;
        FileWriter loantrans_fileWriter = null;
        BufferedWriter loantrans_bufferedWriter = null;
        String loantrans_writePath = CR.loantrans_datapath;
        String DataPath = "Data_" + sf + "/";
        String NEW_LINE_SEPARATOR = "\n";
        Long customerNumber = CR.customer_number;
        Long companyNumber = CR.company_number;
        int customer_size = customerNumber.intValue();
        int company_size = companyNumber.intValue();
        int account_bound = customerNumber.intValue() + companyNumber.intValue();
        int blocked_num = (int) (account_bound * CR.prob_blocked);

        // Generate blocked ids with prob_blocked
        Set<Integer> Blocked_ids = new HashSet<>();
        // AT1 Risk Controlling
        List<Integer> Related_Blocked_Transfer_ids = new ArrayList<>();
        FileWriter blocked_transfer_fileWriter = null;
        BufferedWriter blocked_transfer_bufferedWriter = null;
        // AT2 Risk Controlling
        List<Integer> Related_Blocked_Checking_ids = new ArrayList<>();
        FileWriter blocked_checking_fileWriter = null;
        BufferedWriter blocked_checking_bufferedWriter = null;

        for (int i = 0; i < blocked_num; i++) {
            int id = RG.getRandomint(account_bound);
            Blocked_ids.add(id);
        }

        File directory = new File(DataPath);
        if (!directory.exists())
            directory.mkdirs();
        try {
            cust_fileWriter = new FileWriter(DataPath + cust_writePath, false);
            cust_bufferedWriter = new BufferedWriter(cust_fileWriter);
            savingAccount_fileWriter = new FileWriter(DataPath + savingAccount_writePath, true);
            savingAccount_bufferedWriter = new BufferedWriter(savingAccount_fileWriter);
            checkingAccount_fileWriter = new FileWriter(DataPath + checkingAccount_writePath, true);
            checkingAccount_bufferedWriter = new BufferedWriter(checkingAccount_fileWriter);

            blocked_transfer_fileWriter =  new FileWriter(DataPath + "blocked_transfer.csv", false);
            blocked_transfer_bufferedWriter = new BufferedWriter(blocked_transfer_fileWriter);


            blocked_checking_fileWriter =  new FileWriter(DataPath + "blocked_checking.csv", false);
            blocked_checking_bufferedWriter = new BufferedWriter(blocked_checking_fileWriter);


            // generate the Customers with accounts
            for (int i = 1; i <= CR.customer_number; i++) {
                // generate the gender
                String gender = RG.getRandomString(DS.gender);
                StringBuilder Namebuilder = new StringBuilder();
                Namebuilder.append(RG.getRandomItem(DS.LastName_list)).append(" ");
                // generate first name with gender
                if (gender.equals("female"))
                    Namebuilder.append(RG.getRandomItem(DS.FirstName_female_list));
                else Namebuilder.append(RG.getRandomItem(DS.FirstName_male_list));
                // generate the companyID
                Long cid = RG.getRandomLong(1 + CR.customer_number, CR.customer_number + CR.company_number);
                int companyId = cid.intValue();
                // generate the age
                int age = RG.getRandomint(CR.customer_age_lower, CR.customer_age_upper);
                // generate the phone
                String phone = RG.getRandomPhone();
                // generate the province and citylist
                String province = RG.getRandomProvince();
                List<String> citylist = DS.Province_Cities_Map.get(province);
                // generate the city
                String city = RG.getRandomItem(citylist);
                // generate the timestamp
                Date date = RG.getRandomTimestamp(CR.startDate, CR.midPointDate);
                // generate the blocked label
                int blocked=0;
                if(Blocked_ids.contains(i))
                    blocked=1;

                // generate a new customer
                Customer cust = new Customer(i, companyId, gender, Namebuilder.toString(), age, phone, province, city, 0, CR.customer_loanbalance, 0, 0, 0, blocked, date, date);
                cust_bufferedWriter.write(cust.toString());
                cust_bufferedWriter.write(NEW_LINE_SEPARATOR);

                // get the customer saving balance
                SavingAccount sa = new SavingAccount(i, i, CR.customer_savingbalance, blocked, date, null);
                savingAccount_bufferedWriter.write(sa.toString());
                savingAccount_bufferedWriter.write(NEW_LINE_SEPARATOR);

                // get the customer checking balance
                CheckingAccount ca = new CheckingAccount(i, i, CR.customer_checkingbalance, blocked, date);
                checkingAccount_bufferedWriter.write(ca.toString());
                checkingAccount_bufferedWriter.write(NEW_LINE_SEPARATOR);

            }
            cust_bufferedWriter.flush();
            cust_bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Generate the Company
        Long first_company = CR.customer_number + 1;
        int lower_company =  first_company.intValue();
        Long company_number = CR.company_number;
        int upper_company = company_number.intValue()+first_company.intValue();
        try {
            company_fileWriter = new FileWriter(DataPath+company_writePath, false);
            company_bufferedWriter = new BufferedWriter(company_fileWriter);
            for (int i = lower_company; i < upper_company; i++) {
                // generate the category
                String category = RG.getRandomCategory();
                // generate the name
                String name = category + String.valueOf(i) + category.hashCode();
                // generate the staff size
                int staff_size = RG.getRandomint(CR.company_size_lower, CR.company_size_upper);
                // generate the phone
                String phone = RG.getRandomPhone();
                // generate the province and citylist
                String province = RG.getRandomProvince();
                List<String> citylist = DS.Province_Cities_Map.get(province);
                // generate the city
                String city = RG.getRandomItem(citylist);
                // generate the date
                Date date = RG.getRandomTimestamp(CR.startDate,CR.midPointDate);
                // generate the blocked ids
                int blocked=0;
                if(Blocked_ids.contains(i))
                    blocked=1;
                // generate a new company
                Company company = new Company(i, name, category, staff_size, CR.company_loanbalance, phone, province, city, 0, 0, 0,0,blocked, date, date);
                company_bufferedWriter.write(company.toString());
                company_bufferedWriter.write(NEW_LINE_SEPARATOR);

                // get the company saving balance
                SavingAccount sa = new SavingAccount(i, i, CR.company_savingbalance, blocked, date, null);
                savingAccount_bufferedWriter.write(sa.toString());
                savingAccount_bufferedWriter.write(NEW_LINE_SEPARATOR);

                // get the company checking balance
                CheckingAccount ca = new CheckingAccount(i, i, CR.company_checkingbalance, blocked, date);
                checkingAccount_bufferedWriter.write(ca.toString());
                checkingAccount_bufferedWriter.write(NEW_LINE_SEPARATOR);
            }
            company_bufferedWriter.flush();
            company_bufferedWriter.close();
            savingAccount_bufferedWriter.flush();
            savingAccount_bufferedWriter.close();
            checkingAccount_bufferedWriter.flush();
            checkingAccount_bufferedWriter.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // generate the transfers
        Long transfer_cust_number = Math.round(CR.transfer_number * CR.cust_rate);
        Long transfer_number =CR.transfer_number;

        try {
            transfer_fileWriter = new FileWriter(DataPath+transfer_writePath, false);
            transfer_bufferedWriter = new BufferedWriter(transfer_fileWriter);
            for (long i = 1; i <= transfer_cust_number; i++) {
                // get the random source id and target id
                int src = 0;
                int tar = 0;
                while (src == tar) {
                    src = RG.getRandomint(1,customer_size);
                    tar = RG.getRandomint(1,customer_size+company_size);
                }

                // add the related ids into the blocked set
                if(Blocked_ids.contains(tar)){
                    blocked_transfer_bufferedWriter.write(String.valueOf(src));
                    blocked_transfer_bufferedWriter.write(NEW_LINE_SEPARATOR);
                }

                double amount=RG.getRandomDouble(CR.customer_savingbalance * 0.01);
//                if(rand<0.01){
//                    Related_Rejected_LoanApp_ids.add(i);
//                    amount= CR.customer_savingbalance;
//                }
//                else amount= RG.getRandomDouble(CR.customer_savingbalance * 0.01);

                Date date = RG.getRandomTimestamp(CR.midPoint, CR.endYear);
                // generate the new trans
                Transfer trans = new Transfer(i, src, tar, amount, RG.getRandomCustTransferType(), date,null);
                transfer_bufferedWriter.write(trans.toString());
                transfer_bufferedWriter.write(NEW_LINE_SEPARATOR);
            }

            long lowbound_company = transfer_cust_number + 1;
            long upperbound_company = transfer_number;
            for (long i = lowbound_company; i <= upperbound_company; i++) {
                // get the random source id and target id
                int src = 0;
                int tar = 0;
                while (src == tar) {
                    src = RG.getRandomint(customer_size+1,customer_size+company_size);
                    tar = RG.getRandomint(1,customer_size+company_size);
                }

                // add the related ids into the blocked set
                if(Blocked_ids.contains(tar)){
                    blocked_transfer_bufferedWriter.write(String.valueOf(src));
                    blocked_transfer_bufferedWriter.write(NEW_LINE_SEPARATOR);
                }

                double amount=RG.getRandomDouble(CR.customer_savingbalance * 0.01);
//                if(rand<0.01){
//                    Related_Rejected_LoanApp_ids.add(i);
//                    amount= CR.customer_savingbalance;
//                }
//                else amount= RG.getRandomDouble(CR.customer_savingbalance * 0.01);

                Date date = RG.getRandomTimestamp(CR.startDate,CR.midPointDate);
                // generate the new trans
                Transfer trans = new Transfer(i, src, tar, amount, RG.getRandomCompanyTransferType(), date,null);
                transfer_bufferedWriter.write(trans.toString());
                transfer_bufferedWriter.write(NEW_LINE_SEPARATOR);
            }
            transfer_bufferedWriter.flush();
            transfer_bufferedWriter.close();

            blocked_transfer_bufferedWriter.flush();
            blocked_transfer_bufferedWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // generate the checks
        int checks_cust_number =  Double.valueOf(CR.checking_number * (1-CR.cust_rate)).intValue();
        Long checks_number =CR.checking_number;
        try {
            checking_fileWriter = new FileWriter(DataPath+checking_writePath, false);
            checking_bufferedWriter = new BufferedWriter(checking_fileWriter);
            for (int i = 1; i <= checks_cust_number; i++) {
                // get the random source id and target id
                int src = 0;
                int tar = 0;
                while (src == tar) {
                    src = RG.getRandomint(1, customer_size);
                    tar = RG.getRandomint(1, customer_size + company_size);
                }

                // add the related ids into the blocked set
                if(Blocked_ids.contains(tar)){
                    blocked_checking_bufferedWriter.write(String.valueOf(src));
                    blocked_checking_bufferedWriter.write(NEW_LINE_SEPARATOR);
                }

                // generate the date based on the created date
                Date date = RG.getRandomTimestamp(CR.midPointDate, CR.endDate);
                // generate the new trans
                Checking check = new Checking(i, src, tar, RG.getRandomDouble(CR.customer_checkingbalance * 0.01), RG.getRandomCustCheckType(), date);
                checking_bufferedWriter.write(check.toString());
                checking_bufferedWriter.write(NEW_LINE_SEPARATOR);
            }

            int lb_checks = checks_cust_number + 1;
            Long ub = checks_number;
            int ub_checks = ub.intValue();
            for (int i = lb_checks; i <= ub_checks; i++) {
                // get the random source id and target id
                int src = 0;
                int tar = 0;
                while (src == tar) {
                    src = RG.getRandomint(customer_size+1,customer_size+company_size);
                    tar = RG.getRandomint(1,customer_size+company_size);
                }

                // add the related ids into the blocked set
                if(Blocked_ids.contains(tar)){
                    blocked_checking_bufferedWriter.write(String.valueOf(src));
                    blocked_checking_bufferedWriter.write(NEW_LINE_SEPARATOR);
                }

                Date date = RG.getRandomTimestamp(CR.midPointDate, CR.endDate);
                // generate the new trans
                Checking check = new Checking(i, src, tar, RG.getRandomDouble(CR.company_checkingbalance * 0.01), RG.getRandomCompanyCheckType(), date);
                checking_bufferedWriter.write(check.toString());
                checking_bufferedWriter.write(NEW_LINE_SEPARATOR);
            }
            checking_bufferedWriter.flush();
            checking_bufferedWriter.close();

            blocked_checking_bufferedWriter.flush();
            blocked_checking_bufferedWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // generate the loan applications
        Long loanapps_number = CR.loanapps_number;
        int loansapps_cust_number =  Double.valueOf(loanapps_number * (1-CR.cust_rate)).intValue();
        int loansapps_company_number = loanapps_number.intValue() - loansapps_cust_number;
        int loansapps_company_number_lb = loansapps_cust_number+1;
        int loansapps_company_number_ub = loansapps_company_number + loansapps_company_number_lb;
        Date loanDate = CR.loanDate;
        Date startDate= CR.startDate;
        Date endDate = CR.endDate;
        try {
            loanapps_fileWriter = new FileWriter(DataPath+loanapps_writePath, false);
            loanapps_bufferedWriter = new BufferedWriter(loanapps_fileWriter);
            loantrans_fileWriter = new FileWriter(DataPath+loantrans_writePath, false);
            loantrans_bufferedWriter = new BufferedWriter(loantrans_fileWriter);
            // generate the customer loan applications and transactions
            for (int i = 1; i <= loansapps_cust_number; i++) {
                Date contractTimestamp=null;
                int custID = RG.getRandomint(1, lower_company - 1);
                double amount = RG.getRandomDouble(CR.customer_loanbalance * CR.loan_rate);
                Date loan_date = RG.getRandomTimestamp(loanDate, endDate);
                //System.out.println("The application date is "+DateUtility.convertDateToString(loan_date));
                int duration = RG.getRandomLoanDuration();
                String status = RG.getRandomLoanStatus();
                LoanApps loanapp = new LoanApps(i, custID, amount, duration, status, loan_date);
                loanapps_bufferedWriter.write(loanapp.toString());
                loanapps_bufferedWriter.write(NEW_LINE_SEPARATOR);

                Date OneDayAfter = DateUtility.OneDayAfter(loan_date);
                if(status=="accept")
                    contractTimestamp=OneDayAfter;
                LoanTrans loantrans = new LoanTrans(i, custID, i, amount, status, OneDayAfter, duration, contractTimestamp,0);
                loantrans_bufferedWriter.write(loantrans.toString());
                loantrans_bufferedWriter.write(NEW_LINE_SEPARATOR);
            }

            // generate the company loan applications and transactions
            for (int i=loansapps_company_number_lb;i< loansapps_company_number_ub;i++) {
                Date contractTimestamp=null;
                Long cid = RG.getRandomLong(1+CR.customer_number,CR.customer_number+CR.company_number);
                int companyId= cid.intValue();
                double amount = RG.getRandomDouble(CR.company_loanbalance * CR.loan_rate);
                Date loan_date = RG.getRandomTimestamp(loanDate, endDate);
                int duration = RG.getRandomLoanDuration();
                String status = RG.getRandomLoanStatus();
                LoanApps loanapp = new LoanApps(i, companyId, amount, duration, status, loan_date);
                loanapps_bufferedWriter.write(loanapp.toString());
                loanapps_bufferedWriter.write(NEW_LINE_SEPARATOR);
                Date OneDayAfter = DateUtility.OneDayAfter(loan_date);
                if(status=="accept")
                    contractTimestamp=OneDayAfter;
                LoanTrans loantrans = new LoanTrans(i, companyId, i, amount, status, OneDayAfter, duration, contractTimestamp,0);
                loantrans_bufferedWriter.write(loantrans.toString());
                loantrans_bufferedWriter.write(NEW_LINE_SEPARATOR);
            }

            loanapps_bufferedWriter.flush();
            loanapps_bufferedWriter.close();
            loantrans_bufferedWriter.flush();
            loantrans_bufferedWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // parameter curation
        try {
            Integer contention_num = Integer.parseInt(ConfigLoader.prop.getProperty("contention_num","100"));
            Integer[] list1 = new Integer[contention_num];
            Related_Blocked_Transfer_ids = Arrays.asList(reservoir_sampling(DataPath + "blocked_transfer.csv", list1));
            Integer[] list2 = new Integer[contention_num];
            Related_Blocked_Checking_ids = Arrays.asList(reservoir_sampling(DataPath + "blocked_checking.csv", list2));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // write the anomalies
        try {

            FileOutputStream f = new FileOutputStream(new File(DataPath+"Related_transfer_bids"));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(Related_Blocked_Transfer_ids);

            f = new FileOutputStream(new File(DataPath+"Related_checking_bids"));
            o = new ObjectOutputStream(f);
            o.writeObject(Related_Blocked_Checking_ids);

            o.close();
            f.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
//        // write the anomalies
//        try {
//            FileWriter f = new FileWriter(new File(DataPath+"Blocked_ids"));
//            BufferedWriter fw = new BufferedWriter(f);
//            for(int id : Blocked_ids){
//                fw.write(String.valueOf(id));
//                fw.write(NEW_LINE_SEPARATOR);
//            }
//         //   ObjectOutputStream o = new ObjectOutputStream(f);
//       //     o.writeObject(Blocked_ids);
//            fw.flush();
//            fw.close();
//
//            f = new FileWriter(new File(DataPath+"Related_transfer_bids"));
//            fw = new BufferedWriter(f);
//            for(int id : Related_Blocked_Transfer_ids){
//                fw.write(String.valueOf(id));
//                fw.write(NEW_LINE_SEPARATOR);
//            }
//        //    o = new ObjectOutputStream(f);
//        //    o.writeObject(Related_Blocked_Transfer_ids);
//            fw.flush();
//            fw.close();
//
//            f = new FileWriter(new File(DataPath+"Related_checking_bids"));
//            fw = new BufferedWriter(f);
//            for(int id : Related_Blocked_Checking_ids){
//                fw.write(String.valueOf(id));
//                fw.write(NEW_LINE_SEPARATOR);
//            }
//         //   o = new ObjectOutputStream(f);
//        //    o.writeObject(Related_Blocked_Checking_ids);
//            fw.flush();
//            fw.close();
//            f.close();
//
//        } catch (FileNotFoundException e) {
//            System.out.println("File not found");
//        } catch (IOException e) {
//            System.out.println("Error initializing stream");
//        }

        long millisEnd = System.currentTimeMillis();
        System.out.println("Data is ready under the Data folder!");
        System.out.println("----------------");
        System.out.println("----------------");
        System.out.println("----------------");
        System.out.println("Data generation took "+(millisEnd - millisStart) + " ms");
    }
}
