package org.iplatform.microservices.core.documentservice.bean;

public class DocumentBody {
	private Document document;
	private Boolean success = Boolean.TRUE;
	private String message;

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Boolean getSuccess() {
		return success;
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
