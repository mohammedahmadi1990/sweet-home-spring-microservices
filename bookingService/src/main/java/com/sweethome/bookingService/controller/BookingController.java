package com.sweethome.bookingService.controller;


import com.sweethome.bookingService.dto.BookingDto;
import com.sweethome.bookingService.dto.PaymentDto;
import com.sweethome.bookingService.entity.BookingInfoEntity;
import com.sweethome.bookingService.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingController {
    @Autowired
    BookingService bookingService;
    
    @PostMapping("/booking")
    public ResponseEntity<BookingInfoEntity> bookingDetails(@RequestBody BookingDto bookingRequest) throws Exception{
        BookingInfoEntity bookingInfo = this.bookingService.bookingDetails(bookingRequest);
        return new ResponseEntity<BookingInfoEntity>(bookingInfo, HttpStatus.CREATED);
    }

    @PostMapping("/booking/{bookingId}/transction")
    public ResponseEntity<BookingInfoEntity> doPayment(@RequestBody PaymentDto paymentDetails) throws Exception{
        BookingInfoEntity bookingInfo = bookingService.doPayment(paymentDetails);
        return new ResponseEntity<BookingInfoEntity>(bookingInfo, HttpStatus.CREATED);
    }
}
