CREATE DATABASE workmate_master_db;
DROP TABLE IF EXISTS master_tenant;
CREATE TABLE master_tenant (
  tenant_client_id bigserial NOT NULL,
  db_name varchar(50) NOT NULL,
  url varchar(250) NOT NULL,
  username varchar(50) NOT NULL,
  password varchar(100) NOT NULL,
  driver_class varchar(100) NOT NULL,
  status varchar(10) NOT NULL,
  CONSTRAINT master_tenant_pkey PRIMARY KEY (tenant_client_id)
) ;

INSERT INTO master_tenant (db_name, url, username, password, driver_class, status) VALUES
('workmate_tenant_db1', 'jdbc:postgresql://localhost:5432/workmate_tenant_db1', 'postgres', 'postgres', 'org.postgresql.Driver', 'ACTIVE'),
('workmate_tenant_db2', 'jdbc:postgresql://localhost:5432/workmate_tenant_db2', 'postgres', 'postgres', 'org.postgresql.Driver', 'ACTIVE'),
('workmate_tenant_db3', 'jdbc:mysql://localhost:3306/workmate_tenant_db3?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Jakarta&useSSL=false', 'root', 'root', 'com.mysql.cj.jdbc.Driver', 'ACTIVE');

CREATE DATABASE workmate_tenant_db1;
CREATE DATABASE workmate_tenant_db2;
CREATE TABLE users
(
  id bigserial NOT NULL,
  email character varying(50),
  password character varying(120),
  stamp bigint,
  username character varying(20),
  CONSTRAINT users_pkey PRIMARY KEY (id),
  CONSTRAINT users_email_unique UNIQUE (email),
  CONSTRAINT users_username_unique UNIQUE (username)
);

