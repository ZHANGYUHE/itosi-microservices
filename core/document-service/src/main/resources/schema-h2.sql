--文档表
drop table if exists document;
create table if not exists document ( 
  file_id varchar(32) primary key,
  file_name  varchar(150),
  file_path  varchar(500),
  author    varchar(100),
  opdatetime  datetime default now()
);

--文档操作记录
drop table if exists document_op_log;
create table if not exists document_op_log ( 
  uuid varchar(32) primary key,
  file_id varchar(32),
  operater  varchar(100),
  optype  varchar(20),
  opdatetime  datetime default now()
);

--文档操作记录
drop table if exists document_search_log;
create table if not exists document_search_log ( 
  uuid varchar(32) primary key,
  operater  varchar(100),
  searchparam  varchar(500),
  opdatetime  datetime default now()
);

--用户表
drop table if exists person;
create table person ( 
  uuid varchar(32) primary key,
  username  varchar(100),
  truename  varchar(500),
  password  varchar(500),
  opdatetime  datetime default now()
);