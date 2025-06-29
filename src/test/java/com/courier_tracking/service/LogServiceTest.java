package com.courier_tracking.service;

import com.courier_tracking.entity.CourierLog;
import com.courier_tracking.entity.Store;
import com.courier_tracking.repository.CourierLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {

    @Mock
    private CourierLogRepository repository;

    @Mock
    private UtilService utilService;

    @InjectMocks
    private LogService logService;

    private Store testStore;
    private CourierLog testCourierLog;

    @BeforeEach
    void setUp() {
        testStore = new Store("Test Store", 40.7128, -74.0060);
        
        testCourierLog = CourierLog.builder()
                .id(1L)
                .courierId(123L)
                .lat(40.7128)
                .lon(-74.0060)
                .distance(150.5)
                .store(testStore)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    void getLastLogByCourier_WhenLogExists_ShouldReturnLog() {
        Long courierId = 123L;
        when(repository.findTopByCourierIdOrderByTimestampDesc(courierId))
                .thenReturn(Optional.of(testCourierLog));

        Optional<CourierLog> result = logService.getLastLogByCourier(courierId);

        assertThat(result).isPresent();
        assertThat(result.get().getCourierId()).isEqualTo(courierId);
        assertThat(result.get().getDistance()).isEqualTo(150.5);
    }

    @Test
    void getLastLogByCourier_WhenNoLogExists_ShouldReturnEmpty() {
        Long courierId = 999L;
        when(repository.findTopByCourierIdOrderByTimestampDesc(courierId))
                .thenReturn(Optional.empty());

        Optional<CourierLog> result = logService.getLastLogByCourier(courierId);

        assertThat(result).isEmpty();
    }

    @Test
    void getLastLogByCourier_WithNullCourierId_ShouldReturnEmpty() {
        when(repository.findTopByCourierIdOrderByTimestampDesc(null))
                .thenReturn(Optional.empty());

        Optional<CourierLog> result = logService.getLastLogByCourier(null);

        assertThat(result).isEmpty();
    }

    @Test
    void getLogByCourierAndStoreAndTimestamp_WhenLogExists_ShouldReturnLog() {
        Long courierId = 123L;
        LocalDateTime timestamp = LocalDateTime.now().minusHours(1);
        
        when(repository.findCourierLogByCourierIdAndStoreAndTimestampAfter(courierId, testStore, timestamp))
                .thenReturn(Optional.of(testCourierLog));

        Optional<CourierLog> result = logService.getLogByCourierAndStoreAndTimestamp(courierId, testStore, timestamp);

        assertThat(result).isPresent();
        assertThat(result.get().getCourierId()).isEqualTo(courierId);
        assertThat(result.get().getStore()).isEqualTo(testStore);
    }

    @Test
    void getLogByCourierAndStoreAndTimestamp_WhenNoLogExists_ShouldReturnEmpty() {
        Long courierId = 123L;
        LocalDateTime timestamp = LocalDateTime.now().minusHours(1);
        
        when(repository.findCourierLogByCourierIdAndStoreAndTimestampAfter(courierId, testStore, timestamp))
                .thenReturn(Optional.empty());

        Optional<CourierLog> result = logService.getLogByCourierAndStoreAndTimestamp(courierId, testStore, timestamp);

        assertThat(result).isEmpty();
    }

    @Test
    void saveNewCourierLog_ShouldSaveLogWithCorrectData() {
        double lat = 40.7128;
        double lon = 74.0060;
        long courierId = 123L;
        double distance = 150.5;
        LocalDateTime timestamp = LocalDateTime.now();

        when(repository.save(any(CourierLog.class))).thenReturn(testCourierLog);

        logService.saveNewCourierLog(lat, lon, courierId, distance, timestamp, testStore);

        verify(repository).save(any(CourierLog.class));
    }
} 