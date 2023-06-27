CREATE SEQUENCE TRANSFER_SEQ INCREMENT BY 1 START WITH 6000000;

CREATE SEQUENCE CHECKING_SEQ INCREMENT BY 1 START WITH 600000;

CREATE SEQUENCE LOANAPPS_SEQ INCREMENT BY 1 START WITH 600000;

CREATE SEQUENCE LOANTRANS_SEQ INCREMENT BY 1 START WITH 600000;

create index idx_loanapps_1 on loanapps ( applicantid );

create index idx_loanapps_2 on loanapps ( timestamp );

create index idx_loantrans_1 on loantrans ( applicantid );

create index idx_loantrans_2 on loantrans ( timestamp );

create index idx_transfer_1 on transfer ( sourceid );

create index idx_transfer_2 on transfer ( targetid );

create index idx_transfer_3 on transfer ( type );

create index idx_checking_1 on checking ( sourceid );

create index idx_checking_2 on checking ( targetid );

create index idx_customer_1 on customer ( companyid );

create index idx_company_1 on company ( category );

create index idx_sa_1 on savingaccount ( userid );

create index idx_ca_1 on checkingaccount ( userid );
