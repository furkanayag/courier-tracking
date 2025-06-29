package com.courier_tracking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class UtilServiceTest {

    private UtilService utilService;

    @BeforeEach
    void setUp() {
        utilService = new UtilService();
    }

    @Test
    void calculateDistance_WithValidCoordinates_ShouldReturnCorrectDistance() {
        double firstLat = 40.7128;
        double firstLon = 74.0060;
        double lastLat = 40.7589;
        double lastLon = -73.9851;

        double distance = utilService.calculateDistance(firstLat, firstLon, lastLat, lastLon);

        assertThat(distance).isGreaterThan(0);
        assertThat(distance).isEqualTo(Math.sqrt(Math.pow(lastLat - firstLat, 2) + Math.pow(lastLon - firstLon, 2)));
    }

    @Test
    void calculateDistance_WithSameCoordinates_ShouldReturnZero() {
        double lat = 40.7128;
        double lon = -74.0060;

        double distance = utilService.calculateDistance(lat, lon, lat, lon);

        assertThat(distance).isEqualTo(0.0);
    }
} 