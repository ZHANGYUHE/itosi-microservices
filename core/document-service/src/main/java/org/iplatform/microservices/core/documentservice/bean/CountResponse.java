package org.iplatform.microservices.core.documentservice.bean;

public class CountResponse {
	private Integer count;
	private Boolean success = Boolean.TRUE;
	private String message;

	public Boolean getSuccess() {
		return success;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
