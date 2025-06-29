package com.courier_tracking.controller.advice;

import com.courier_tracking.model.BusinessError;
import com.courier_tracking.model.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class CustomControllerAdvice {
    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handle(Exception e) {
        log.error("Exception encountered. {}", e.getMessage());
        String message = e instanceof BusinessException ? ((BusinessException) e).getErrorMessage() :
                "Exception encountered. Please check logs.";
        BusinessError businessError = new BusinessError(HttpStatus.BAD_REQUEST, e.getMessage(), message);
        return new ResponseEntity<>(businessError, new HttpHeaders(), businessError.getStatus());
    }
}
