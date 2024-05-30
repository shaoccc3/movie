package com.ispan.theater.controller;

import com.ispan.theater.domain.Cinema;
import com.ispan.theater.dto.CinemaDto;
import com.ispan.theater.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class CinemaCotroller {
    @Autowired
    private CinemaService cinemaService;
    @PostMapping("/admin/backstage/updateCinemaAddress")
    public ResponseEntity<?> updateCinemaAddress(@RequestBody CinemaDto cinemadto) {
        Integer id = cinemadto.id;
        Cinema cinema = cinemaService.getCinemaById(id);
        if(cinema != null) {
            cinema.setAddress(cinemadto.address);
            cinemaService.saveCinema(cinema);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
