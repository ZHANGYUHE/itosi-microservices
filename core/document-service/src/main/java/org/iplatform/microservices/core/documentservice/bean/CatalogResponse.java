package org.iplatform.microservices.core.documentservice.bean;

import java.util.ArrayList;
import java.util.List;

public class CatalogResponse {
	private CatalogDO catalog;
	private Boolean success = Boolean.TRUE;
	private String message;
	private List<CatalogDO> catalogChilds = new ArrayList();
	private List<DocumentDO> documentChilds = new ArrayList();
	public CatalogDO getCatalog() {
		return catalog;
	}

	public void setCatalog(CatalogDO catalog) {
		this.catalog = catalog;
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

	public List<CatalogDO> getCatalogChilds() {
		return catalogChilds;
	}

	public void setCatalogChilds(List<CatalogDO> catalogChilds) {
		this.catalogChilds = catalogChilds;
	}

	public List<DocumentDO> getDocumentChilds() {
		return documentChilds;
	}

	public void setDocumentChilds(List<DocumentDO> documentChilds) {
		this.documentChilds = documentChilds;
	}
}
