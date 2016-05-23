package org.iplatform.microservices.core.documentservice.bean;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DocumentDO {
	private String file_id;
	private String file_name;
	private String author;
	private String file_path;
	private Date opdatetime;
	private List<DocumentLink> links = new ArrayList();

	public Date getOpdatetime() {
		return opdatetime;
	}

	public void setOpdatetime(Date opdatetime) {
		this.opdatetime = opdatetime;
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
		links.add(new DocumentLink(DocumentLink.LinkID.view,"/document/"+this.author+"/"+this.file_id+"/view"));
		links.add(new DocumentLink(DocumentLink.LinkID.info,"/document/"+this.author+"/"+this.file_id+"/info"));
		links.add(new DocumentLink(DocumentLink.LinkID.download,"/document/"+this.author+"/"+this.file_id+"/download"));
		return links;
	}
}
