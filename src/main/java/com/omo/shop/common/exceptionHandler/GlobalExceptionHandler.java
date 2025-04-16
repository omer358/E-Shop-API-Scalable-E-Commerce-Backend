package com.omo.shop.common.exceptionHandler;

import com.omo.shop.common.constants.ExceptionMessages;
import com.omo.shop.common.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        ApiResponse response = new ApiResponse(ExceptionMessages.TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

}
