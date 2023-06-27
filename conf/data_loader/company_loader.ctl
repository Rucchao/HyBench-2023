load data
infile '/opt/oracle/oradata/Data_1x/company.csv'
badfile '/home/oracle/bad.log'
into table company
fields terminated by ','
(
                companyID,
                name,
                category,
                staff_size,
                loan_balance,
                phone,
                province,
                city,
                SavingCredit,
				        CheckingCredit,
				        LoanCredit,
                Isblocked,
                created_date timestamp "yyyy-mm-dd HH24:MI:SS.FF3",
                last_update_timestamp timestamp "yyyy-mm-dd HH24:MI:SS.FF3"
)
