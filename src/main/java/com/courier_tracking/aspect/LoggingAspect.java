package com.courier_tracking.aspect;

import com.courier_tracking.aspect.annotation.LogOperation;
import com.courier_tracking.controller.request.CourierRequest;
import com.courier_tracking.entity.CourierLog;
import com.courier_tracking.entity.Store;
import com.courier_tracking.model.BusinessException;
import com.courier_tracking.service.LogService;
import com.courier_tracking.service.StoreService;
import com.courier_tracking.service.UtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LoggingAspect {
    private final LogService service;

    private final UtilService utilService;
    private final StoreService storeService;

    /** every courier request is being logged in this layer.
     * if that is not the first request that courier sent, distance will be calculated and added to new log.
     * if courier is any closer than 100 m to any store
     * and if courier was not logged for same store any shorter than 1 min,
     * that will be added to the log.
     * */
    @Around("@annotation(logOperation)")
    public Object logOperation(ProceedingJoinPoint joinPoint, LogOperation logOperation) throws Throwable {
        Object result = joinPoint.proceed();

        try {
            double lat = Double.parseDouble(resolveExpression(logOperation.lat(), joinPoint));
            double lon = Double.parseDouble(resolveExpression(logOperation.lon(), joinPoint));
            long courierId = Long.parseLong(resolveExpression(logOperation.courierId(), joinPoint));
            String resolvedTimestamp = resolveExpression(logOperation.timeStamp(), joinPoint);
            LocalDateTime timestamp = LocalDateTime.parse(resolvedTimestamp);

            Optional<CourierLog> optionalCourierLog = service.getLastLogByCourier(courierId);

            //calculation distance
            double distance = optionalCourierLog.map(CourierLog::getDistance).orElse(0.0);

            //calculating dist to each store and check if it is already logged for same store
            List<Store> stores = getStoresInRange(lat, lon, courierId, timestamp);

            if (stores.isEmpty()){
                service.saveNewCourierLog(lat, lon, courierId, distance, timestamp, null);
            } else {
                stores.forEach(s -> service.saveNewCourierLog(lat, lon, courierId, distance, timestamp, s));
            }
        } catch (Exception e) {
            log.error("Error during logging operation: {}", e.getMessage());
            throw BusinessException.generalError();
        }

        return result;
    }

    private List<Store> getStoresInRange(double lat, double lon, long courierId, LocalDateTime timestamp) {
        List<Store> stores = storeService.getAll();

        stores.removeIf(s -> {
            double distanceToStore = utilService.calculateDistance(s.getLat(), s.getLon(), lat, lon);
            if (distanceToStore > 0.1) {
                return true;
            } else {
                Optional<CourierLog> optionalLog = service.getLogByCourierAndStoreAndTimestamp(courierId, s, timestamp.minusMinutes(1));
                return optionalLog.isPresent();
            }
        });

        return stores;
    }

    private String resolveExpression(String expression, ProceedingJoinPoint joinPoint) {
        if (!expression.startsWith("#")) {
            return expression;
        }

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            context.setVariable("request", args[0]);
        }

        try {
            Object value = parser.parseExpression(expression).getValue(context);
            return value.toString();
        } catch (Exception e) {
            throw BusinessException.generalError();
        }
    }
} 