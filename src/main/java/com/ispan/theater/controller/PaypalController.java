package com.ispan.theater.controller;

import com.ispan.theater.domain.PaypalOrder;
import com.ispan.theater.dto.PaymentRequest;
import com.ispan.theater.service.PaypalService;
import com.ispan.theater.util.JsonWebTokenUtility;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;

@RestController
@CrossOrigin
@RequestMapping("/api/paypal")
public class PaypalController {
    @Autowired
    private PaypalService paypalService;
    @Autowired
    private JsonWebTokenUtility jsonWebTokenUtility;
    String cancelUrl = "https://httpbin.org/get?paymentStatus=cancelled";
    String successUrl =  "http://localhost:5173/order/paymentsuccess";

    @PostMapping("/pay")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest) {
        //String data = jsonWebTokenUtility.validateEncryptedToken(token);
        try {
            Payment payment = paypalService.createPayment(
                    paymentRequest.getTotal(), paymentRequest.getCurrency(),
                    "paypal", "sale", "Payment Description", cancelUrl,
                    successUrl);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return ResponseEntity.ok(link.getHref());
                }
            }
            return ResponseEntity.ok("No approval URL FOUND");
        } catch (PayPalRESTException e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }


    }

    @GetMapping("/execute")
    public  ResponseEntity<?> executePayment(@RequestParam String PayerID, @RequestParam String paymentId) {
        //String data = jsonWebTokenUtility.validateEncryptedToken(token);
        try {
            Payment payment = paypalService.executePayment(paymentId, PayerID);
            String saleid = paypalService.getSaleIdFromPayment(payment);
            System.out.println(paymentId);
            if ("approved".equals(payment.getState())) {
                paypalService.insertPaypalOrder(paymentId,PayerID,1,"付款成功",saleid);
                return ResponseEntity.ok(new HashMap<String, String>() {{
                    put("status", "success");
                    put("url", "http://localhost:5173/movie/findlist");
                }});
            } else {
                paypalService.insertPaypalOrder(paymentId,PayerID,1,"付款失敗",saleid);
                return ResponseEntity.ok(new HashMap<String, String>() {{
                    put("status", "failure");
                    put("url", "http://localhost:5173/");
                }});
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new HashMap<String, String>() {{
                put("status", "error");
                put("url", "http://localhost:5173/");
            }});
        }
    }
    @PostMapping("/refund")
    public ResponseEntity<?> refundPaypal(@RequestParam("orderId") Integer orderId) {
        try {
            PaypalOrder paypalOrder = paypalService.findByOrderId(orderId);
            String saleId = paypalOrder.getSaleId();
            String refundStatus = paypalService.refundPayment(saleId);
            if ("success".equals(refundStatus)) {
                paypalOrder.setStatus("已退款");
                paypalService.updatePaypalorder(paypalOrder);
                return ResponseEntity.ok(new HashMap<String, Object>() {{
                    put("status", "success");
                    put("message", "Refund successful");
                }});
            } else {
                return ResponseEntity.ok(new HashMap<String, Object>() {{
                    put("status", "failure");
                    put("message", "Refund failed");
                }});
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HashMap<String, Object>() {{
                put("status", "error");
                put("message", "Error processing refund: " + e.getMessage());
            }});
        }
    }


}
