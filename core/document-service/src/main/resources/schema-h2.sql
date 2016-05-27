--文档表
drop table if exists document;
create table if not exists document ( 
  file_id varchar(32) primary key,--文档id
  catalog_id varchar(32),--目录id
  file_name  varchar(150),--文档名称
  file_path  varchar(500),--文档实际路径
  author    varchar(100),--拥有者
  size	bigint,--文档大小
  tags varchar(1000), --文档标签，多个逗号分隔
  down_cnt bigint, --下载量
  description varchar(1000), --文档简介
  state varchar(100) default 'private', --文档状态,public公开，share分享，private私有,多个状态逗号分隔
  suffdix varchar(1000),--扩展名
  star_act int, --精品级别,1-5表示评级
  opdatetime  datetime default now()
);

--文档操作记录
drop table if exists document_op_log;
create table if not exists document_op_log ( 
  uuid varchar(32) primary key,
  file_id varchar(32), --文档id
  file_name  varchar(150),--文档名称
  author    varchar(100),--拥有者
  description varchar(1000), --文档简介
  operater  varchar(100), --操作人
  optype  varchar(20), --操作类型,create,info,download,...
  opdatetime  datetime default now()
);

--文档搜索记录
drop table if exists document_search_log;
create table if not exists document_search_log ( 
  uuid varchar(32) primary key,
  operater  varchar(100), --操作人
  searchparam  varchar(500), --搜索参数
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

--收藏文档
drop table if exists document_collect;
create table if not exists document_collect ( 
  collect_id varchar(32) primary key, --收藏id
  file_id varchar(32), --文档id
  file_state  boolean default true, --文档状态，有效/失效
  collect_author    varchar(100), --收藏人	
  opdatetime  datetime default now()
);
