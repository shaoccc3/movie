package com.ispan.theater.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OrderConditionPublisherV2 {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	public void publish(Integer userId) {
		// [2]使用publishEvent方法发布事件
		applicationEventPublisher.publishEvent(new OrderConditionEventV2(this, userId));
	}
}
