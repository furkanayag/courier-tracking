package com.courier_tracking.controller;

import com.courier_tracking.controller.request.CourierRequest;
import com.courier_tracking.controller.response.DistanceResponse;
import com.courier_tracking.service.CourierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class CourierControllerTest {

    @Mock
    private CourierService courierService;

    @InjectMocks
    private CourierController courierController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(courierController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getDistance_WithValidCourierId_ShouldReturnDistanceResponse() throws Exception {
        Long courierId = 123L;
        DistanceResponse expectedResponse = new DistanceResponse(150.5);
        when(courierService.getDistance(courierId)).thenReturn(expectedResponse);

        mockMvc.perform(get("/courier/total-distance")
                        .param("courierId", courierId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.distance").value(150.5));
    }

    @Test
    void getDistance_WithInvalidCourierId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/courier/total-distance")
                        .param("courierId", "invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDistance_WithNegativeCourierId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/courier/total-distance")
                        .param("courierId", "-123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void newLocation_WithValidRequest_ShouldReturnOk() throws Exception {
        CourierRequest request = new CourierRequest();
        request.setCourierId(123L);
        request.setLat(40.7128);
        request.setLon(-74.0060);
        request.setTimestamp(LocalDateTime.of(2024, 1, 15, 14, 30, 0));

        mockMvc.perform(post("/courier/location")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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
    void newLocation_WithMissingRequiredFields_ShouldReturnBadRequest() throws Exception {
        CourierRequest request = new CourierRequest();

        mockMvc.perform(post("/courier/location")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
} 