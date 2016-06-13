package org.iplatform.microservices.support.discoveryserver;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
@EnableDiscoveryClient
public class DiscoveryServerApplication {

    static {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);
    }
    
	public static void main(String[] args) {
		SpringApplication.run(DiscoveryServerApplication.class, args);
	}
}
