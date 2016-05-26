--文档表
drop table if exists document;
create table if not exists document ( 
  file_id varchar(32) primary key,--文档id
  catalog_id varchar(32),--目录id
  file_name  varchar(150),--文档名称
  file_path  varchar(500),--文档实际路径
  author    varchar(100),--拥有者
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

--文档目录表
drop table if exists catalog;
create table if not exists catalog ( 
  catalog_id varchar(32) primary key, --目录id
  parent_catalog_id varchar(32), --上级目录id
  catalog_name  varchar(150), --目录名
  isroot boolean default true, --根路径
  author    varchar(100), --目录所有人
  opdatetime  datetime default now()
);