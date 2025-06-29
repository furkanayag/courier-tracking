package com.courier_tracking.controller;

import com.courier_tracking.controller.request.CourierRequest;
import com.courier_tracking.controller.response.DistanceResponse;
import com.courier_tracking.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courier")
@RequiredArgsConstructor
public class CourierController {
    private final CourierService service;

    /**
     * Find total distance courier ever had.
     */
    @GetMapping("/total-distance")
    public ResponseEntity<DistanceResponse> getDistance(@RequestParam Long courierId){
        DistanceResponse response = service.getDistance(courierId);
        return ResponseEntity.ok(response);
    }

    /**
     * Every new location request is logged aspect oriented way by LogOperation aspect.
     */
    @PostMapping("/location")
    public ResponseEntity<Void> newLocation(@RequestBody CourierRequest request){
        service.saveCourierLocation(request);
        return ResponseEntity.ok().build();
    }
}
