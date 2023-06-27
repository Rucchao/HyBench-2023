load data
infile '/opt/oracle/oradata/Data_1x/loanTrans.csv'
badfile '/home/oracle/bad.log'
into table loantrans
fields terminated by ','
(
                id,
                applicantID,
                appID,
                amount,
                status,
                timestamp timestamp   "yyyy-mm-dd HH24:MI:SS.FF3",
                duration,
                contract_timestamp timestamp "yyyy-mm-dd HH24:MI:SS.FF3",
                delinquency
)
