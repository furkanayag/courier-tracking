package com.courier_tracking.service;

import com.courier_tracking.aspect.annotation.LogOperation;
import com.courier_tracking.controller.request.CourierRequest;
import com.courier_tracking.entity.CourierLog;
import com.courier_tracking.entity.Store;
import com.courier_tracking.repository.CourierLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogService {
    private final CourierLogRepository repository;
    private final UtilService utilService;

    public Optional<CourierLog> getLastLogByCourier(Long courierId){
        return repository.findTopByCourierIdOrderByTimestampDesc(courierId);
    }

    public Optional<CourierLog> getLogByCourierAndStoreAndTimestamp(Long courierId, Store store, LocalDateTime localDateTime){
        return repository.findCourierLogByCourierIdAndStoreAndTimestampAfter(courierId, store, localDateTime);
    }

    public void saveNewCourierLog(double lat, double lon, long courierId, double distance, LocalDateTime timestamp, Store store){
        CourierLog log = CourierLog.builder()
                .lat(lat)
                .lon(lon)
                .courierId(courierId)
                .distance(distance)
                .store(store)
                .timestamp(timestamp)
                .build();

        repository.save(log);
    }
}
