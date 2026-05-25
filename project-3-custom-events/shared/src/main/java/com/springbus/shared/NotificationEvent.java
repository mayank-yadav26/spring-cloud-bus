package com.springbus.shared;

import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * Custom remote event for Spring Cloud Bus - Approach 1 (Recommended)
 * This event extends RemoteApplicationEvent and will be automatically
 * intercepted and forwarded by Spring Cloud Bus to all connected services.
 * 
 * When a service publishes this event via ApplicationEventPublisher,
 * Spring Cloud Bus listens for it and broadcasts it to the message broker
 * (RabbitMQ/Kafka) to be distributed to other services.
 */
public class NotificationEvent extends RemoteApplicationEvent {

    private String message;
    private long eventTime;

    // Default constructor required for deserialization by Spring Cloud Bus
    public NotificationEvent() {
        super();
    }

    // Constructor for publishing the event
    @SuppressWarnings("deprecation")
    public NotificationEvent(Object source, String originService, String destinationService, String message) {
        super(source, originService, destinationService);
        this.message = message;
        this.eventTime = System.currentTimeMillis();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public String toString() {
        return "NotificationEvent{" +
                "message='" + message + '\'' +
                ", eventTime=" + eventTime +
                ", originService='" + getOriginService() + '\'' +
                '}';
    }
}
