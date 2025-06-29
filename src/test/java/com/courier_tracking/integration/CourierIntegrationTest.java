package com.courier_tracking.integration;

import com.courier_tracking.controller.request.CourierRequest;
import com.courier_tracking.entity.CourierLog;
import com.courier_tracking.entity.Store;
import com.courier_tracking.repository.CourierLogRepository;
import com.courier_tracking.repository.StoreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@Testcontainers
@ActiveProfiles("test")
class CourierIntegrationTest {

    @Container
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("courier_test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CourierLogRepository courierLogRepository;

    @Autowired
    private StoreRepository storeRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();

        courierLogRepository.deleteAll();
        storeRepository.deleteAll();
    }

    @Test
    void getDistance_WithExistingCourierLogs_ShouldReturnTotalDistance() throws Exception {
        Store store = new Store("Test Store", 40.7128, -74.0060);
        storeRepository.save(store);

        CourierLog log1 = CourierLog.builder()
                .courierId(123L)
                .lat(40.7128)
                .lon(-74.0060)
                .distance(100.0)
                .store(store)
                .timestamp(LocalDateTime.now().minusHours(1))
                .build();
        courierLogRepository.save(log1);

        CourierLog log2 = CourierLog.builder()
                .courierId(123L)
                .lat(40.7589)
                .lon(-73.9851)
                .distance(250.0)
                .store(store)
                .timestamp(LocalDateTime.now())
                .build();
        courierLogRepository.save(log2);

        mockMvc.perform(get("/courier/total-distance")
                        .param("courierId", "123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.distance").value(250.0));
    }

    @Test
    void getDistance_WithNoCourierLogs_ShouldReturnZeroDistance() throws Exception {
        mockMvc.perform(get("/courier/total-distance")
                        .param("courierId", "999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.distance").value(0.0));
    }

    @Test
    void newLocation_WithValidRequest_ShouldLogLocation() throws Exception {
        CourierRequest request = new CourierRequest();
        request.setCourierId(123L);
        request.setLat(40.7128);
        request.setLon(-74.0060);
        request.setTimestamp(LocalDateTime.now());

        mockMvc.perform(post("/courier/location")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(courierLogRepository.findTopByCourierIdOrderByTimestampDesc(123L))
                .isPresent();
    }

    @Test
    void newLocation_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        String invalidJson = "{ invalid json }";

        mockMvc.perform(post("/courier/location")
                        .content(invalidJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDistance_WithInvalidCourierId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/courier/total-distance")
                        .param("courierId", "invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDistance_WithMissingCourierId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/courier/total-distance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void newLocation_WithMissingRequiredFields_ShouldReturnBadRequest() throws Exception {
        CourierRequest request = new CourierRequest();
        // Missing all required fields

        mockMvc.perform(post("/courier/location")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
} 