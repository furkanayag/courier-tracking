package com.courier_tracking.service;

import com.courier_tracking.controller.request.CourierRequest;
import com.courier_tracking.controller.response.DistanceResponse;
import com.courier_tracking.entity.CourierLog;
import com.courier_tracking.entity.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

    @Mock
    private LogService logService;

    @InjectMocks
    private CourierService courierService;

    private CourierLog sampleCourierLog;
    private CourierRequest sampleRequest;

    @BeforeEach
    void setUp() {
        Store store = new Store("Test Store", 40.7128, -74.0060);

        sampleCourierLog = CourierLog.builder()
                .id(1L)
                .courierId(123L)
                .lat(40.7128)
                .lon(-74.0060)
                .distance(150.5)
                .store(store)
                .timestamp(LocalDateTime.now())
                .build();

        sampleRequest = new CourierRequest();
        sampleRequest.setCourierId(123L);
        sampleRequest.setLat(40.7128);
        sampleRequest.setLon(-74.0060);
        sampleRequest.setTimestamp(LocalDateTime.now());
    }

    @Test
    void getDistance_WhenCourierHasLogs_ShouldReturnTotalDistance() {
        Long courierId = 123L;
        when(logService.getLastLogByCourier(courierId))
                .thenReturn(Optional.of(sampleCourierLog));

        DistanceResponse response = courierService.getDistance(courierId);

        assertThat(response).isNotNull();
        assertThat(response.getDistance()).isEqualTo(150.5);
    }

    @Test
    void getDistance_WhenCourierHasNoLogs_ShouldReturnZeroDistance() {
        Long courierId = 123L;
        when(logService.getLastLogByCourier(courierId))
                .thenReturn(Optional.empty());

        DistanceResponse response = courierService.getDistance(courierId);

        assertThat(response).isNotNull();
        assertThat(response.getDistance()).isEqualTo(0.0);
    }

    @Test
    void getDistance_WithNullCourierId_ShouldHandleGracefully() {
        when(logService.getLastLogByCourier(null))
                .thenReturn(Optional.empty());

        DistanceResponse response = courierService.getDistance(null);

        assertThat(response).isNotNull();
        assertThat(response.getDistance()).isEqualTo(0.0);
    }
} 