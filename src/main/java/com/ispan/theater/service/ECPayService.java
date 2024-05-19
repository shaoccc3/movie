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
		obj.setMerchantTradeNo(order.getEcpayNo());
		obj.setTotalAmount(order.getOrderAmount().toString().substring(0,order.getOrderAmount().toString().indexOf("."))+"");
		obj.setMerchantTradeDate(DatetimeConverter.createSqlDatetimeECPay(order.getCreateDate()));
//		obj.setTotalAmount("123");
//		obj.setMerchantTradeDate("2027/12/02 10:10:10");
		obj.setTradeDesc("此次共買了"+quantity+"張電影票");
		obj.setItemName("電影票");
		obj.setReturnURL("http://losthost:8080/ecpayResult");
//		obj.setClientBackURL("http://localhost:5173/order/showtimes");
		obj.setOrderResultURL("http://localhost:8080/order-redirect");
		obj.setNeedExtraPaidInfo("Y");
		String form = all.aioCheckOut(obj, null);
		String temp =form.substring(0, form.indexOf("<script"))+"</form>";
		return temp;
	}
	
}
