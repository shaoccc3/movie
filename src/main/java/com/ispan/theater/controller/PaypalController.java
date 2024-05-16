package com.ispan.theater.controller;

import com.ispan.theater.dto.PaymentRequest;
import com.ispan.theater.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/paypal")
public class PaypalController {
    @Autowired
    private PaypalService paypalService;
    String cancelUrl = "https://httpbin.org/get?paymentStatus=cancelled";
    String successUrl = "https://httpbin.org/get?paymentStatus=success";

    @PostMapping("/pay")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest){
        try{
            Payment payment = paypalService.createPayment(
                    paymentRequest.getTotal(),paymentRequest.getCurrency(),
                    "paypal","sale","Payment Description",cancelUrl,
                    successUrl);
            for(Links link : payment.getLinks()){
                if(link.getRel().equals("approval_url")){
                    return ResponseEntity.ok(link.getHref());
                }
            }
            return ResponseEntity.ok("No approval URL FOUND");
        }
        catch (PayPalRESTException e){
            return ResponseEntity.badRequest().body("ERROR: "+ e.getMessage());
        }
    }
    @PostMapping("/exucute")
    public ResponseEntity<?> createPayment(@RequestParam String payerId, @RequestParam String paymentId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return ResponseEntity.ok("Payment successful.");
            }
            return ResponseEntity.badRequest().body("Payment failed");
        } catch (PayPalRESTException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
