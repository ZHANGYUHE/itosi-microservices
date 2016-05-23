package org.iplatform.microservices.core.documentservice.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.iplatform.microservices.core.documentservice.bean.PersonDO;

@Mapper
public interface PersonMapper {
	
	@Select("select * from person where username = #{username}")
	PersonDO findByLoginNameEquals(@Param("username") String username);	
}
