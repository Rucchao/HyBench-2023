declare
 ex number;
begin
  select MAX(id)  + 1 into ex from LOANTRANS;
  If ex > 0 then
    begin
            execute immediate 'DROP SEQUENCE LOANTRANS_SEQ';
      exception when others then
        null;
    end;
    execute immediate 'CREATE SEQUENCE LOANTRANS_SEQ INCREMENT BY 1 START WITH ' || ex ;
  end if;
end;