package org.iplatform.microservices.core.documentservice.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.iplatform.microservices.core.documentservice.bean.CatalogDO;
import org.iplatform.microservices.core.documentservice.bean.DocumentDO;
import org.iplatform.microservices.core.documentservice.bean.DocumentOpLogDO;
import org.iplatform.microservices.core.documentservice.bean.DocumentSearchLogDO;

@Mapper
public interface CatalogMapper {

	@Insert("insert into catalog (catalog_id, parent_catalog_id, catalog_name, isroot, author) values (#{catalog_id}, #{parent_catalog_id}, #{catalog_name}, ${isroot}, #{author})")
	void create(CatalogDO catalog);	

	@Delete("delete from catalog where catalog_id = #{catalog_id}")
	void deleteById(@Param("catalog_id") String catalog_id);
	
	@Update("update catalog set catalog_name=#{catalog_name} where catalog_id=#{catalog_id}")
	int updateCatalogName(String catalog_name, String catalog_id);	
	
	@Select("select * from catalog where catalog_id=#{catalog_id}")
	CatalogDO getCatalogById(@Param("catalog_id") String catalog_id);
	
	@Select("select * from catalog where author=#{author} and isroot=1")
	List<CatalogDO> myCatalog(@Param("author") String author);

	@Select("select * from catalog where author is null and isroot=1")
	List<CatalogDO> getPublicCatalog();
	
	@Select("select * from catalog where author=#{author} and parent_catalog_id=#{parent_catalog_id}")
	List<CatalogDO> getChildCatalog(@Param("author") String author,@Param("parent_catalog_id") String parent_catalog_id);		

}
