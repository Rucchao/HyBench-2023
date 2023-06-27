load data
infile '/opt/oracle/oradata/Data_1x/loanApps.csv'
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
