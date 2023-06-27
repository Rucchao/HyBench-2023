load data
infile '/opt/oracle/oradata/Data_1x/customer.csv'
badfile '/home/oracle/bad.log'
into table customer
fields terminated by ','
(               custID,
                companyID,
                gender,
                name,
                age,
                phone,
                province,
                city,
                loan_balance,
                s_credit,
                c_credit,
                b_credit,
                Isblocked,
                created_date timestamp "yyyy-mm-dd HH24:MI:SS.FF3",
                last_update_timestamp timestamp "yyyy-mm-dd HH24:MI:SS.FF3"
)
