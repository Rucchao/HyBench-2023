# HyBench - A New Benchmark for HTAP Databases


## Data Generation and Loading

```
bash hybench -t gendata -c conf/pg.props -f conf/stmt_postgres.toml
```

## Index Building 

```
bash hybench -t sql -c conf/pg.props -f conf/create_index_pg.sql
```

## Run the Benchmark

```
bash hybench -t runall -c conf/pg.props -f conf/stmt_postgres.toml
```
