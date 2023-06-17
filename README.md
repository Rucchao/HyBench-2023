# HyBench - A New Benchmark for HTAP Databases


## Data Generation and Loading

```
bash hybench -t sql -c conf/pg.props -f conf/ddl_pg.sql

bash hybench -t gendata -c conf/pg.props -f conf/stmt_postgres.toml

psql -h localhost -U postgres -d hybench_sf1x -f conf/load_data_pg.sql
```

## Index Building 

```
bash hybench -t sql -c conf/pg.props -f conf/create_index_pg.sql
```

## Run the Benchmark

```
bash hybench -t runall -c conf/pg.props -f conf/stmt_postgres.toml
```

## Performance Metrics
![Model](https://github.com/Rucchao/HyBench-2023/blob/master/Metrics.png)
