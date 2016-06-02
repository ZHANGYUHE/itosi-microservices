package org.iplatform.microservices.support.authserver.bean;

import org.codehaus.jackson.annotate.JsonProperty;

public class AuthServiceToken {
	@JsonProperty
	public String access_token;
	@JsonProperty
	public String token_type;
	@JsonProperty
	public String refresh_token;
	@JsonProperty
	public String expires_in;
	@JsonProperty
	public String scope;
}
