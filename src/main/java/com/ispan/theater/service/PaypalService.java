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
import java.util.List;

@Service
public class PaypalService {
    @Autowired
    private APIContext apiContext;
    @Autowired
    private PaypalOrderRepository paypalOrderRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Payment createPayment(Double total, String currency, String method, String intent, String description
            , String cancelUrl, String successUrl) throws PayPalRESTException {
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

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecution);
    }

    public String refundPayment(String saleId) throws PayPalRESTException {
        Sale sale = Sale.get(apiContext, saleId);
        String saleAmouunt = sale.getAmount().getTotal();
        String currency = sale.getAmount().getCurrency();
        RefundRequest refundRequest = new RefundRequest();
        Amount amount = new Amount().setCurrency(currency).setTotal(saleAmouunt);
        refundRequest.setAmount(amount);
        Refund refund = sale.refund(apiContext, refundRequest);
        if (refund.getState().equals("completed")) {
            return "success";
        } else {
            return "fail";
        }
    }

    public String getSaleIdFromPayment(Payment payment) throws PayPalRESTException {
        for (Transaction transaction : payment.getTransactions()) {
            List<RelatedResources> relatedResources = transaction.getRelatedResources();
            for (RelatedResources relatedResource : relatedResources) {
                Sale sale = relatedResource.getSale();
                if (sale != null) {
                    return sale.getId();
                }
            }
        }
        return null;
    }
    public void updatePaypalorder(PaypalOrder order) throws PayPalRESTException {
        paypalOrderRepository.save(order);
    }
    public void insertPaypalOrder(String paymentId, String PayerID, Integer orderId, String status, String saleId) throws PayPalRESTException {
        PaypalOrder paypalOrder = new PaypalOrder();
        paypalOrder.setPaymentId(paymentId);
        paypalOrder.setPayerId(PayerID);
        paypalOrder.setOrderId(orderId);
        paypalOrder.setStatus(status);
        paypalOrder.setSaleId(saleId);
        paypalOrderRepository.save(paypalOrder);
    }

    public PaypalOrder findByOrderId(Integer orderId) throws PayPalRESTException {
        return paypalOrderRepository.findByOrderId(orderId);
    }

}
