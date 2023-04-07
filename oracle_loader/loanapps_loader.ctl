load data
infile '/sys/fs/cgroup/Data_1x/loanapps.csv'
badfile '/home/oracle/bad.log'
into table loanapps
fields terminated by ','
(
                id,
                applicantID,
                amount,
                duration,
                status,
                timestamp timestamp "yyyy-mm-dd HH24:MI:SS.FF3"
)