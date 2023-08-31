alter table customer INMEMORY;
alter table company INMEMORY;
alter table savingaccount INMEMORY;
alter table checkingaccount INMEMORY;
alter table checking INMEMORY;
alter table transfer INMEMORY;
alter table loanapps INMEMORY;
alter table loantrans INMEMORY;

/* use the following commands to populate the data to IMCS
EXEC DBMS_INMEMORY.POPULATE('USER', 'CUSTOMER');
EXEC DBMS_INMEMORY.POPULATE('USER', 'COMPANY');
EXEC DBMS_INMEMORY.POPULATE('USER', 'CHECKINGACCOUNT');
EXEC DBMS_INMEMORY.POPULATE('USER', 'SAVINGACCOUNT');
EXEC DBMS_INMEMORY.POPULATE('USER', 'CHECKING');
EXEC DBMS_INMEMORY.POPULATE('USER', 'TRANSFER');
EXEC DBMS_INMEMORY.POPULATE('USER', 'LOANAPPS');
EXEC DBMS_INMEMORY.POPULATE('USER', 'LOANTRANS');
*/