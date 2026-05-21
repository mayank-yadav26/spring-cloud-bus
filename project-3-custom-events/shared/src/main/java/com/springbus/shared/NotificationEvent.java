package com.springbus.shared;

import org.springframework.cloud.bus.event.RemoteApplicationEvent;

public class NotificationEvent extends RemoteApplicationEvent {

    private String message;
    private long eventTime;

    public NotificationEvent() {
    }

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
