package com.courier_tracking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class
BusinessException extends RuntimeException{

    private final HttpStatus httpStatus;
    private final String errorMessage;

    public static BusinessException generalError(){ return new BusinessException(HttpStatus.BAD_REQUEST, "Exception encountered. Please check logs.");}
    public static BusinessException initializingStores(){ return new BusinessException(HttpStatus.BAD_REQUEST, "Exception encountered while initializing stores");}



}
