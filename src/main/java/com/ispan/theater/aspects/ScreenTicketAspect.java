
package com.ispan.theater.aspects;

import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.Screening;
import com.ispan.theater.service.TicketService;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ScreenTicketAspect {
    @Autowired
    private TicketService ticketService;

    @Pointcut("execution(* com.ispan.theater.service.ScreeningService.createScreening(com.ispan.theater.domain.Movie, org.json.JSONObject)) && args(movie, jsonObject)")
    public void screeningCreation(Movie movie, JSONObject jsonObject) {}
    @Pointcut("execution(* com.ispan.theater.service.ScreeningService.jsonToScreen(org.json.JSONObject)) && args(jsonScreen)")
    public void jsonScreening(JSONObject jsonScreen) {}

    @AfterReturning(pointcut = "screeningCreation(movie, jsonObject)", returning = "screening")
    @Transactional
    public void afterReturningAdvice(Movie movie, JSONObject jsonObject, Screening screening) {
        JSONObject screeningJson = new JSONObject();
        screeningJson.put("screeningId", screening.getId());
        ticketService.insertTicket2(screeningJson);
        System.out.println("HAHAHASuccessfully created screening for movie: " + screening.getMovie().getName());
    }
    @AfterReturning(pointcut = "jsonScreening(jsonScreen)", returning = "screeningid")
    @Transactional
    public void afterJsonScreening(JSONObject jsonScreen, Integer screeningid) {
        JSONObject screeningJson = new JSONObject();
        screeningJson.put("screeningId", screeningid);
        ticketService.insertTicket2(screeningJson);
        System.out.println("HAHAHASuccessfully created screening for movie: " + screeningid);
    }

    @AfterThrowing(pointcut = "screeningCreation(movie, jsonObject)", throwing = "exception")
    public void afterThrowingAdvice(Movie movie, JSONObject jsonObject, Exception exception) {
        System.out.println("Error occurred while creating screening for movie: " + movie.getName());
        System.out.println("Error message: " + exception.getMessage());
    }
    
}
