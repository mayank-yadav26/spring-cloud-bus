package com.springbus.configclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ConfigController {

    @Autowired
    private AppConfig appConfig;

    @GetMapping("/config")
    public ConfigResponse getConfig() {
        log.info("Current config - Message: {}, Version: {}", appConfig.getMessage(), appConfig.getVersion());
        return new ConfigResponse(appConfig.getMessage(), appConfig.getVersion());
    }

    @GetMapping("/health")
    public String health() {
        return "Config Client 1 is running!";
    }
}
