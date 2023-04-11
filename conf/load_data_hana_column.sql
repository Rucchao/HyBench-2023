SET SCHEMA HYBENCH1X_COLUMN;

IMPORT FROM CSV FILE '/hana/mounts/Data_1x/customer.csv'
    INTO HYBENCH1X_COLUMN.customer
    WITH THREADS 10
    BATCH 50000
    RECORD DELIMITED BY '\n'
    FIELD DELIMITED BY ','
    OPTIONALLY ENCLOSED BY '"'
    TIMESTAMP FORMAT 'YYYY-MM-DD HH24:MI:SS.FF3'
    ERROR LOG 'error_log.txt'
;

IMPORT FROM CSV FILE '/hana/mounts/Data_1x/company.csv'
    INTO HYBENCH1X_COLUMN.company
    WITH THREADS 10
    BATCH 50000
    RECORD DELIMITED BY '\n'
    FIELD DELIMITED BY ','
    OPTIONALLY ENCLOSED BY '"'
    TIMESTAMP FORMAT 'YYYY-MM-DD HH24:MI:SS.FF3'
    ERROR LOG 'error_log.txt'
;

IMPORT FROM CSV FILE '/hana/mounts/Data_1x/savingAccount.csv'
    INTO HYBENCH1X_COLUMN.savingaccount
    WITH THREADS 10
    BATCH 50000
    RECORD DELIMITED BY '\n'
    FIELD DELIMITED BY ','
    OPTIONALLY ENCLOSED BY '"'
    TIMESTAMP FORMAT 'YYYY-MM-DD HH24:MI:SS.FF3'
    ERROR LOG 'error_log.txt'
;

IMPORT FROM CSV FILE '/hana/mounts/Data_1x/checkingAccount.csv'
    INTO HYBENCH1X_COLUMN.checkingaccount
    WITH THREADS 10
    BATCH 50000
    RECORD DELIMITED BY '\n'
    FIELD DELIMITED BY ','
    OPTIONALLY ENCLOSED BY '"'
    TIMESTAMP FORMAT 'YYYY-MM-DD HH24:MI:SS.FF3'
    ERROR LOG 'error_log.txt'
;

IMPORT FROM CSV FILE '/hana/mounts/Data_1x/checking.csv'
    INTO HYBENCH1X_COLUMN.checking
    WITH THREADS 10
    BATCH 50000
    RECORD DELIMITED BY '\n'
    FIELD DELIMITED BY ','
    OPTIONALLY ENCLOSED BY '"'
    TIMESTAMP FORMAT 'YYYY-MM-DD HH24:MI:SS.FF3'
    ERROR LOG 'error_log.txt'
;

IMPORT FROM CSV FILE '/hana/mounts/Data_1x/loanApps.csv'
    INTO HYBENCH1X_COLUMN.loanapps
    WITH THREADS 10
    BATCH 50000
    RECORD DELIMITED BY '\n'
    FIELD DELIMITED BY ','
    OPTIONALLY ENCLOSED BY '"'
    TIMESTAMP FORMAT 'YYYY-MM-DD HH24:MI:SS.FF3'
    ERROR LOG 'error_log.txt'
;

IMPORT FROM CSV FILE '/hana/mounts/Data_1x/loanTrans.csv'
    INTO HYBENCH1X_COLUMN.loantrans
    WITH THREADS 10
    BATCH 50000 RECORD
    DELIMITED BY '\n'
    FIELD DELIMITED BY ','
    OPTIONALLY ENCLOSED BY '"'
    TIMESTAMP FORMAT 'YYYY-MM-DD HH24:MI:SS.FF3'
    ERROR LOG 'error_log.txt'
;

IMPORT FROM CSV FILE '/hana/mounts/Data_1x/transfer.csv'
   INTO HYBENCH1X_COLUMN.transfer
   WITH THREADS 10
   BATCH 50000 RECORD
   DELIMITED BY '\n'
   FIELD DELIMITED BY ','
   OPTIONALLY ENCLOSED BY '"'
   TIMESTAMP FORMAT 'YYYY-MM-DD HH24:MI:SS.FF3'
   ERROR LOG 'error_log.txt';