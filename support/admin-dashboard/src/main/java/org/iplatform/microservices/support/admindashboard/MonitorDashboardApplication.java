package org.iplatform.microservices.support.admindashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import de.codecentric.boot.admin.config.EnableAdminServer;

@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
public class MonitorDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitorDashboardApplication.class, args);
	}
}
