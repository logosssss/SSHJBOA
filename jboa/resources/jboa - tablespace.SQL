spool c:\DBCreateInfoJBOA.log;

-- 创建表空间
CREATE TABLESPACE JBOADB
DATAFILE 'JBOADB.dat' SIZE 100M AUTOEXTEND ON NEXT  10M MAXSIZE UNLIMITED;
-- 创建用户
--connect internal
CREATE USER bdqn  IDENTIFIED BY bdqn;
  
ALTER USER bdqn IDENTIFIED BY bdqn;
ALTER USER bdqn DEFAULT   TABLESPACE JBOADB;
ALTER USER bdqn DEFAULT 	ROLE ALL;
GRANT CONNECT TO bdqn;
GRANT RESOURCE TO bdqn;
GRANT create table to bdqn;
GRANT create trigger to bdqn;
--disconnect
spool out;
Exit;



