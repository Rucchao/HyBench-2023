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

### 配置文件参数列表

| 参数名            | 默认值                                       | 值设置建议                                                                                  | 备注                                       |   |
|----------------|-------------------------------------------|----------------------------------------------------------------------------------------|------------------------------------------|---|
| db             | mysql                                     | 代表本轮测试的数据库是什么名字，若为MySQL协议的，则填写MySQL,若为postgresql协议的则填写postgresql，同时也可以填写其他类型的数据库。      | 在代码中无具体意义，主要用于打印结果。                      |   |
| classname      | com.mysql.jdbc.Driver                     | 指TP类的SQL访问采用那个JDBC的类                                                                   |                                          |   |
| username       | xxx                                       | 指TP类访问连接的用户名                                                                           |                                          |   |
| password       | xxx                                       | 指TP类访问连接的密码                                                                            |                                          |   |
| url            | jdbc:oceanbase:// :<PORT>/?useUnicode=xxx | TP类访问的jdbc 连接串，根据各jdbc的定义自行填写。后续可根据实际情况填写连接参数                                          |                                          |   |
| url_ap         | jdbc:oceanbase:// :<PORT>/?useUnicode=xxx | AP类访问的jdbc 连接串，根据各jdbc的定义自行填写。后续可根据实际情况填写连接参数                                          |                                          |   |
| classname_ap   | com.mysql.jdbc.Driver                     | 指AP类的SQL访问采用那个JDBC的类                                                                   |                                          |   |
| username_ap    | xxx                                       | AP类访问连接的用户名                                                                            |                                          |   |
| password_ap    | xxx                                       | AP类访问连接的用户名                                                                            |                                          |   |
| sf             | 1x                                        | 代表测试的数据集大小，仅支持1x、10x。                                                       | 在生成数据和执行SQL时候均会用到。只能支持2个入参值，不支持其他的数据集大小。 |   |
| at1_percent    | 35                                        | 代表在执行混合负载时，AT1类的SQL的执行比例。如值为35，则代表为35%。                                                | 与其他几个AT类的SQL的执行比例合为100。建议直接使用默认值。        |   |
| at2_percent    | 25                                        | 代表在执行混合负载时，AT2类的SQL的执行比例。如值为25，则代表为25%。                                                | 与其他几个AT类的SQL的执行比例合为100。建议直接使用默认值。        |   |
| at3_percent    | 15                                        | 代表在执行混合负载时，AT3类的SQL的执行比例。如值为15，则代表为15%。                                                | 与其他几个AT类的SQL的执行比例合为100。建议直接使用默认值。        |   |
| at4_percent    | 15                                        | 代表在执行混合负载时，AT4类的SQL的执行比例。如值为35，则代表为15%。                                                | 与其他几个AT类的SQL的执行比例合为100。建议直接使用默认值。        |   |
| at5_percent    | 7                                         | 代表在执行混合负载时，AT5类的SQL的执行比例。如值为35，则代表为7%。                                                 | 与其他几个AT类的SQL的执行比例合为100。建议直接使用默认值。        |   |
| at6_percent    | 3                                         | 代表在执行混合负载时，AT6类的SQL的执行比例。如值为35，则代表为3%。                                                 | 与其他几个AT类的SQL的执行比例合为100。建议直接使用默认值。        |   |
| apclient       | 1                                         | 在执行AP类的测试时，所开启的AP类连接数量。也可以理解为AP请求的并发数。                                                 |                                          |   |
| tpclient       | 1                                         | 在执行TP类的测试时，所开启的TP类连接数量。也可以理解为TP请求的并发数。                                                 |                                          |   |
| fresh_interval | 20                                        | 新鲜度测试的间隔时间。基于xpRunMins 的值来判断。若此值设置为20.xpRunMins 设置为1分钟，则代表1分钟除以20为3秒。意味着每3秒执行以下新鲜度的查询。 |                                          |   |
| apRunMins      | 1                                         | AP类测试的运行时长。                                                                            |                                          |   |
| tpRunMins      | 1                                         | TP类测试的运行时长                                                                             |                                          |   |
| xpRunMins      | 1                                         | XP类测试的运行时长                                                                             |                                          |   |
| xtpclient      | 1                                         | 在XP测试场景中，TP类型的请求所使用的连接数。                                                               |                                          |   |
| xapclient      | 1                                         | 在XP测试场景中，AP类型的请求所使用的连接数                                                                |                                          |   |
| apround        | 1                                         | AP类型请求的执行轮数，要求至少1轮完整跑完。                                                                | 仅在AP Power测试负载中生效。                       |   |



