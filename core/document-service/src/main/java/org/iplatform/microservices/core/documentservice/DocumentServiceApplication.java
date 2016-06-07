package org.iplatform.microservices.core.documentservice;

import javax.net.ssl.HttpsURLConnection;

import org.iplatform.microservices.core.documentservice.properties.DocumentServiceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import com.netflix.discovery.DiscoveryManager;

@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@EnableResourceServer
@EnableConfigurationProperties({ DocumentServiceProperties.class })
public class DocumentServiceApplication {
	private static final Logger LOG = LoggerFactory.getLogger(DocumentServiceApplication.class);

    static {
        // for localhost testing only
        LOG.warn("Will now disable hostname check in SSL, only to be used during development");
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);
    }	

	public static void main(String[] args) {
		SpringApplication.run(DocumentServiceApplication.class, args);
		
		LOG.info("Register ShutdownHook");
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                LOG.info("Shutting down, unregister from Eureka!");                
                DiscoveryManager.getInstance().shutdownComponent();
            }
        });		
	}
}
