package com.springbus.serviceb;

import com.springbus.shared.NotificationEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;

@SpringBootApplication(scanBasePackages = {"com.springbus.serviceb", "com.springbus.shared"})
@RemoteApplicationEventScan(basePackageClasses = NotificationEvent.class)
public class ServiceBApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceBApplication.class, args);
    }
}
