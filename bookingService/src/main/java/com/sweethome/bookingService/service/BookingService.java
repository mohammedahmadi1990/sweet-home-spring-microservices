package com.sweethome.bookingService.service;

import com.sweethome.bookingService.dao.BookingDao;
import com.sweethome.bookingService.dto.BookingDto;
import com.sweethome.bookingService.dto.PaymentDto;
import com.sweethome.bookingService.entity.BookingInfoEntity;
import com.sweethome.bookingService.exceptions.CustomException;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class BookingService {

    @Autowired
    public BookingService(BookingDao bookingDao, RestTemplate restTemplate, Producer<String, String> producer){
        this.bookingDao = bookingDao;
        this.restTemplate = restTemplate;
        this.producer = producer;
    }

    BookingDao bookingDao;
    RestTemplate restTemplate;
    Producer<String, String> producer;
    private int pricePerRoomPerDay = 1000;

    @Value("${url.service.payment}")
    private String paymentServiceUrl;

    public static ArrayList<String> getRandomNumbers(int count){
        Random rand = new Random();
        int upperBound = 100;
        ArrayList<String>numberList = new ArrayList<String>();

        for (int i=0; i<count; i++){
            numberList.add(String.valueOf(rand.nextInt(upperBound)));
        }

        return numberList;
    }


    public BookingInfoEntity bookingDetails(BookingDto bookingRequest) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        int requestedNumOfRooms = bookingRequest.getNumOfRooms();

        String roomNumbers = String.join(",", getRandomNumbers(requestedNumOfRooms));
        BookingInfoEntity bookingInfo = new BookingInfoEntity();
        bookingInfo.setRoomNumber(roomNumbers);
        bookingInfo.setFromDate(bookingRequest.getFromDate());
        bookingInfo.setToDate(bookingRequest.getToDate());
        bookingInfo.setBookedOn(date);
        bookingInfo.setAadharNumber(bookingRequest.getAadharNumber());
        long numberOfDays = (bookingInfo.getToDate().getTime() - bookingRequest.getFromDate().getTime())/ (1000*60*60*24);
        System.out.println("Number of days: " + numberOfDays);
        bookingInfo.setRoomPrice((int) (bookingRequest.getNumOfRooms() * pricePerRoomPerDay * numberOfDays));
        System.out.println(bookingInfo.toString());
        return bookingDao.save(bookingInfo);
    }


    public BookingInfoEntity doPayment (PaymentDto paymentDetails) throws Exception {

        System.out.println(paymentDetails.toString());
        String url = this.paymentServiceUrl + "/transaction";

        if(!(paymentDetails.getPaymentMode().trim().equalsIgnoreCase("UPI") | paymentDetails.getPaymentMode().trim().equalsIgnoreCase("CARD"))) {
            throw new CustomException("Invalid Payment Method.");
        }

        int bookingId = paymentDetails.getBookingId();
        Optional<BookingInfoEntity> bookingInfoOptional  = bookingDao.findById(bookingId);
        if(bookingInfoOptional.isPresent()){
            BookingInfoEntity bookingInfo = bookingInfoOptional.get();
            int transactionId = restTemplate.postForObject(url, paymentDetails, Integer.class);
            bookingInfo.setTransactionId(transactionId);
            bookingDao.save(bookingInfo);
            String message = "Booking confirmed for user with aadhaar number: " + bookingInfo.getAadharNumber() +    "    |    "  + "Here are the booking details:    " + bookingInfo.toString();
            producer.send(new ProducerRecord<String, String>("message","message", message));
            return bookingInfo;
        }
        else {
            throw new CustomException("Invalid Booking Id.");
        }

    }
}
