-- create master database and table
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

-- Create tenant database and its tables. Repeat all of the steps below for creation a new database for a new client/company.
CREATE DATABASE workmate_tenant_db1;
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

CREATE TABLE role
(
  id bigserial NOT NULL,
  name character varying(20),
  stamp bigint,
  CONSTRAINT role_pkey PRIMARY KEY (id)
);
INSERT INTO role (name, stamp) VALUES
('ROLE_ADMIN', 1),
('ROLE_USER', 1);

CREATE TABLE user_roles
(
  user_id bigint NOT NULL,
  role_id bigint NOT NULL,
  CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk_user_id FOREIGN KEY (user_id)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_role_id FOREIGN KEY (role_id)
      REFERENCES role (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
-- End of creation a new database for client/company

-- Predefine data.

-- Temporary data
INSERT INTO master_tenant (db_name, url, username, password, driver_class, status) VALUES
('workmate_tenant_db1', 'jdbc:postgresql://localhost:5432/workmate_tenant_db1', 'postgres', 'postgres', 'org.postgresql.Driver', 'ACTIVE'),
('workmate_tenant_db2', 'jdbc:postgresql://localhost:5432/workmate_tenant_db2', 'postgres', 'postgres', 'org.postgresql.Driver', 'ACTIVE'),
('workmate_tenant_db3', 'jdbc:mysql://localhost:3306/workmate_tenant_db3?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Jakarta&useSSL=false', 'root', 'root', 'com.mysql.cj.jdbc.Driver', 'ACTIVE');

