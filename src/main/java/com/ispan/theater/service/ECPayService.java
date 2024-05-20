package com.ispan.theater.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ispan.theater.domain.Order;
import com.ispan.theater.util.DatetimeConverter;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

@Service
public class ECPayService {
	public String ecpayCheckout(Order order,Integer quantity) {
		AllInOne all=new AllInOne("");
		AioCheckOutALL obj = new AioCheckOutALL();
//		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
		obj.setMerchantTradeNo(order.getPaymentNo());
		obj.setTotalAmount(order.getOrderAmount().toString().substring(0,order.getOrderAmount().toString().indexOf("."))+"");
		obj.setMerchantTradeDate(DatetimeConverter.createSqlDatetimeECPay(order.getCreateDate()));
		obj.setTradeDesc("此次共買了"+quantity+"張電影票");
		obj.setItemName("電影票");
		obj.setReturnURL("http://losthost:8080/ecpayResult");
		obj.setOrderResultURL("http://localhost:8080/order-redirect");
//		obj.setOrderResultURL("http://httpbin.org/post");

		obj.setNeedExtraPaidInfo("Y");
		String form = all.aioCheckOut(obj, null);
		String temp =form.substring(0, form.indexOf("<script"))+"</form>";
		return temp;
	}
	
}
