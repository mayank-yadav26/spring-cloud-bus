package com.springbus.serviceb;

import com.springbus.shared.NotificationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    @EventListener
    public void handleNotificationEvent(NotificationEvent event) {
        System.out.println("[SERVICE B] Received NotificationEvent: " + event);
        System.out.println("[SERVICE B] Message: " + event.getMessage());
        System.out.println("[SERVICE B] From: " + event.getOriginService());
        System.out.println("[SERVICE B] Event Time: " + event.getEventTime());
    }
}
