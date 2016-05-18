package org.iplatform.microservices.support.configserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigServerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
