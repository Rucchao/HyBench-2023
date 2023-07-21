# HyBench - A New Benchmark for HTAP Databases 

## Environments
```
MAVEN version > 3.0

JAVA version > 17

Tested OS: |MacOS Ventura 13.2.1|Ubuntu 22.04|CentOS 7

```

## PostgreSQL Streaming Replication [Optional for Freshness Evaluation]

Please install the PostgreSQL 14 and configure the streaming replication as follows:

```
https://wiki.postgresql.org/wiki/Streaming_Replication
```

## Benchmark Configuration

Configure the parameters such as scale factor, endpoints (i.e., urls), and client number as follows:

```
vim https://github.com/Rucchao/HyBench-2023/blob/master/conf/pg.props
```
## Data Generation and Loading [PostgreSQL]
```
cd HyBench-2023

sudo -u postgres psql -c 'create database hybench_sf1x;'

bash hybench -t sql -c conf/pg.props -f conf/ddl_pg.sql

bash hybench -t gendata -c conf/pg.props -f conf/stmt_postgres.toml

psql -h localhost -U postgres -d hybench_sf1x -f conf/load_data_pg.sql
```

## Index Building [PostgreSQL]

```
bash hybench -t sql -c conf/pg.props -f conf/create_index_pg.sql
```

## Run the Benchmark [PostgreSQL]

```
bash hybench -t runall -c conf/pg.props -f conf/stmt_postgres.toml
```

## Performance Metrics
![Model](https://github.com/Rucchao/HyBench-2023/blob/master/Metrics.png)

## Benchmarking Notes
(1) Error: Could not find or load main class com.hybench.HyBench
```
Solution: modify the path to lib directory in the hybench file
```

(2) mvn clean package...Failed to execute goal on project HyBench:HyBench:jar:1.0-SNAPSHOT
```
Solution: once re-compiled with mvn clean pacakge, replace the HyBench-1.0-SNAPSHOT.jar in the lib folder with the newly-generated file in the target folder
```

(3) File not Found for PG data loading
```
Solution: modify the path to data directory in the conf/load_data_pg.sql file. For instance, replace 'Data_1x/customer.csv' with 'Data_10x/customer.csv'
```

(4) Benchmarking other HTAP databases

Please refer to [wiki](https://github.com/Rucchao/HyBench-2023/wiki) for benchmarking more HTAP databases.

(5) Benchmarking Parameters

### Parameter List

| Name            | Default                                       | Description                                                                                  | Comments                                      |   |
|----------------|-------------------------------------------|----------------------------------------------------------------------------------------|------------------------------------------|---|
| db             | postgres                                     | System Under Test      |                      |   |
| classname      | org.postgresql.Driver                     | TP JDBC Driver                                                                 |                                          |   |
| username       | xxx                                       | TP username                                                                           |                                          |   |
| password       | xxx                                       |TP password                                                                            |                                          |   |
| url            | jdbc:postgresql://localhost:5433/hybench_sf1x | TP JDBC URL                                          |                                          |   |
| url_ap         | jdbc:postgresql://localhost:5433/hybench_sf1x | AP JDBC URL                                         |                                          |   |
| classname_ap   | org.postgresql.Driver                    |  AP JDBC Driver                                                                    |                                          |   |
| username_ap    | xxx                                       | AP username                                                                            |                                          |   |
| password_ap    | xxx                                       | AP password                                                                            |                                          |   |
| sf             | 1x                                        | scale factor: 1x、10x、100x。                                                       |  |   |
| at1_percent    | 35                                        | AT1 ratio                                              | sum= 100%        |   |
| at2_percent    | 25                                        | AT2 ratio                                                   | sum= 100%          |   |
| at3_percent    | 15                                        | AT3 ratio                                                | sum= 100%          |   |
| at4_percent    | 15                                        | AT4 ratio                                                 | sum= 100%          |   |
| at5_percent    | 7                                         | AT5 ratio                                                  | sum= 100%          |   |
| at6_percent    | 3                                         | AT6 ratio                                                 | sum= 100%          |   |
| apclient       | 1                                         | AP concurrency                                             |                                          |   |
| tpclient       | 1                                         | TP concurrency                                                 |                                          |   |
| fresh_interval | 20                                        | If xpRunMins is set to 1 min，then the freshne evaluation is performed every 60/20=3 seconds  |                                          |   |
| apRunMins      | 1                                         | AP evaluation time                                                                            |                                          |   |
| tpRunMins      | 1                                         | TP evaluation time                                                                              |                                          |   |
| xpRunMins      | 1                                         | XP evaluation time                                                                             |                                          |   |
| xtpclient      | 1                                         | XP-ATS concurrency                                                             |                                          |   |
| xapclient      | 1                                         | XP-IQS concurrency                                                                |                                          |   |
| apround        | 1                                         | AP round，at least 1 round should be evaluated                                                                | AP Power test                      |   |



