package org.iplatform.microservices.core.documentservice.bean;

import java.util.List;

public class DocumentResponse {
	private DocumentDO document;
	private Boolean success = Boolean.TRUE;
	private String message;

	public DocumentDO getDocument() {
		return document;
	}

	public void setDocument(DocumentDO document) {
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
