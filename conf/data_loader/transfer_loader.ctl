load data
infile '/opt/oracle/oradata/Data_1x/transfer.csv'
badfile '/home/oracle/bad.log'
into table transfer
fields terminated by ','
trailing nullcols
(
                id,
                sourceID,
                targetID,
                amount,
                type,
                timestamp timestamp "yyyy-mm-dd HH24:MI:SS.FF3"     ,
                fresh_ts timestamp
)
