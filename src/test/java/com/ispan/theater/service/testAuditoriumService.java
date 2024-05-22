package com.ispan.theater.service;


import com.ispan.theater.domain.Auditorium;
import com.ispan.theater.domain.AuditoriumLevel;
import com.ispan.theater.domain.Cinema;
import com.ispan.theater.domain.Layout;
import com.ispan.theater.repository.AuditoriumLevelRepository;
import com.ispan.theater.repository.LayoutRepository;
import org.json.JSONObject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;



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
    @Autowired
    private LayoutRepository layoutRepository;


    @Test
    public void testAuditoriumService() {
        AuditoriumLevel auditoriumLevel = new AuditoriumLevel();
        auditoriumLevel.setContext("數位");
        audoriumLevelService.insertLevel(auditoriumLevel);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("layoutId", 1);
        jsonObject.put("levelid",1);
        jsonObject.put("auditoriumNumber",3);
        jsonObject.put("cinemaid",1);


        Auditorium auditorium =  auditoriumService.saveAuditorium(jsonObject);
    }

//    @Test
//    public void testAuditoriumService() {
//        AuditoriumLevel auditoriumLevel = new AuditoriumLevel();
//        auditoriumLevel.setContext("數位");
//        audoriumLevelService.insertLevel(auditoriumLevel);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("layoutId", 1);
//        jsonObject.put("levelid",1);
//        jsonObject.put("auditoriumNumber",1);
//        jsonObject.put("cinemaid",1);
//
//
//        Auditorium auditorium =  auditoriumService.saveAuditorium(jsonObject);
//    }
    @Transactional
    @Test
    public void testLayout() {
        Auditorium auditorium = auditoriumService.getAuditorium(3);
        layoutService.insertLayout(auditorium);
    }
    @Test
    public void testSelect() {
        Auditorium auditorium = auditoriumService.getAuditorium(3);
        List<Layout> result = layoutRepository.findByAuditorium(auditorium);
        for (Layout layout : result) {
            System.out.println(layout.toString());
        }
    }
}
