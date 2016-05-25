package org.iplatform.microservices.core.documentservice.bean;

import java.sql.Date;

public class CatalogDO {
	private String catalog_id;
	private String parent_catalog_id;
	private String catalog_name;
	private Boolean isroot; 
	private String author;
	private Date opdatetime;
	public String getCatalog_id() {
		return catalog_id;
	}
	public void setCatalog_id(String catalog_id) {
		this.catalog_id = catalog_id;
	}
	public String getParent_catalog_id() {
		return parent_catalog_id;
	}
	public void setParent_catalog_id(String parent_catalog_id) {
		this.parent_catalog_id = parent_catalog_id;
	}
	public String getCatalog_name() {
		return catalog_name;
	}
	public void setCatalog_name(String catalog_name) {
		this.catalog_name = catalog_name;
	}
	public Boolean getIsroot() {
		return isroot;
	}
	public void setIsroot(Boolean isroot) {
		this.isroot = isroot;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getOpdatetime() {
		return opdatetime;
	}
	public void setOpdatetime(Date opdatetime) {
		this.opdatetime = opdatetime;
	}
}
