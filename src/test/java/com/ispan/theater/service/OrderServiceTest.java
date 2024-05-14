package com.ispan.theater.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ispan.theater.domain.Order;

import jakarta.transaction.Transactional;

@SpringBootTest
public class OrderServiceTest {
	@Autowired
	OrderService os;
	
	ExecutorService executor=Executors.newFixedThreadPool(100);
	CountDownLatch countDownLatch=new CountDownLatch(100);
	
//	@Test
//	@Transactional
	public void find() {
		Order order=os.findOrderByUserId(3);
//		System.out.println(order);
	}
	
	@Test
	public void lockTest() throws InterruptedException {
		for(int i=0;i<10;i++) {
			executor.execute(()->{
			Order order=os.findOrderByUserId(3);
			synchronized (this) {
				if(order.getOrderAmount()==1) {
					return;
				}
				os.updeteOrderAmount(order);
				countDownLatch.countDown();
			}
			});
		}
		countDownLatch.await();
		executor.shutdown();
	}
	
	
}


