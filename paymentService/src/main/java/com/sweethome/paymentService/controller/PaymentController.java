package com.sweethome.paymentService.controller;

import com.sweethome.paymentService.entity.PaymentDetailsEntity;
import com.sweethome.paymentService.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class PaymentController {
    PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService){
        super();
        this.paymentService = paymentService;
    }

    @PostMapping("/transaction")
    public ResponseEntity<Integer> makePayment(@RequestBody PaymentDetailsEntity paymentDetails){
        int paymentId = this.paymentService.makePayment(paymentDetails);
        return new ResponseEntity<Integer>(paymentId, HttpStatus.CREATED);
    }

    @GetMapping("/transaction/{transactionId")
    public ResponseEntity<PaymentDetailsEntity> getpaymentById(@PathVariable int transactionId) throws Exception {
        PaymentDetailsEntity paymentDetails = this.paymentService.getPaymentById(transactionId);
        return new ResponseEntity<PaymentDetailsEntity>(paymentDetails, HttpStatus.OK);
    }
}
