CREATE INDEX ON transfer (sourceid);
CREATE INDEX ON transfer (targetid);
CREATE INDEX ON transfer (fresh_ts);

CREATE INDEX ON checking (sourceid);
CREATE INDEX ON checking (targetid);
CREATE INDEX ON customer (companyid);

CREATE INDEX ON loanapps (applicantid);
CREATE INDEX ON loantrans (applicantid);
CREATE INDEX ON loantrans (appid);