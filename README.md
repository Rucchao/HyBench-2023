# HyBench - A New Benchmark for HTAP Databases 

Please refer to our [VLDB 2024 Research Paper](https://vldb.org/pvldb/volumes/17/paper/HyBench%3A%20A%20New%20Benchmark%20for%20HTAP%20Databases) for more details:

```
Chao Zhang, Guoliang Li, and Tao Lv. "HyBench: A New Benchmark for HTAP Databases." Proceedings of the VLDB Endowment 17.5 (2024): 939-951.
```
Please cite our paper as follows:

@article{zhang2024hybench,
  title={HyBench: A New Benchmark for HTAP Databases},\
  author={Chao Zhang and Guoliang Li and Tao Lv},\
  journal={Proceedings of the VLDB Endowment},\
  volume={17},\
  number={5},\
  pages={939--951},\
  year={2024},\
  publisher={VLDB Endowment}\
}


## Software Environments
```
MAVEN version > 3.6.3

JAVA version > 17

Tested OS: |MacOS Ventura 13.2.1|Ubuntu 22.04|CentOS 7| Windows 10

An example for JAVA installation in MacOS: brew install openjdk@17
```


## Benchmark Configuration

Configure the parameters for PostgreSQL 14 including **username, password, username_ap, password_ap** in the following file (NB: configure the same URL for a standalone setting):

```
cd HyBench-2023
vim ./conf/pg.props
```
## Step 1: Data Generation and Loading [PostgreSQL 14]
```
psql -h localhost -U postgres -c 'create database hybench_sf1x;'

bash hybench -t sql -c conf/pg.props -f conf/ddl_pg.sql

bash hybench -t gendata -c conf/pg.props -f conf/stmt_postgres.toml

psql -h localhost -U postgres -d hybench_sf1x -f conf/load_data_pg.sql
```

## Step 2: Index Building [PostgreSQL 14]

```
bash hybench -t sql -c conf/pg.props -f conf/create_index_pg.sql
```

## Step 3: Run the Benchmark [PostgreSQL 14]

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
Solution: when the Maven version is greater than 3.8, you need to remove the tags of blocked mirrors of $Maven_home/conf/settings.xml. 
```

(3) With the OS in Windows, you may use the Java command to run HyBench: 
```
Solution: java -cp "HyBench-1.0-SNAPSHOT.jar;lib/*" com.hybench.HyBench [-t][-c][-f]
```
(4) File not Found for PG data loading
```
Solution: modify the path to data directory in the conf/load_data_pg.sql file. For instance, replace 'Data_1x/customer.csv' with 'Data_10x/customer.csv'
```

(5) java.util.concurrent.ExecutionException: java.lang.NullPointerException: Cannot invoke "java.util.List.size()" because "this.Related_Blocked_Checking_ids" is null

```
Solution: make sure the data directory is under the working path and contains two files: 'Related_checking_bids' with Related_transfer_bids'
```

(6) Fail to generate data
```
make sure the user has the write permission to the folder or use the sudo command: sudo bash hybench -t gendata -c conf/pg.props -f conf/stmt_postgres.toml
```

(7) Benchmarking Parameters

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

(9) Please refer to [parameters.toml](https://github.com/Rucchao/HyBench-2023/blob/master/src/main/resources/parameters.toml) for the parameters of data generation.

