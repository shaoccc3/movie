package com.ispan.theater.service;

import com.ispan.theater.domain.Auditorium;
import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.Screening;
import com.ispan.theater.repository.AuditoriumRepository;
import com.ispan.theater.repository.MovieRepository;
import com.ispan.theater.repository.ScreeningRepository;
import com.ispan.theater.util.DatetimeConverter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class ScreeningService {
    @Autowired
    private ScreeningRepository screeningRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private AuditoriumRepository auditoriumRepository;

    public Screening createScreening(Movie movie, JSONObject jsonObject) {
        String startTime = jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        Integer auditoriumId = jsonObject.getInt("auditoriumId");
        Auditorium auditorium = auditoriumRepository.findById(auditoriumId).orElse(null);
        Screening screening = new Screening();
        screening.setMovie(movie);
        screening.setCreateDate(new Date());
        screening.setModifyDate(new Date());
        screening.setStartTime(DatetimeConverter.parse(startTime, "yyyy-MM-dd HH:mm"));
        screening.setEndTime(DatetimeConverter.parse(endTime, "yyyy-MM-dd HH:mm"));
        screening.setAuditorium(auditorium);
        return screeningRepository.save(screening);
    }
    public Screening getScreening(Integer screeningId) {
        return screeningRepository.findById(screeningId).orElse(null);
    }
}
