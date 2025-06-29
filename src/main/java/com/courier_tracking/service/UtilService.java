package com.courier_tracking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UtilService {
    public double calculateDistance(double firstLat, double firstLon, double lastLat, double lastLon){
        return Math.sqrt(Math.pow(lastLat - firstLat, 2) + Math.pow(lastLon - firstLon, 2));
    }
}
