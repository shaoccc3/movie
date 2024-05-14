package com.ispan.theater.service;

import com.ispan.theater.domain.Auditorium;
import com.ispan.theater.domain.AuditoriumLevel;
import com.ispan.theater.domain.Cinema;
import com.ispan.theater.repository.AuditoriumLevelRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class testAuditoriumService {
    @Autowired
    private AuditoriumService auditoriumService;
    @Autowired
    private CinemaService cinemaService;
    @Autowired
    private AudoriumLevelService audoriumLevelService;
    @Autowired
    private LayoutService layoutService;

    @Test
    public void testAuditoriumService() {
        AuditoriumLevel auditoriumLevel = new AuditoriumLevel();
        auditoriumLevel.setContext("數位");
        audoriumLevelService.insertLevel(auditoriumLevel);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("layoutId", 1);
        jsonObject.put("levelid",1);
        jsonObject.put("auditoriumNumber",1);
        jsonObject.put("cinemaid",1);
        Auditorium auditorium =  auditoriumService.saveAuditorium(jsonObject);
    }
    @Test
    public void testLayout() {
        Auditorium auditorium = auditoriumService.getAuditorium(1);
        layoutService.insertLayout(auditorium);
    }
}
