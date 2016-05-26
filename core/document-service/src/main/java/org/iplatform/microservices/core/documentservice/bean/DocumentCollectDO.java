package org.iplatform.microservices.core.documentservice.bean;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DocumentCollectDO {
	private String file_id;
	private String collect_id;
	private String collect_author;
	private Boolean file_state;
	private Date opdatetime;
	public String getFile_id() {
		return file_id;
	}
	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}
	public String getCollect_id() {
		return collect_id;
	}
	public void setCollect_id(String collect_id) {
		this.collect_id = collect_id;
	}
	public String getCollect_author() {
		return collect_author;
	}
	public void setCollect_author(String collect_author) {
		this.collect_author = collect_author;
	}
	public Boolean getFile_state() {
		return file_state;
	}
	public void setFile_state(Boolean file_state) {
		this.file_state = file_state;
	}
	public Date getOpdatetime() {
		return opdatetime;
	}
	public void setOpdatetime(Date opdatetime) {
		this.opdatetime = opdatetime;
	}
}
