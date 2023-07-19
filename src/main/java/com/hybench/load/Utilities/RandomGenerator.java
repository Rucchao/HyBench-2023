package Utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Created by chao zhang on 2023.7.19.
 */

public class RandomGenerator {
    public static Random rand = new Random();

    String[] prefix_3 = {
            "134", "135", "136", "137", "138", "139", "150", "151", "152", "157", "158", "159",
            "182", "183", "184", "187", "188", "178", "147", "172", "198","130", "131", "132",
            "145", "155", "156", "166", "171", "175", "176", "185", "186", "166", "133", "149",
            "153", "173", "177", "180", "181", "189", "199"
    };

    String[] CompanyCategories= {"agriculture", "forestry", "animal_husbandry","fishery","agriculture_auxiliary",
            "coal_mining","oil_gas_mining","black_medal_mining","nonferrous_metal_mining","nonmetal_mining",
            "mining_auxiliary","other_mining","agriculture_food_processing","food_manufacturing","alcohol_drink_tea",
            "tobacco_manufacturing","textile_industry","textile_garment","leather_clothing","wood_bamboo_processing",
            "furniture_manufacturing","paper_industry","printing_business","office_art_sports_entertainment_supplies","oil_coal_processing",
            "chemistry_manufacturing","medical_care_medicines","chemical_fiber_manufacturing","rubber_plastic","nonmetal_manufacturing",
            "black_medal_manufacturing","nonferrous_metal_manufacturing","medal_manufacturing","general_equipment_manufacturing","special_equipment_manufacturing",
            "automotive_manufacturing","railway_ship_aviation_manufacturing","electrical_machinery_manufacturing","computer_communication_manufacturing",
            "instrument_manufacturing", "other_manufacturing","abandoned_resource_utilization","medal_machinery_equipment_repair","electricity_manufacturing_supply",
            "gas_manufacturing_supply", "water_manufacturing_supply","house_construction","civil_engineering","building_installation","building_decoration",
            "wholesale","retail","railway_transportation","road_transportation","water_transportation", "pipeline_transportation",
            "multi_transportation_agent","loading_warehousing","postal_service","accommodation", "catering","telecommunication","internet_service",
            "software_IT","money_finance","capital_market","insurance","other_finance","real_estate","leasing_service","business_service","research",
            "technology_service","promotion_service","water_management","eco_environmental_management","public_facility_management","land_management",
            "resident_service","vehicle_electronic_product_repair","other_service","education","health","social_work",
            "news_publishing","broadcasting_recoding_filming","culture_art","sports","entertainment","public_administration",
            "national_institution","consultative_institution","social_protection","social_group","the_masses","international_institution"};

    String[] Provinces_municipalities= {"Guangdong","Jiangsu","Shandong","Zhejiang","Henan","Sichuan","Hubei","Fujian","Hunan","Shanghai","Anhui","Hebei","Beijing",
                        "Shaanxi","Jiangxi","Chongqing","Liaoning","Yunnan","Guangxi","Hong_Kong","Shanxi","Inner_Mongolia","Guizhou","Xinjiang","Tianjin","Heilongjiang",
                        "Jilin","Gansu","Hainan","Ningxia","Qinghai","Tibet","Macau","Taiwan"};

    public enum transfer_cust_type {transfer, red_packet, donate, personal_invest}
    public enum transfer_company_type {business, salary, service, company_invest}

    public enum check_cust_type {check, others}
    public enum check_company_type {business, service, invest}

    public enum loan_type {company_business, personal_business}
    int[] loan_contract_type= {30, 60, 90, 180, 365};
    public enum loan_status {under_review, reject, accept}

    public String getRandomLoanStatus (){
        return loan_status.values()[rand.nextInt(loan_status.values().length)].toString();
    }

    public String getRandomCustTransferType (){
        return transfer_cust_type.values()[rand.nextInt(transfer_cust_type.values().length)].toString();
    }

    public String getRandomCompanyTransferType (){
        return transfer_company_type.values()[rand.nextInt(transfer_company_type.values().length)].toString();
    }

    public String getRandomCustCheckType (){
        return transfer_cust_type.values()[rand.nextInt(check_cust_type.values().length)].toString();
    }

    public String getRandomCompanyCheckType (){
        return transfer_company_type.values()[rand.nextInt(check_company_type.values().length)].toString();
    }

    public String getRandomItem(List<String> lst) {
        return lst.get(rand.nextInt(lst.size()));
    }

    public int[] getRandomEdge(int range1, int range2) {
            int[] edge= {0,1};
            int src =rand.nextInt(range1);
            int tar =rand.nextInt(range2);
            while (src!=tar){
                edge[0]=src;
                edge[1]=tar;
                return edge;
        }
        return null;
    }

    public String getRandomItemFromset(Set<String> set) {
        Iterator<String> iter = set.iterator();
        for (int i = 0; i < rand.nextInt(set.size()); i++) {
            iter.next();
        }
        return iter.next();
    }

    public String getRandomProvince() {
        return getRandomString(Provinces_municipalities);
    }

    public String getRandomString(String[] slst) {
        return slst[rand.nextInt(slst.length)];
    }

    public int getRandomint(int bound) {
        return rand.nextInt(bound);
    }

    public List getRandomlist(List lst, int size, int cur_idx) {
        List sublist=new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int idx=rand.nextInt(lst.size());
            if (cur_idx!=idx)
            sublist.add(lst.get(idx));
        }
        return sublist;
    }


    public int getRandomint(int lower, int upper) {
        return rand.nextInt(lower,upper);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double getRandomDouble(Double bound) {
        Double num = rand.nextDouble(bound);
        return round(num, 2);
    }

    public long getRandomLong(long lower, long upper) {
        return rand.nextLong(lower,upper);
    }

    public long getRandomLong(long upper) {
        return rand.nextLong(upper);
    }

    public String getRandomPhone() {
        StringBuilder builder = new StringBuilder();
        builder.append(prefix_3[rand.nextInt(prefix_3.length)]);
        //last_8 digit
        for (int i = 0; i < 8; i++) {
            int d = rand.nextInt(10);
            builder.append(d);
        }
        return builder.toString();
    }

    public String getRandomCategory() {
        return CompanyCategories[rand.nextInt(CompanyCategories.length)];
    }

    public Date getRandomDate(int startYear, int endYear) {
        long aDay = TimeUnit.DAYS.toMillis(1);
        long now = new Date().getTime();
        Date start = new Date(now - aDay * 365 * (endYear-startYear+1));
        Date end = new Date(now);

        long startMillis = start.getTime();
        long endMillis = end.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);

        return new Date(randomMillisSinceEpoch);
    }

    public String getExponentialProvince(ExponentialCDF exponen) {
        int i=getExponentialIndex(Provinces_municipalities.length,exponen);
        return Provinces_municipalities[i];
    }

    public String getPowerProvince(PowerCDF power_province) {
        int i=getPowerIndex(Provinces_municipalities.length,power_province);
        return Provinces_municipalities[i];
    }

    public String getPowerCategory(PowerCDF power_category) {
        int i=getPowerIndex(CompanyCategories.length,power_category);
        return CompanyCategories[i];
    }

    public int getPowerIndex(int size, PowerCDF power) {
        int idx= binary_search(rand, power, size);
        return idx;
    }

    public Date getRandomTimestamp(int startYear, int endYear) {
        long aDay = TimeUnit.DAYS.toMillis(1);
        long now = new Date().getTime();
        Date start = new Date(now - aDay * 365 * (endYear-startYear+1));
        Date end = new Date(now);

        long startMillis = start.getTime();
        long endMillis = end.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);

        return new Date(randomMillisSinceEpoch);
    }

    public Date getRandomTimestamp(Date start, Date end) {
//        long aDay = TimeUnit.DAYS.toMillis(1);
//        long now = new Date().getTime();
//        Date start;
//        if (d1.compareTo(d2)>=0)
//            start = d1;
//        else start = d2;
//        Date end = new Date(now);

        long startMillis = start.getTime();
        long endMillis = end.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);
        return new Date(randomMillisSinceEpoch);
    }

//    public Date getRandomTimestamp(String s1, String s2) throws ParseException {
//        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(s1);
//        Date end = new SimpleDateFormat("yyyy-MM-dd").parse(s2);
//        long startMillis = start.getTime();
//        long endMillis = end.getTime();
//        long randomMillisSinceEpoch = ThreadLocalRandom
//                .current()
//                .nextLong(startMillis, endMillis);
//        return new Date(randomMillisSinceEpoch);
//    }

    public Date getRandomTimestamp(Date min_date) {
        long now = new Date().getTime();
        Date end = new Date(now);

        long startMillis = min_date.getTime();
        long endMillis = end.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);
        return new Date(randomMillisSinceEpoch);
    }

    public int getExponentialIndex(int size, ExponentialCDF exponen) {
        Random rand = new Random();
        int idx= binary_search_expo(rand, exponen, size);
        return idx;
    }


    public static int binary_search(Random rand, PowerCDF power, int size){
        double randomDis = rand.nextDouble();
        int lowerBound = 0;
        int upperBound = size;
        int midPoint = (upperBound + lowerBound) / 2;

        while (upperBound > (lowerBound + 1)) {
            if (power.getPowerDist().cdf(midPoint) > randomDis) {
                upperBound = midPoint;
            } else {
                lowerBound = midPoint;
            }
            midPoint = (upperBound + lowerBound) / 2;
        }
        return midPoint;
    }

    public int binary_search_expo(Random rand, ExponentialCDF expo, int size){
        // Doing binary search for finding the tag
        double randomDis = rand.nextDouble();
        int lowerBound = 0;
        int upperBound = size;
        int midPoint = (upperBound + lowerBound) / 2;

        while (upperBound > (lowerBound + 1)) {
            if (expo.getExponenDist().cdf(midPoint) > randomDis) {
                upperBound = midPoint;
            } else {
                lowerBound = midPoint;
            }
            midPoint = (upperBound + lowerBound) / 2;
        }
        return midPoint;
    }

}

