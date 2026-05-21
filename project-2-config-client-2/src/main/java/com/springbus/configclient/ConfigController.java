package com.springbus.configclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {

    @Autowired
    private AppConfig appConfig;

    @GetMapping("/config")
    public ConfigResponse getConfig() {
        System.out.println("Current config - Message: " + appConfig.getMessage() + ", Version: " + appConfig.getVersion());
        return new ConfigResponse(appConfig.getMessage(), appConfig.getVersion());
    }

    @GetMapping("/health")
    public String health() {
        return "Config Client 2 is running!";
    }
}
