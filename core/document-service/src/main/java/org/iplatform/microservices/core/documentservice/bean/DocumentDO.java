package org.iplatform.microservices.core.documentservice.bean;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DocumentDO {
	private String file_id;
	private String file_name;
	private String author;
	private String file_path;
	private String catalog_id;
	private Date opdatetime;
	private List<DocumentLink> links = new ArrayList();

	public Date getOpdatetime() {
		return opdatetime;
	}

	public void setOpdatetime(Date opdatetime) {
		this.opdatetime = opdatetime;
	}

	public String getCatalog_id() {
		return catalog_id;
	}

	public void setCatalog_id(String catalog_id) {
		this.catalog_id = catalog_id;
	}

	public String getFile_id() {
		return file_id;
	}

	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String realFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public List<DocumentLink> getLinks() {
		links.add(new DocumentLink(DocumentLink.LinkID.view,"/api/v1/document/"+this.file_id+"/view"));
		links.add(new DocumentLink(DocumentLink.LinkID.info,"/api/v1/document/"+this.file_id+"/info"));
		links.add(new DocumentLink(DocumentLink.LinkID.download,"/api/v1/document/"+this.file_id+"/download"));
		return links;
	}
}
