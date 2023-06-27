load data
infile '/opt/oracle/oradata/Data_1x/checking.csv'
badfile '/home/oracle/bad.log'
into table checking
fields terminated by ','
(
                id,
                sourceID,
                targetID,
                amount,
                type,
                timestamp timestamp "yyyy-mm-dd HH24:MI:SS.FF3"
)
