# HyBench - A New Benchmark for HTAP Databases

## Environments
```
JAVA version > 17

Tested OS: |MacOS Ventura 13.2.1|Ubuntu 22.04|CentOS 7

```

## Data Generation and Loading [PostgreSQL]
```
cd HyBench-2023

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
Solution: once re-compiled with mvn clean pacakge, replace the HyBench-1.0-SNAPSHOT.jar in the lib folder with the newly-generated file in the targe folder
```

