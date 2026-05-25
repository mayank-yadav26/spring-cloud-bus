package com.springbus.servicea;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ServiceAController {

    @Autowired
    private MessageBroadcaster messageBroadcaster;

    @PostMapping("/notify")
    public String sendNotification(@RequestParam String message) {
        log.info("[SERVICE A] Controller: Received request to send notification: {}", message);
        messageBroadcaster.broadcastNotification(message);
        return "Notification sent: " + message;
    }

    @GetMapping("/health")
    public String health() {
        return "Service A (Publisher) is running!";
    }

    @GetMapping("/info")
    public String info() {
        return "Project 3: Service A - Event Publisher";
    }
}
