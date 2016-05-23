package org.iplatform.microservices.core.documentservice.bean;

import java.sql.Date;

public class DocumentOpLogDO {
	private String file_id;
	private String uuid;
	private String operater;	
	private String optype;
	private Date opdatetime;
	public String getFile_id() {
		return file_id;
	}
	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getOperater() {
		return operater;
	}
	public void setOperater(String operater) {
		this.operater = operater;
	}
	public String getOptype() {
		return optype;
	}
	public void setOptype(String optype) {
		this.optype = optype;
	}
	public Date getOpdatetime() {
		return opdatetime;
	}
	public void setOpdatetime(Date opdatetime) {
		this.opdatetime = opdatetime;
	}
}
