package com.ispan.theater.listener;

import org.springframework.context.ApplicationEvent;

public class OrderConditionEvent extends ApplicationEvent {
	
	private String message;
	
	public OrderConditionEvent(Object source, String message) {
		super(source);
		this.message=message;
	}

	public String getMessage() {
        return message;
    }

}
