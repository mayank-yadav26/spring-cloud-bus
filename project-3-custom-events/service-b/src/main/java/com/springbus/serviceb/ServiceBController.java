package com.springbus.serviceb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceBController {

    @GetMapping("/health")
    public String health() {
        return "Service B (Listener) is running!";
    }

    @GetMapping("/info")
    public String info() {
        return "Project 3: Service B - Event Listener (check logs for received events)";
    }
}
