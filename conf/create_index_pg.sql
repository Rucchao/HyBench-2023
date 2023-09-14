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


CREATE SEQUENCE IF NOT EXISTS transfer_id_seq;
SELECT SETVAL('transfer_id_seq', (SELECT max(id) FROM transfer));
ALTER TABLE transfer ALTER COLUMN id SET DEFAULT nextval('transfer_id_seq'::regclass);
ALTER sequence transfer_id_seq owner to postgres;
ALTER SEQUENCE transfer_id_seq OWNED BY transfer.id;

CREATE SEQUENCE IF NOT EXISTS checking_id_seq;
SELECT SETVAL('checking_id_seq', (SELECT max(id) FROM checking));
ALTER TABLE checking ALTER COLUMN id SET DEFAULT nextval('checking_id_seq'::regclass);
ALTER sequence checking_id_seq owner to postgres;
ALTER SEQUENCE checking_id_seq OWNED BY checking.id;

CREATE SEQUENCE IF NOT EXISTS loanapps_id_seq;
SELECT SETVAL('loanapps_id_seq', (SELECT max(id) FROM loanapps));
ALTER TABLE loanapps ALTER COLUMN id SET DEFAULT nextval('loanapps_id_seq'::regclass);
ALTER sequence loanapps_id_seq owner to postgres;
ALTER SEQUENCE loanapps_id_seq OWNED BY loanapps.id;

CREATE SEQUENCE IF NOT EXISTS loantrans_id_seq;
SELECT SETVAL('loantrans_id_seq', (SELECT max(id) FROM loantrans));
ALTER TABLE loantrans ALTER COLUMN id SET DEFAULT nextval('loantrans_id_seq'::regclass);
ALTER sequence loantrans_id_seq owner to postgres;
ALTER SEQUENCE loantrans_id_seq OWNED BY loantrans.id;
