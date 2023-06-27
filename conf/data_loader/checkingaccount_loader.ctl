load data
infile '/opt/oracle/oradata/Data_1x/checkingAccount.csv'
badfile '/home/oracle/bad.log'
into table checkingaccount
fields terminated by ','
(
                accountID,
                userID,
                balance,
                Isblocked,
                timestamp timestamp "yyyy-mm-dd HH24:MI:SS.FF3"
)
