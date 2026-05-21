package com.springbus.foundation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusController {

    @GetMapping("/health")
    public String health() {
        return "Bus Foundation App is running!";
    }

    @PostMapping("/test-message")
    public String sendTestMessage() {
        System.out.println("Test message endpoint triggered!");
        return "Message sent to bus";
    }

    @GetMapping("/info")
    public String info() {
        return "Project 1: Bus Foundation & RabbitMQ Setup";
    }
}
