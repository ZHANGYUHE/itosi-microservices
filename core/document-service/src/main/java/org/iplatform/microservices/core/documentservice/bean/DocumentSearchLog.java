package org.iplatform.microservices.core.documentservice.bean;

import java.sql.Date;

public class DocumentSearchLog {
	private String uuid;
	private String operater;
	private String searchparam;
	private Date opdatetime;
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
	public String getSearchparam() {
		return searchparam;
	}
	public void setSearchparam(String searchparam) {
		this.searchparam = searchparam;
	}
	public Date getOpdatetime() {
		return opdatetime;
	}
	public void setOpdatetime(Date opdatetime) {
		this.opdatetime = opdatetime;
	}
}
