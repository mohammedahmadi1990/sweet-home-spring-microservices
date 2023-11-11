package com.sweethome.paymentService.service;

import com.sweethome.paymentService.dao.PaymentDetailsDao;
import com.sweethome.paymentService.entity.PaymentDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {
    PaymentDetailsDao paymentDetailsDao;

    @Autowired
    public PaymentService(PaymentDetailsDao paymentDetailsDao){
        super();
        this.paymentDetailsDao = paymentDetailsDao;
    }

    public int makePayment(PaymentDetailsEntity paymentRequest){
        return this.paymentDetailsDao.save(paymentRequest).getId();
    }

    public PaymentDetailsEntity getPaymentById(int paymentId) throws Exception{
        Optional<PaymentDetailsEntity> paymentDetailsDaoOp = this.paymentDetailsDao.findById(paymentId);
        if(paymentDetailsDaoOp.isPresent()){
            return paymentDetailsDaoOp.get();
        }else{
            throw new Exception("Invalid payment ID.");
        }
    }

}
