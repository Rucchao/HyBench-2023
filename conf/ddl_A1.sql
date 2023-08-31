CREATE TABLE CUSTOMER
        (
                custID       int PRIMARY KEY,
                companyID    int            ,
                gender       char(6)        ,
                name         char(15)       ,
                age          int            ,
                phone        char(11)       ,
                province     char(15)       ,
                city         char(15)       ,
                loan_balance real           ,
                s_credit     int            ,
                c_credit     int            ,
                b_credit     int            ,
                Isblocked    int            ,
                created_date timestamp           ,
                last_update_timestamp timestamp
        );

CREATE TABLE COMPANY
        (
                companyID    int PRIMARY KEY,
                name         varchar(60)    ,
                category     varchar(50)    ,
                staff_size   int            ,
                loan_balance real           ,
                phone        char(11)       ,
                province     char(15)       ,
                city         char(15)       ,
                SavingCredit int            ,
				CheckingCredit int			,
				LoanCredit	 int			,
                Isblocked    int            ,
                created_date timestamp           ,
                last_update_timestamp timestamp
        );
CREATE TABLE SAVINGACCOUNT
        (
                accountID int PRIMARY KEY,
                userID    int            ,
                balance   real           ,
                Isblocked int            ,
                timestamp timestamp,
                fresh_ts timestamp default current_timestamp
        );
CREATE TABLE CHECKINGACCOUNT
        (
                accountID int PRIMARY KEY,
                userID    int            ,
                balance   real           ,
                Isblocked int            ,
                timestamp timestamp
        );

CREATE TABLE TRANSFER
        (
                id       NUMBER(19,0) PRIMARY KEY,
                sourceID int            ,
                targetID int            ,
                amount   real           ,
                type     char(10)       ,
                timestamp timestamp     ,
                fresh_ts timestamp default current_timestamp
        );



CREATE TABLE CHECKING
        (
                id       int PRIMARY KEY,
                sourceID int            ,
                targetID int            ,
                amount   real           ,
                type     char(10)       ,
                timestamp timestamp
        );



CREATE TABLE LOANAPPS
        (
                id          int PRIMARY KEY,
                applicantID int            ,
                amount      real           ,
                duration    int            ,
                status      char(12)       ,
                timestamp timestamp
        );


CREATE TABLE LOANTRANS
        (
                id          int PRIMARY KEY ,
                applicantID int             ,
                appID       int             ,
                amount      real            ,
                status      char(12)        ,
                timestamp timestamp         ,
                duration int                ,
                contract_timestamp timestamp,
                delinquency int
        );
