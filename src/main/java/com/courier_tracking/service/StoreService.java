package com.courier_tracking.service;

import com.courier_tracking.entity.Store;
import com.courier_tracking.model.BusinessException;
import com.courier_tracking.repository.StoreRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoreService {

    private final ObjectMapper objectMapper;
    private final StoreRepository repository;

    /** Right after jar is up, stores will be uploaded to the database from .json for further usages.
     * A caching system such as redis,
     * would be more idealistic solution in a real word scenario since Migros has thousands of stores over Turkey.*/
    @PostConstruct
    public void init() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("static/stores.json");
            List<Store> stores = objectMapper.readValue(inputStream, new TypeReference<>(){});
            repository.saveAll(stores);
        } catch (Exception e){
            log.error("Exception encountered while initializing stores. {}", e.getMessage());
            throw BusinessException.initializingStores();
        }
    }

    /** In this part 2 approach could be used,
     * First one -> data could be filtered while fetching to get stores that is max 100m away from our courier.
     * Second one -> all data will be fetched and filtering is going to be on Java.
     * Both approach have pros and cons. I will use second one here for problem specific reasons.
     * */
    public List<Store> getAll(){
        return repository.findAll();
    }
}
