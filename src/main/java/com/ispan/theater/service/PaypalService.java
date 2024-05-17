package com.ispan.theater.service;

import com.ispan.theater.domain.PaypalOrder;
import com.ispan.theater.repository.PaypalOrderRepository;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class PaypalService {
    @Autowired
    private APIContext apiContext;
    @Autowired
    private PaypalOrderRepository paypalOrderRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Payment createPayment(Double total,String currency,String method,String intent,String description
                                ,String cancelUrl,String successUrl)throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(Arrays.asList(transaction));

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId,String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return payment.execute(apiContext,paymentExecution);
    }
    public String refundPayment(String paymentId) throws PayPalRESTException{
        Payment payment = Payment.get(apiContext, paymentId);
        for (Transaction transaction : payment.getTransactions()) {
            for (RelatedResources resources : transaction.getRelatedResources()) {
                Sale sale = resources.getSale();
                return sale.getId();  // 获取 saleId
            }
        }
        Sale sale = Sale.get(apiContext,paymentId);
        Refund refund = new Refund();
        refund.setAmount(sale.getAmount());
        Refund refund1 = sale.refund(apiContext,refund);
        if(refund1.getState().equals("completed")){
            return "success";
        }
        else{
            return "fail";
        }

    }
    public void insertPaypalOrder(String paymentId,String PayerID,Integer orderId,String status) throws PayPalRESTException {
        PaypalOrder paypalOrder = new PaypalOrder();
        paypalOrder.setPaymentId(paymentId);
        paypalOrder.setPayerId(PayerID);
        paypalOrder.setOrderId(orderId);
        paypalOrder.setStatus(status);
        paypalOrderRepository.save(paypalOrder);
    }
    public PaypalOrder findByOrderId(Integer orderId) throws PayPalRESTException {
        return paypalOrderRepository.findByOrderId(orderId);
    }

}
