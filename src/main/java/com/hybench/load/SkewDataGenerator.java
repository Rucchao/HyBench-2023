import Schema.*;
import Utilities.*;
import Utilities.Writer;
import com.moandjiezana.toml.Toml;
import umontreal.iro.lecuyer.probdist.PowerDist;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

/**
 * Created by chao zhang on 2023.7.19.
 */

public class SkewDataGenerator{

    public static void main(String[] args) throws ParseException {
        System.out.println("This is a skewed data generator of HyBench, Version 0.1");
        System.out.println("----------------");
        System.out.println("----------------");
        System.out.println("----------------");
        System.out.println("Data is generating...");
        System.out.println("----------------");
        System.out.println("----------------");
        System.out.println("----------------");
        String sf = "1x";
        double alpha = 0.4;
        double lambda = 0.2;
        int province_number = 34;
        int category_number =96;
        int city_number = 355;
        int total_year = 9;
        int window_size=20;
        DataSource DS = new DataSource();
        RandomGenerator RG = new RandomGenerator();
        Sorting Sort= new Sorting();
        // input the scale factor, e.g. 1x or 10x
        ConfigReader CR = new ConfigReader(sf);
        ArrayList<Record> cust_list = new ArrayList<Record>();
        ArrayList<Record> company_list = new ArrayList<Record>();
        ArrayList<Record> transfer_list = new ArrayList<Record>();
        ArrayList<Record> savingAccount_list = new ArrayList<Record>();
        ArrayList<Record> checkingAccount_list = new ArrayList<Record>();
        ArrayList<Record> checking_list = new ArrayList<Record>();
        ArrayList<Record> loanApps_list = new ArrayList<Record>();
        ArrayList<Record> loanTrans_list = new ArrayList<Record>();
        // initialize the number
        Long CompanyNumber_long = CR.company_number;
        Long CustomerNumber_long = CR.customer_number;
        int CompanyNumber= CompanyNumber_long.intValue();
        int CustomerNumber=CustomerNumber_long.intValue();
        Long transfer_number=CR.transfer_number;
        Long transfer_cust_number_long = Math.round(CR.transfer_number * CR.cust_rate);
        int transfer_cust_number =  transfer_cust_number_long.intValue();
        int transfer_company_number = transfer_number.intValue()-transfer_cust_number;
        Long check_number=CR.checking_number;
        Long check_cust_number_long = Math.round(CR.checking_number * (1-CR.cust_rate));
        int check_cust_number =  check_cust_number_long.intValue();
        int check_company_number = check_number.intValue()-check_cust_number;

        ExponentialCDF exponen = new ExponentialCDF(lambda);
        double lambda_transfer = CustomerNumber* 1.0/transfer_cust_number;
        double lambda_check = CompanyNumber* 1.0/check_company_number;
        //System.out.println(lambda_transfer);
        //System.out.println(lambda_check);
        ExponentialCDF exponen_transfer = new ExponentialCDF(lambda_transfer);
        ExponentialCDF exponen_check = new ExponentialCDF(lambda_check);
        PowerCDF power_province = new PowerCDF(0,province_number,alpha);
        PowerCDF power_city = new PowerCDF(0,city_number,alpha);
        PowerCDF power_company = new PowerCDF(0,CompanyNumber,alpha);
        PowerCDF power_year = new PowerCDF(1,9,alpha);
        PowerCDF power_transfer = new PowerCDF(0,window_size,alpha);
        PowerCDF power_check = new PowerCDF(0,window_size,alpha);

        int id_counter=0;
        // generate the Customers with accounts
        for (int i=1;i<=CustomerNumber;i++) {
            // generate the gender
            String gender = RG.getRandomString(DS.gender);
            StringBuilder Namebuilder = new StringBuilder();
            Namebuilder.append(RG.getRandomItem(DS.LastName_list)).append(" ");
            // generate first name with gender
            if(gender.equals("female"))
                Namebuilder.append(RG.getRandomItem(DS.FirstName_female_list));
            else Namebuilder.append(RG.getRandomItem(DS.FirstName_male_list));

            // generate the province with power distribution
            String province = RG.getPowerProvince(power_province);
            List<String> citylist = DS.Province_Cities_Map.get(province);
            // generate the city with exponential distribution
            int cityID = RG.getExponentialIndex(citylist.size(), exponen);
            String city = citylist.get(cityID);
            // generate the companyID with power distribution
            int companyId= RG.getPowerIndex(CompanyNumber, power_company);
            //long companyId = RG.getRandomLong(1+CR.customer_number,CR.customer_number+CR.company_number);
            // generate the age
            int age = RG.getRandomint(CR.customer_age_lower,CR.customer_age_upper);
            // generate the phone
            String phone= RG.getRandomPhone();

            // generate the year with power distribution
            int year = RG.getPowerIndex(total_year, power_year);
            Date date = RG.getRandomDate(CR.startYear, CR.startYear + year);
            id_counter+=1;

            // generate a new customer
            Customer cust = new Customer(i, CustomerNumber+companyId, gender, Namebuilder.toString(),age,phone,province,city,cityID,CR.customer_loanbalance,0,0,0,0,date, date);
            cust_list.add(cust);

            // get the customer saving balance
            SavingAccount sa = new SavingAccount(i, i, CR.customer_savingbalance, 0,date);
            savingAccount_list.add(sa);

            // get the customer checking balance
            CheckingAccount ca = new CheckingAccount(i, i, CR.customer_checkingbalance, 0,date);
            checkingAccount_list.add(ca);
        }

        // Generate the Company with accounts
        for (int i=0;i< CompanyNumber;i++) {
            // generate the category
            //String category = RG.getPowerCategory(power_companyCategory);
            String  category = RG.getRandomCategory();

            // generate the name
            String name = category + String.valueOf(i)+ category.hashCode();
            // generate the staff size
            int staff_size = RG.getRandomint(CR.company_size_lower,CR.company_size_upper);
            // generate the phone
            String phone= RG.getRandomPhone();

            // generate the province with exponential distribution
            String province = RG.getExponentialProvince(exponen);
            List<String> citylist = DS.Province_Cities_Map.get(province);

            // generate the city with exponential distribution
            int idx = RG.getExponentialIndex(citylist.size(), exponen);
            String city = citylist.get(idx);

            // generate the date with power distribution
            int year = RG.getPowerIndex(total_year, power_year);
            Date date = RG.getRandomDate(CR.startYear,CR.startYear+year);

            id_counter+=1;
            // generate a new company
            Company company = new Company(id_counter, name, category, staff_size, CR.company_loanbalance, phone,province,city,idx,0,0, date, date);
            company_list.add(company);

            // get the company saving balance
            SavingAccount sa = new SavingAccount(id_counter, id_counter, CR.company_savingbalance, 0,date);
            savingAccount_list.add(sa);

            // get the customer checking balance
            CheckingAccount ca = new CheckingAccount(id_counter, id_counter, CR.company_savingbalance, 0,date);
            checkingAccount_list.add(ca);
        }

        // Generate the transfers for the customers with power distribution
        id_counter=0;
        for (int i = 0; i < cust_list.size(); i++) {
            if (id_counter < transfer_cust_number) {
                // Step1: generate the number of transfer for each customer with an exponential distribution
                int transfer_number_per_cust=exponen_transfer.getValue(RG.rand);
                List<Customer> sorted_custlist=null;
                List<Company> sorted_companylist=null;
                Customer c1 = (Customer)cust_list.get(i);

                // Step2: generate the random sublists for an active customer
                if (transfer_number_per_cust>0 && transfer_number_per_cust/2>1){
                    List<Customer> custs = RG.getRandomlist(cust_list, window_size, i);
                    List<Company> companies = RG.getRandomlist(company_list, window_size, i);
                    // System.out.println(((Customer)cust_list.get(i)).province());
                    // System.out.println(((Customer)cust_list.get(i)).city());
                    sorted_custlist=Sort.sortCustomerOnLocation(custs, c1.province(), c1.city());

                    // Step3: generate the transfers to the customer based on the attribute correlation
                    for (int j = 1; j <= transfer_number_per_cust/2; j++) {
                        int idx = RG.getPowerIndex(sorted_custlist.size(),power_transfer);
                        Customer c2 = sorted_custlist.get(idx);
                        // generate the new transfer
                        Date d1 = c1.createdDate();
                        Date d2 = c2.createdDate();
                        Date date = RG.getRandomTimestamp(d1,d2);
                        id_counter += 1;
                        Transfer trans = new Transfer(id_counter, c1.custID(), c2.custID(), RG.getRandomDouble(CR.customer_savingbalance *0.01),RG.getRandomCustTransferType(),date);
                        transfer_list.add(trans);
                    }

                    // Step4: generate the transfers based on the attribute correlation
                    sorted_companylist=Sort.sortCompaniesOnLocation(companies, c1.province(), c1.city());
                    for (int j = 1; j <= transfer_number_per_cust/2; j++) {
                        int idx = RG.getPowerIndex(sorted_companylist.size(),power_transfer);
                        Company c2 = sorted_companylist.get(idx);
                        // generate the new transer
                        Date d1 = c1.createdDate();
                        Date d2 = c2.createdDate();
                        Date date = RG.getRandomTimestamp(d1,d2);
                        id_counter += 1;
                        Transfer trans = new Transfer(id_counter, c1.custID(), c2.companyID(), RG.getRandomDouble(CR.customer_savingbalance *0.02),RG.getRandomCustTransferType(),date);
                        transfer_list.add(trans);
                    }
                }
            }
            else {
                // truncate the outbound items
                transfer_list.subList(transfer_cust_number,transfer_list.size()).clear();
                id_counter=transfer_cust_number;
            }
        }

        // Randomly generate the remaining customer transfers
        int powersize=transfer_list.size();
        if(powersize<transfer_cust_number){
            for (int i=1;i<= (transfer_cust_number-powersize);i++) {
                int src = 0;
                int tar = 0;
                while (src == tar) {
                    src = RG.getRandomint(cust_list.size());
                    tar = RG.getRandomint(cust_list.size());
                }
                // generate the date based on the created date
                Record c1 = cust_list.get(src);
                Record c2 = cust_list.get(tar);
                Date d1 = ((Customer) c1).createdDate();
                Date d2 = ((Customer) c2).createdDate();
                Date date = RG.getRandomTimestamp(d1, d2);
                // generate the new trans
                id_counter += 1;
                Transfer trans = new Transfer(id_counter, src, tar, RG.getRandomDouble(CR.customer_savingbalance * 0.01), RG.getRandomCustTransferType(), date);
                transfer_list.add(trans);
            }
        }

        // Generate the transfers for the companies with power distribution
        int new_id_counter=0;
        for (int i = 0; i < company_list.size(); i++) {
            if (new_id_counter < transfer_company_number) {
                // Step1: generate the number of transfer for each company with an exponential distribution
                int transfer_number_per_company=exponen_check.getValue(RG.rand);
                List<Customer> sorted_custlist=null;
                List<Company> sorted_companylist=null;
                Company c1 = (Company)company_list.get(i);

                // Step2: generate the random sublists for an active customer
                if (transfer_number_per_company>0){
                    List<Customer> custs = RG.getRandomlist(cust_list, window_size, i);
                    List<Company> companies = RG.getRandomlist(company_list, window_size, i);
                    sorted_custlist=Sort.sortCustomerOnLocation(custs, c1.province(),c1.city());
                    // Step3: generate the transfers based on the attribute correlation
                    for (int j = 1; j <= transfer_number_per_company/2; j++) {
                        int idx = RG.getPowerIndex(sorted_custlist.size(),power_transfer);
                        Customer c2 = sorted_custlist.get(idx);
                        // generate the new transfer
                        Date d1 = c1.createdDate();
                        Date d2 = c2.createdDate();
                        Date date = RG.getRandomTimestamp(d1,d2);
                        id_counter += 1;
                        new_id_counter+= 1;
                        Transfer trans = new Transfer(id_counter, c1.companyID(), c2.custID(), RG.getRandomDouble(CR.company_savingbalance *0.01),RG.getRandomCompanyTransferType(),date);
                        transfer_list.add(trans);
                    }

                    // Step4: generate the transfers based on the attribute correlation
                    sorted_companylist=Sort.sortCompaniesOnLocation(companies, c1.province(),c1.city());
                    for (int j = 1; j <= transfer_number_per_company/2; j++) {
                        int idx = RG.getPowerIndex(sorted_companylist.size(),power_transfer);
                        Company c2 = sorted_companylist.get(idx);
                        // generate the new transer
                        Date d1 = c1.createdDate();
                        Date d2 = c2.createdDate();
                        Date date = RG.getRandomTimestamp(d1,d2);
                        id_counter += 1;
                        new_id_counter+= 1;
                        Transfer trans = new Transfer(id_counter, c1.companyID(), c2.companyID(), RG.getRandomDouble(CR.company_savingbalance *0.02),RG.getRandomCompanyCheckType(),date);
                        transfer_list.add(trans);
                    }
                }
            }
            else {
                // truncate the outbound items
                transfer_list.subList(transfer_cust_number+transfer_company_number,transfer_list.size()).clear();
                id_counter=transfer_company_number;
            }
        }

        // Randomly generate the remaining company transfers
        powersize =new_id_counter;
        if(new_id_counter<transfer_company_number){
            for (int i=1;i<= (transfer_company_number-powersize);i++) {
                int src = 0;
                int tar = 0;
                while (src == tar) {
                    src = RG.getRandomint(company_list.size());
                    tar = RG.getRandomint(company_list.size());
                }
                // generate the date based on the created date
                Record c1 = company_list.get(src);
                Record c2 = company_list.get(tar);

                Date d1 = ((Company) c1).createdDate();
                Date d2 = ((Company) c2).createdDate();
                Date date = RG.getRandomTimestamp(d1, d2);
                // generate the new trans
                id_counter += 1;
                new_id_counter += 1;
                Transfer trans = new Transfer(id_counter, src, tar, RG.getRandomDouble(CR.company_savingbalance * 0.02), RG.getRandomCustTransferType(), date);
                transfer_list.add(trans);
            }
        }

        // Generate the checks for the customers with power distribution
        id_counter=0;
        for (int i = 0; i < cust_list.size(); i++) {
            if (id_counter < check_cust_number) {
                // Step1: generate the number of checks for each customer with an exponential distribution
                int check_number_per_cust=exponen_check.getValue(RG.rand);
                List<Customer> sorted_custlist=null;
                List<Company> sorted_companylist=null;
                Customer c1 = (Customer)cust_list.get(i);

                // Step2: generate the random sublists for an active customer
                if (check_number_per_cust>0 && check_number_per_cust/2>1){
                    List<Customer> custs = RG.getRandomlist(cust_list, window_size, i);
                    List<Company> companies = RG.getRandomlist(company_list, window_size, i);
                    sorted_custlist=Sort.sortCustomerOnLocation(custs, c1.province(), c1.city());

                    // Step3: generate the checks to the customer based on the attribute correlation
                    for (int j = 1; j <= check_number_per_cust/2; j++) {
                        int idx = RG.getPowerIndex(sorted_custlist.size(),power_transfer);
                        Customer c2 = sorted_custlist.get(idx);
                        // generate the new check
                        Date d1 = c1.createdDate();
                        Date d2 = c2.createdDate();
                        Date date = RG.getRandomTimestamp(d1,d2);
                        id_counter += 1;
                        Checking check = new Checking(id_counter, c1.custID(), c2.custID(), RG.getRandomDouble(CR.customer_checkingbalance* 0.01),RG.getRandomCustCheckType(),date);
                        checking_list.add(check);
                    }

                    // Step4: generate the checks based on the attribute correlation
                    sorted_companylist=Sort.sortCompaniesOnLocation(companies, c1.province(), c1.city());
                    for (int j = 1; j <= check_number_per_cust/2; j++) {
                        int idx = RG.getPowerIndex(sorted_companylist.size(),power_transfer);
                        Company c2 = sorted_companylist.get(idx);
                        // generate the new check
                        Date d1 = c1.createdDate();
                        Date d2 = c2.createdDate();
                        Date date = RG.getRandomTimestamp(d1,d2);
                        id_counter += 1;
                        Checking check = new Checking(id_counter, c1.custID(), c2.companyID(), RG.getRandomDouble(CR.customer_checkingbalance* 0.01),RG.getRandomCustCheckType(),date);
                        checking_list.add(check);
                    }
                }
            }
            else {
                // truncate the outbound items
                checking_list.subList(check_cust_number,checking_list.size()).clear();
                id_counter=check_cust_number;
            }
        }

        // Randomly generate the remaining customer checks
        powersize=checking_list.size();
        if(powersize<check_cust_number){
            for (int i=1;i<= (check_cust_number-powersize);i++) {
                int src = 0;
                int tar = 0;
                while (src == tar) {
                    src = RG.getRandomint(cust_list.size());
                    tar = RG.getRandomint(cust_list.size());
                }
                // generate the date based on the created date
                Record c1 = cust_list.get(src);
                Record c2 = cust_list.get(tar);
                Date d1 = ((Customer) c1).createdDate();
                Date d2 = ((Customer) c2).createdDate();
                Date date = RG.getRandomTimestamp(d1, d2);
                // generate the new trans
                id_counter += 1;
                Checking check = new Checking(id_counter, src, tar, RG.getRandomDouble(CR.customer_checkingbalance* 0.01),RG.getRandomCustCheckType(),date);
                checking_list.add(check);
            }
        }

        // Generate the checks for the company with power distribution
        new_id_counter=0;
        for (int i = 0; i < company_list.size(); i++) {
            if (new_id_counter < check_company_number) {
                // Step1: generate the number of transfer for each company with an exponential distribution
                int check_number_per_company=exponen_check.getValue(RG.rand);
                List<Customer> sorted_custlist=null;
                List<Company> sorted_companylist=null;
                Company c1 = (Company)company_list.get(i);

                // Step2: generate the random sublists for an active company
                if (check_number_per_company>0 && check_number_per_company/2>1){
                    List<Customer> custs = RG.getRandomlist(cust_list, window_size, i);
                    List<Company> companies = RG.getRandomlist(company_list, window_size, i);
                    sorted_custlist=Sort.sortCustomerOnLocation(custs, c1.province(),c1.city());
                    // Step3: generate the transfers based on the attribute correlation
                    for (int j = 1; j <= check_number_per_company/2; j++) {
                        int idx = RG.getPowerIndex(sorted_custlist.size(),power_transfer);
                        Customer c2 = sorted_custlist.get(idx);
                        // generate the new transfer
                        Date d1 = c1.createdDate();
                        Date d2 = c2.createdDate();
                        Date date = RG.getRandomTimestamp(d1,d2);
                        id_counter += 1;
                        new_id_counter+= 1;
                        Checking check = new Checking(id_counter, c1.companyID(), c2.custID(), RG.getRandomDouble(CR.company_checkingbalance* 0.01),RG.getRandomCompanyCheckType(),date);
                        checking_list.add(check);
                    }

                    // Step4: generate the transfers based on the attribute correlation
                    sorted_companylist=Sort.sortCompaniesOnLocation(companies, c1.province(),c1.city());
                    for (int j = 1; j <= check_number_per_company/2; j++) {
                        int idx = RG.getPowerIndex(sorted_companylist.size(),power_transfer);
                        Company c2 = sorted_companylist.get(idx);
                        // generate the new transer
                        Date d1 = c1.createdDate();
                        Date d2 = c2.createdDate();
                        Date date = RG.getRandomTimestamp(d1,d2);
                        id_counter += 1;
                        new_id_counter+= 1;
                        Checking check = new Checking(id_counter, c1.companyID(), c2.companyID(), RG.getRandomDouble(CR.company_checkingbalance* 0.01),RG.getRandomCompanyCheckType(),date);
                        checking_list.add(check);
                    }
                }
            }
            else {
                // truncate the outbound items
                checking_list.subList(check_cust_number+check_company_number,checking_list.size()).clear();
            }
        }

        // Randomly generate the remaining company checks
        powersize =new_id_counter;
        if(new_id_counter<check_company_number){
            for (int i=1;i<= (check_company_number-powersize);i++) {
                int src = 0;
                int tar = 0;
                while (src == tar) {
                    src = RG.getRandomint(company_list.size());
                    tar = RG.getRandomint(company_list.size());
                }
                // generate the date based on the created date
                Record c1 = company_list.get(src);
                Record c2 = company_list.get(tar);

                Date d1 = ((Company) c1).createdDate();
                Date d2 = ((Company) c2).createdDate();
                Date date = RG.getRandomTimestamp(d1, d2);
                // generate the new trans
                id_counter += 1;
                new_id_counter += 1;
                Checking check = new Checking(id_counter, src, tar, RG.getRandomDouble(CR.company_checkingbalance* 0.01),RG.getRandomCompanyCheckType(),date);
                checking_list.add(check);
            }
        }

        // generate the loan applications
        Long loanapps_number = CR.loanapps_number;
        Long loansapps_cust_number =  Math.round(loanapps_number * (1-CR.cust_rate));
        int loansapps_company_number = loanapps_number.intValue() - loansapps_cust_number.intValue();
        id_counter=0;

        // generate the customer loan applications and transactions with power distribution
        List<Record> SortedCustlistOnCity=Sort.sortCustomerOnCity(cust_list);
        List<Record> SortedCompanylistOnCity=Sort.sortCompaniesOnCity(company_list);

        for (int i=1;i<=loansapps_cust_number.intValue();i++) {
            int custID=RG.getPowerIndex(SortedCustlistOnCity.size(), power_city);
            double amount = RG.getRandomDouble(CR.customer_loanbalance * CR.loan_rate);
            Record cust = SortedCustlistOnCity.get(custID);
            Date min_date = ((Customer)cust).createdDate();
            Date loan_date = RG.getRandomTimestamp(min_date);
            id_counter +=1;
            LoanApps loanapp = new LoanApps(id_counter, custID, amount, loan_date);
            loanApps_list.add(loanapp);

            String status = RG.getRandomLoanStatus();
            Date OneDayAfter = DateUtility.OneDayAfter(loan_date);
            LoanTrans loantran = new LoanTrans(id_counter, custID, i, amount, status, OneDayAfter);
            loanTrans_list.add(loantran);
        }

        // generate the company loan applications and transactions
        for (new_id_counter=0; new_id_counter< loansapps_company_number;new_id_counter++) {
            int companyID = RG.getPowerIndex(SortedCompanylistOnCity.size(), power_city);
            double amount = RG.getRandomDouble(CR.company_loanbalance * CR.loan_rate);
            Record company = SortedCompanylistOnCity.get(companyID);
            Date min_date = ((Company)company).createdDate();
            Date loan_date = RG.getRandomTimestamp(min_date);
            int cID = ((Company)company).companyID();
            id_counter +=1;
            LoanApps loanapp = new LoanApps(id_counter,cID, amount, loan_date);
            loanApps_list.add(loanapp);

            String status = RG.getRandomLoanStatus();
            Date OneDayAfter = DateUtility.OneDayAfter(loan_date);
            LoanTrans loantran = new LoanTrans(id_counter,cID,id_counter, amount, status, OneDayAfter);
            loanTrans_list.add(loantran);
        }

        // Sort cust_list and company_list
        Sort.sortCustomerOnid(cust_list);
        Sort.sortCompaniesOnid(company_list);

        // write to disk
        Writer csvwriter = new Writer();
        String DataPath="SkewData_"+sf+"/";
        csvwriter.write2csv(cust_list,CR.customer_datapath,DataPath);
        csvwriter.write2csv(company_list,CR.company_datapath,DataPath);
        csvwriter.write2csv(savingAccount_list,CR.savingaccount_datapath,DataPath);
        csvwriter.write2csv(transfer_list,CR.transfer_datapath,DataPath);
        csvwriter.write2csv(checkingAccount_list,CR.checkingaccount_datapath,DataPath);
        csvwriter.write2csv(checking_list,CR.checking_datapath,DataPath);
        csvwriter.write2csv(loanApps_list,CR.loanapps_datapath, DataPath);
        csvwriter.write2csv(loanTrans_list,CR.loantrans_datapath, DataPath);
    }
}
