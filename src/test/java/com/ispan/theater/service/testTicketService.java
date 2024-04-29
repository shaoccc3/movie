package com.ispan.theater.service;

import com.ispan.theater.domain.Screening;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class testTicketService {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private ScreeningService screeningService;
    @Test
    public void testTicketServiceInsert() {
        Screening s = screeningService.getScreening(2);
        JSONObject jo = new JSONObject().put("screeningId", s.getId());
        ticketService.insertTicket(jo);

    }
}
