package com.courier_tracking.repository;

import com.courier_tracking.entity.CourierLog;
import com.courier_tracking.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CourierLogRepository extends JpaRepository<CourierLog,Long>{
    Optional<CourierLog> findTopByCourierIdOrderByTimestampDesc(Long courierId);
    Optional<CourierLog> findCourierLogByCourierIdAndStoreAndTimestampAfter(Long courierId, Store store, LocalDateTime timestamp);
}
