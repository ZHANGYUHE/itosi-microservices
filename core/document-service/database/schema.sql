--文档表
CREATE TABLE document ( 
  file_id VARCHAR(32) PRIMARY KEY,
  file_name  VARCHAR(150),
  file_path  VARCHAR(500),
  author    VARCHAR(100),
  opdatetime  DATETIME default now()
);

--文档操作记录
CREATE TABLE document_op_log ( 
  uuid VARCHAR(32) PRIMARY KEY,
  file_id VARCHAR(32),
  operater  VARCHAR(100),
  optype  VARCHAR(20),
  opdatetime  DATETIME default now()
);

--文档操作记录
CREATE TABLE document_search_log ( 
  uuid VARCHAR(32) PRIMARY KEY,
  operater  VARCHAR(100),
  searchparam  VARCHAR(500),
  opdatetime  DATETIME default now()
);

--用户表
CREATE TABLE person ( 
  uuid VARCHAR(32) PRIMARY KEY,
  username  VARCHAR(100),
  truename  VARCHAR(500),
  password  VARCHAR(500),
  opdatetime  DATETIME default now()
);