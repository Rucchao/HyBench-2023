CREATE TABLE customer (
custID int PRIMARY KEY,
companyID int,
gender char(6),
name char(15),
age int,
phone char(11),
province char(15),
city char(15),
loan_balance real,
saving_credit int,
checking_credit int,
loan_credit int,
Isblocked int,
created_date Date,
last_update_timestamp timestamp
);

CREATE TABLE company (
companyID int PRIMARY KEY,
name varchar,
category varchar,
staff_size int,
loan_balance real,
phone char(11),
province char(15),
city char(15),
saving_credit int,
checking_credit int,
loan_credit int,
Isblocked int,
created_date Date,
last_update_timestamp timestamp
);


CREATE TABLE savingAccount (
 accountID int PRIMARY KEY,
 userID int,
 balance real,
 Isblocked int,
 timestamp timestamp,
 fresh_ts timestamp  default current_timestamp
);

CREATE TABLE checkingAccount (
accountID int PRIMARY KEY,
userID int,
balance real,
Isblocked int,
timestamp timestamp
);

CREATE TABLE transfer (
  id int PRIMARY KEY,
  sourceID int,
  targetID int,
  amount real,
  type char(10),
  timestamp timestamp,
  fresh_ts timestamp  default current_timestamp
);

CREATE TABLE checking (
  id int PRIMARY KEY,
  sourceID int,
  targetID int,
  amount real,
  type char(10),
  timestamp timestamp
);

CREATE TABLE loanapps (
  id int PRIMARY KEY,
  applicantID int,
  amount real,
  duration int,
  status char(12),
  timestamp timestamp
);

CREATE TABLE loantrans (
  id int PRIMARY KEY,
  applicantID int,
  appID int,
  amount real,
  status char(12),
  timestamp timestamp,
  duration int,
  contract_timestamp timestamp,
  delinquency int
);
