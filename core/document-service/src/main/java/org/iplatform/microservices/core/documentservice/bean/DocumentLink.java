package org.iplatform.microservices.core.documentservice.bean;

public class DocumentLink {
	private LinkID id;
	private String url;

	public LinkID getId() {
		return id;
	}
	public void setId(LinkID id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public DocumentLink(LinkID id, String url) {
		super();
		this.id = id;
		this.url = url;
	}

	public enum LinkID{  
	    view,info,download
	} 	
}
