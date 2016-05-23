package org.iplatform.microservices.core.documentservice.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.iplatform.microservices.core.documentservice.bean.DocumentDO;
import org.iplatform.microservices.core.documentservice.bean.DocumentOpLogDO;
import org.iplatform.microservices.core.documentservice.bean.DocumentSearchLogDO;

@Mapper
public interface DocumentMapper {
	
	@Select("select * from document")
	List<DocumentDO> list();

	@Select("select * from document where author=#{author}")
	List<DocumentDO> mylist(@Param("author") String author);

	@Select("<script>select * from document where author=#{author} and <foreach item='file_name' index='index' collection='file_names' open='(' separator='or' close=')'>file_name like #{file_name}</foreach></script>")
	List<DocumentDO> search(Map<String,Object> params);
	
	@Select("select * from document where file_id = #{file_id}")
	DocumentDO findById(@Param("file_id") String file_id);
	
	@Delete("delete from document where file_id = #{file_id}")
	void deleteById(@Param("file_id") String id);	
	
	@Insert("insert into document (file_id, file_name, file_path, author) values (#{file_id}, #{file_name}, #{file_path}, #{author})")
	void create(DocumentDO document);	
	
	@Update("update document set file_name=#{file_name}, file_path=#{file_path}, author=#{author} where file_id=#{file_id}")
	int updateAll(DocumentDO document);

	@Insert("insert into document_op_log (uuid, file_id, operater, optype) values (#{uuid}, #{file_id}, #{operater}, #{optype})")
	void createoplog(DocumentOpLogDO documentoplog);	

	@Insert("insert into document_search_log (uuid, operater, searchparam) values (#{uuid}, #{operater}, #{searchparam})")
	void createsearchlog(DocumentSearchLogDO documentsearchLog);		
		
	/*
	@Update("<script>update document <set> "
			+ "<if test='name != null'>name=#{name},</if> "
			+ "<if test='age != null'>age=${age},</if> "
			+ "<if test='place != null'>place=#{place},</if> "
			+ "</set> "
			+ "where file_id=${file_id}</script>")
	int update(Document person);
	*/	
}
