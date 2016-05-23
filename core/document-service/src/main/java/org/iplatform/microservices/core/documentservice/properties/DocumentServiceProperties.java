package org.iplatform.microservices.core.documentservice.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "document-service",locations = "classpath:document-service.yml") 
public class DocumentServiceProperties {
	String paperpath;
	String documentpath;
	public String getPaperpath() {
		return paperpath;
	}
	public void setPaperpath(String paperpath) {
		this.paperpath = paperpath;
	}
	public String getDocumentpath() {
		return documentpath;
	}
	public void setDocumentpath(String documentpath) {
		this.documentpath = documentpath;
	}
}
