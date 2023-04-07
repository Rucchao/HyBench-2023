load data
infile '/sys/fs/cgroup/Data_1x/loantrans.csv'
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