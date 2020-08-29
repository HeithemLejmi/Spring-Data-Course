package com.pluralsight.coursespringdataoverview.entity;

import java.time.LocalDateTime;

public class Flight {
    private String id;

    private String origin;
    private String destination;
    private LocalDateTime scheduleAt;

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getScheduleAt() {
        return scheduleAt;
    }

    public String getId(){
        return id;
    }


    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setScheduleAt(LocalDateTime scheduleAt) {
        this.scheduleAt = scheduleAt;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", scheduleAt=" + scheduleAt +
                '}';
    }
}
