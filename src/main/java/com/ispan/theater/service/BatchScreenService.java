package com.ispan.theater.service;

import com.ispan.theater.domain.Auditorium;
import com.ispan.theater.domain.Cinema;
import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.Screening;
import com.ispan.theater.dto.SchduleDto;
import com.ispan.theater.repository.AuditoriumRepository;
import com.ispan.theater.repository.CinemaRepository;
import com.ispan.theater.repository.MovieRepository;
import com.ispan.theater.repository.ScreeningRepository;
import com.ispan.theater.util.DatetimeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class BatchScreenService {
    @Autowired
    private ScreeningRepository screeningRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private AuditoriumRepository auditoriumRepository;
    @Autowired
    private CinemaRepository cinemaRepository;

    public void batchScreenInsert(SchduleDto request) {
        Movie movie = movieRepository.findById(request.getMovieId()).orElseThrow(() -> new RuntimeException("Movie not found"));
        Auditorium auditorium = auditoriumRepository.findById(request.getAudotoriumId()).orElseThrow(() -> new RuntimeException("Auditorium not found"));
        Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();
        calendar.setTime(DatetimeConverter.parse(request.getStartDate(),"yyyy-MM-dd"));
        Date endDate = DatetimeConverter.parse(request.getEndDate(),"yyyy-MM-dd");
        while (!calendar.getTime().after(endDate)){
            calendar.set(Calendar.HOUR_OF_DAY, 9);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date dayEnd = (Date) calendar.getTime().clone();
            dayEnd.setHours(23);
            dayEnd.setMinutes(59);
            for(int i=0;i<=request.getDailySessions();i++){
                Screening screening = new Screening();
                screening.setStartTime(calendar.getTime());
                calendar.add(Calendar.MINUTE,movie.getDuration());

                screening.setMovie(movie);
                screening.setEndTime(calendar.getTime());
                calendar.add(Calendar.MINUTE,30);
                if(calendar.getTime().after(dayEnd)){
                    break;
                }
                screening.setAuditorium(auditorium);
                screening.setCreateDate(new Date());
                screening.setModifyDate(new Date());
                screeningRepository.save(screening);
            }
            calendar.add(Calendar.DATE, 1);
        }


    }

}
