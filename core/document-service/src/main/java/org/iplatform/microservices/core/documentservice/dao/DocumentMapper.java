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
	List<DocumentDO> mylistWithOutCatalog(@Param("author") String author);
	
	@Select("select count(1) from document where author=#{author}")
	Integer myCount(@Param("author") String author);

	@Select("select sum(down_cnt) from document where author=#{author}")
	Integer myDownCount(@Param("author") String author);
	
	@Select("select * from document where author=#{author} and catalog_id is null")
	List<DocumentDO> mylistWithRootCatalog(@Param("author") String author);	
	
	@Select("select * from document where author=#{author} and catalog_id=#{catalog_id}")
	List<DocumentDO> mylistWithCatalog(@Param("author") String author,@Param("catalog_id") String catalog_id);		

	@Select("<script>select * from document where author=#{author} and <foreach item='file_name' index='index' collection='file_names' open='(' separator='or' close=')'>file_name like #{file_name}</foreach></script>")
	List<DocumentDO> search(Map<String,Object> params);
	
	@Select("select * from document where file_id = #{file_id}")
	DocumentDO findById(@Param("file_id") String file_id);
	
	@Delete("delete from document where file_id = #{file_id}")
	void deleteById(@Param("file_id") String id);	
	
	@Delete("delete from document where file_path = #{file_path}")
	void deleteByFilePath(@Param("file_path") String file_path);		
	
	@Delete("delete from document where catalog_id = #{catalog_id}")
	void deleteByCatalogId(@Param("catalog_id") String catalog_id);		
	
	@Insert("insert into document (file_id, catalog_id, file_name, file_path, author) values (#{file_id}, #{catalog_id}, #{file_name}, #{file_path}, #{author})")
	void create(DocumentDO document);	
	
	@Update("update document set file_name=#{file_name}, file_path=#{file_path}, author=#{author} where file_id=#{file_id}")
	int updateAll(DocumentDO document);

	@Insert("insert into document_op_log (uuid, file_id, operater, optype) values (#{uuid}, #{file_id}, #{operater}, #{optype})")
	void createoplog(DocumentOpLogDO documentoplog);	

	@Select("<script>select * from document_op_log where operater=#{operater} and optype in <foreach item='item' index='index' collection='optypes' open='(' separator=',' close=')'> #{item} </foreach></script>")
	List<DocumentOpLogDO> myOpeaters(@Param("operater") String operater,@Param("optypes") List<String> optypes);
	
	@Insert("insert into document_search_log (uuid, operater, searchparam) values (#{uuid}, #{operater}, #{searchparam})")
	void createsearchlog(DocumentSearchLogDO documentsearchLog);		
		
	
	@Update("<script>update document <set> "
			+ "<if test='file_name != null'>file_name=#{file_name},</if> "
			+ "<if test='catalog_id != null'>catalog_id=#{catalog_id},</if> "
			+ "</set> "
			+ "where file_id=${file_id}</script>")
	int update(DocumentDO document);
}
