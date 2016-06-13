package org.iplatform.microservices.api.product;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.discovery.DiscoveryManager;

@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@EnableResourceServer
@RestController
public class ProductApiServiceApplication {

    private static final Logger LOG = LoggerFactory.getLogger(ProductApiServiceApplication.class);

    static {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);
    }
    
	public static void main(String[] args) {
		SpringApplication.run(ProductApiServiceApplication.class, args);
        LOG.info("Register ShutdownHook");
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                LOG.info("Shutting down, unregister from Eureka!");
                DiscoveryManager.getInstance().shutdownComponent();
            }
        });		
	}
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	@RequestMapping("ribbonHello")
    public String ribbonHello() {
		String url = null;
		List<ServiceInstance> list = discoveryClient.getInstances("document-service");
		if (list != null && list.size() > 0 ) {
	        url = list.get(0).getUri().toASCIIString();
	    }
        return restTemplate.getForEntity(url+"/documentservice/api/v1/document/me", String.class).getBody();
    }	
}
