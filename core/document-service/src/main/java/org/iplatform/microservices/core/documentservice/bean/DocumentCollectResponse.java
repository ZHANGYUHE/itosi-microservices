package org.iplatform.microservices.core.documentservice.bean;

public class DocumentCollectResponse {
	private DocumentCollectDO document;
	private Boolean success = Boolean.TRUE;
	private String message;
	public DocumentCollectDO getDocument() {
		return document;
	}
	public void setDocument(DocumentCollectDO document) {
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
