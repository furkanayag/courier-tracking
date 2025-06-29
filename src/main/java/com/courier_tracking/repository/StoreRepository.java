package com.courier_tracking.repository;

import com.courier_tracking.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, String>{
    List<Store> findByLatBetweenAndLonBetween(double minLat, double maxLat, double minLon, double maxLon);
}
