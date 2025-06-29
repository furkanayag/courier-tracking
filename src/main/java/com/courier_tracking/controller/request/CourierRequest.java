package com.courier_tracking.controller.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourierRequest {
    private long courierId;
    private double lat;
    private double lon;
    private LocalDateTime timestamp;
}
