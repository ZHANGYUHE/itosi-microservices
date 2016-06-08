package org.iplatform.microservices.support.authserver.controller;

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.iplatform.microservices.support.authserver.bean.AuthServiceToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Controller
public class HelloWorldController {

	@Autowired
	private RestOperations restTemplate;
	private static final Logger LOG = LoggerFactory.getLogger(HelloWorldController.class);
	ObjectMapper objectMapper = new ObjectMapper();

	@ResponseBody
	@RequestMapping("/hello")
	public String sayHello() {
		return "Hello User!";
	}

	@RequestMapping("/")
	public String register(ModelMap map) throws Exception {		
		return "login";
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(@RequestParam(value = "username") String username,@RequestParam(value = "password") String password,@RequestParam(value = "captcha") String captcha,ModelMap map,HttpServletRequest request) throws Exception {
		try {
			String kaptchaExpected = (String)request.getSession().getAttribute("verify");
			if(kaptchaExpected.equals(captcha)){
				try{
					//此处通过用户名密码获取access_token然后返回页面
					String url = "https://localhost:9999/api/oauth/token?username="+username+"&password="+password+"&grant_type=password";
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
					map.addAttribute("message", "验证码无效或者用户名密码错误");	
					return "login";
				}				
			}else{
				map.addAttribute("message", "验证码无效或者用户名密码错误");
				return "login";
			}
			
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
