package com.courier_tracking.service;

import com.courier_tracking.aspect.annotation.LogOperation;
import com.courier_tracking.controller.request.CourierRequest;
import com.courier_tracking.controller.response.DistanceResponse;
import com.courier_tracking.entity.CourierLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourierService {
    private final LogService logService;

    public DistanceResponse getDistance(Long courierId){
        Optional<CourierLog> courierLog =  logService.getLastLogByCourier(courierId);
        return new DistanceResponse(courierLog.map(CourierLog::getDistance).orElse(0.0));
    }

    @LogOperation(
            lat = "#request.lat",
            lon = "#request.lon",
            courierId = "#request.courierId",
            timeStamp = "#request.timestamp"
    )
    public void saveCourierLocation(CourierRequest request){
        // Method body is intentionally empty as it is handled by the aspect
    }
}
