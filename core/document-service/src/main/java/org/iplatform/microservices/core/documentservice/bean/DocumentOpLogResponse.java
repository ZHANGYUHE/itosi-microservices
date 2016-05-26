package org.iplatform.microservices.core.documentservice.bean;

import java.util.List;

public class DocumentOpLogResponse {
	private List<DocumentOpLogDO> documentOpLogs;
	private Boolean success = Boolean.TRUE;
	private String message;

	public List<DocumentOpLogDO> getDocumentOpLogs() {
		return documentOpLogs;
	}

	public void setDocumentOpLogs(List<DocumentOpLogDO> documentOpLogs) {
		this.documentOpLogs = documentOpLogs;
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
