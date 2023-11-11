package com.sweethome.bookingService.exceptions;


import com.sweethome.bookingService.dto.CustomeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<CustomeResponse> handleCustomException(Exception ex){
        CustomeResponse exceptionResponse = new CustomeResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<CustomeResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
