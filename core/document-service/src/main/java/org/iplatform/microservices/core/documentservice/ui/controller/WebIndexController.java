package org.iplatform.microservices.core.documentservice.ui.controller;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.iplatform.microservices.core.documentservice.bean.AuthServiceToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Controller
public class WebIndexController {

	@Autowired
	private RestOperations restTemplate;
	
	private static final Logger LOG = LoggerFactory.getLogger(WebIndexController.class);
	ObjectMapper objectMapper = new ObjectMapper();

	@RequestMapping("/login")
	public String index(ModelMap map) throws Exception {
		try {
			//此处通过用户名密码获取access_token然后返回页面
			String url = "https://localhost:9999/api/oauth/token?username=admin&password=admin&grant_type=password";
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			String auth = "itosiapp:secret";
			byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
			headers.add("Authorization", "Basic " + new String(encodedAuth));
			ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(null, headers),
					String.class);
			if (result.getStatusCode() == HttpStatus.OK) {
				AuthServiceToken token = objectMapper.readValue(result.getBody(), AuthServiceToken.class);
				map.addAttribute("access_token", token.access_token);
			}
			return "index";
		} catch (Exception e) {
			LOG.error("", e);
			throw new Exception(e);
		}
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}