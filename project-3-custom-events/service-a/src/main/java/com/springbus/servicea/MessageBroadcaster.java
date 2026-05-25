package com.springbus.servicea;

import com.springbus.shared.NotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * MessageBroadcaster publishes NotificationEvent through Spring Cloud Bus.
 * Spring Cloud Bus automatically intercepts RemoteApplicationEvent instances
 * and forwards them via RabbitMQ to all connected services.
 */
@Slf4j
@Service
public class MessageBroadcaster {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private BusProperties busProperties;

    public void broadcastNotification(String message) {
        try {
            log.info("[SERVICE A] MessageBroadcaster: Publishing notification: {}", message);
            
            // Create RemoteApplicationEvent - Spring Cloud Bus will forward this to other services
            NotificationEvent event = new NotificationEvent(
                    this,
                    busProperties.getId(),  // Origin service (auto-detected by bus)
                    "*",                     // Destination: "*" means broadcast to all services
                    message
            );

            log.info("[SERVICE A] MessageBroadcaster: Publishing event via Spring Cloud Bus");
            eventPublisher.publishEvent(event);
            log.info("[SERVICE A] MessageBroadcaster: Event published successfully");
        } catch (Exception e) {
            log.error("[SERVICE A] MessageBroadcaster: Error publishing notification", e);
            throw new RuntimeException("Failed to broadcast notification", e);
        }
    }
}
