package org.iplatform.microservices.support.authserver;

import java.security.Principal;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@EnableEurekaClient
@Controller
public class SpringBootOauth2Application {
	
    static {
        // for localhost testing only        
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);
    }	
    
	@RequestMapping("/user")
	@ResponseBody
	public Principal user(Principal user) {
		return user;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootOauth2Application.class, args);
	}
}
