package org.iplatform.microservices.core.documentservice.bean;

import java.sql.Date;

public class PersonDO {
	private String uuid;
	private String username;
	private String truename;
	private Date opdatetime;
	private String password;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getOpdatetime() {
		return opdatetime;
	}
	public void setOpdatetime(Date opdatetime) {
		this.opdatetime = opdatetime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTruename() {
		return truename;
	}
	public void setTruename(String truename) {
		this.truename = truename;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}