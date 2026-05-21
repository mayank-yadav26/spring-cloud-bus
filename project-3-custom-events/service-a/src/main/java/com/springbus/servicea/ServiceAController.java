package com.springbus.servicea;

import com.springbus.shared.NotificationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceAController {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @PostMapping("/notify")
    public String sendNotification(@RequestParam String message) {
        System.out.println("[SERVICE A] Publishing notification: " + message);
        NotificationEvent event = new NotificationEvent(this, "service-a", null, message);
        eventPublisher.publishEvent(event);
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
