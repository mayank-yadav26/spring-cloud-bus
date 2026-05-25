package com.springbus.serviceb;

import com.springbus.shared.NotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationListener {

    // @EventListener automatically works with RemoteApplicationEvent via Spring Cloud Bus
    // When a RemoteApplicationEvent is published on the bus, it's automatically delivered
    // to all listening instances
    @EventListener
    public void handleNotificationEvent(NotificationEvent event) {
        log.info("========================================");
        log.info("[SERVICE B] Received NotificationEvent via Spring Cloud Bus!");
        log.info("[SERVICE B] Message: {}", event.getMessage());
        log.info("[SERVICE B] From Service: {}", event.getOriginService());
        log.info("[SERVICE B] Destination: {}", event.getDestinationService());
        log.info("[SERVICE B] Event Timestamp: {}", event.getEventTime());
        log.info("[SERVICE B] Event ID: {}", event.getId());
        log.info("[SERVICE B] Event fully processed");
        log.info("========================================");
    }
}
