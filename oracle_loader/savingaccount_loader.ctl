load data
infile '/sys/fs/cgroup/Data_1x/savingAccount.csv'
badfile '/home/oracle/bad.log'
into table savingaccount
fields terminated by ','
(
                accountID,
                userID,
                balance,
                Isblocked,
                timestamp timestamp "yyyy-mm-dd HH24:MI:SS.FF3"
)