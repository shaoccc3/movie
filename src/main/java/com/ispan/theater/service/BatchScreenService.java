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
import org.springframework.transaction.annotation.Transactional;

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

//    public void batchScreenInsert(SchduleDto request) {
//        Movie movie = movieRepository.findById(request.getMovieId()).orElseThrow(() -> new RuntimeException("Movie not found"));
//        Auditorium auditorium = auditoriumRepository.findById(request.getAudotoriumId()).orElseThrow(() -> new RuntimeException("Auditorium not found"));
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(DatetimeConverter.parse(request.getStartDate(),"yyyy-MM-dd"));
//        Date endDate = DatetimeConverter.parse(request.getEndDate(),"yyyy-MM-dd");
//        String frequency = request.getFrequency();
//        while (!calendar.getTime().after(endDate)){
//            calendar.set(Calendar.HOUR_OF_DAY, 9);
//            calendar.set(Calendar.MINUTE, 0);
//            calendar.set(Calendar.SECOND, 0);
//            Date dayEnd = (Date) calendar.getTime().clone();
//            dayEnd.setHours(23);
//            dayEnd.setMinutes(59);
//            if(frequency.equals("僅熱門時段")){
//                if(calendar.getTime().getDay()>0&&calendar.getTime().getDay()<6){
//                    Date temp = calendar.getTime();
//                    temp.setHours(16);
//                    calendar.setTime(temp);
//                }
//                Screening screening = new Screening();
//                screening.setStartTime(calendar.getTime());
//                calendar.add(Calendar.MINUTE,movie.getDuration());
//                screening.setMovie(movie);
//            } else if (frequency.equals("僅早午夜場")) {
//                if(calendar.getTime().getHours()>10 && calendar.getTime().getHours()<21){
//                    Date temp = calendar.getTime();
//                    temp.setHours(21);
//                    calendar.setTime(temp);
//                }
//
//            }
//            else {
//                for(int i=0;i<=request.getDailySessions();i++){
//                    Screening screening = new Screening();
//                    screening.setStartTime(calendar.getTime());
//                    calendar.add(Calendar.MINUTE,movie.getDuration());
//
//                    screening.setMovie(movie);
//                    screening.setEndTime(calendar.getTime());
//                    calendar.add(Calendar.MINUTE,30);
//                    if(calendar.getTime().after(dayEnd)){
//                        break;
//                    }
//                    screening.setAuditorium(auditorium);
//                    screening.setCreateDate(new Date());
//                    screening.setModifyDate(new Date());
//                    screeningRepository.save(screening);
//                }
//            }
//            calendar.add(Calendar.DATE, 1);
//        }


    //    }
    @Transactional
    public void batchScreenInsert(SchduleDto request) {
        Movie movie = movieRepository.findById(request.getMovieId()).orElseThrow(() -> new RuntimeException("Movie not found"));
        Auditorium auditorium = auditoriumRepository.findById(request.getAudotoriumId()).orElseThrow(() -> new RuntimeException("Auditorium not found"));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DatetimeConverter.parse(request.getStartDate(), "yyyy-MM-dd"));
        Date endDate = DatetimeConverter.parse(request.getEndDate(), "yyyy-MM-dd");

        while (!calendar.getTime().after(endDate)) {
            setupDailyScreenings(calendar, movie, auditorium, request.getFrequency(), request.getDailySessions());
            calendar.add(Calendar.DATE, 1);
        }
    }

    private void setupDailyScreenings(Calendar date, Movie movie, Auditorium auditorium, String frequency, int sessionsPerDay) {
        Calendar sessionTime = (Calendar) date.clone();
        sessionTime.set(Calendar.HOUR_OF_DAY, 9);
        sessionTime.set(Calendar.MINUTE, 0);
        sessionTime.set(Calendar.SECOND, 0);
        int endHour = 23;
        int endHourmin = 1;
        int currentDay = sessionTime.get(Calendar.DAY_OF_YEAR);
        System.out.println(frequency);
        if (frequency.equals("僅熱門時段")) {
            if (sessionTime.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || sessionTime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                // 周末全天
                sessionTime.set(Calendar.HOUR_OF_DAY, 9);
            } else {
                // 平日下午四點到晚上九點
                if (sessionTime.get(Calendar.HOUR_OF_DAY) < 16) {
                    sessionTime.set(Calendar.HOUR_OF_DAY, 16);
                }
                endHour = 21;
            }
        }
        for (int i = 0; i < sessionsPerDay; i++) {
            int hourOfDay = sessionTime.get(Calendar.HOUR_OF_DAY);
            if (hourOfDay > endHour || hourOfDay < endHourmin) {
                break; // 確保不超過指定的結束時間
            }

            if (frequency.equals("僅早午夜場")) {
                // 早上十點前 晚上九點後
                if (sessionTime.get(Calendar.HOUR_OF_DAY) < 21 && sessionTime.get(Calendar.HOUR_OF_DAY) > 9) {
                    sessionTime.set(Calendar.HOUR_OF_DAY, 21);
                }
            }
            Screening screening = new Screening();
            screening.setStartTime(sessionTime.getTime());
            sessionTime.add(Calendar.MINUTE, movie.getDuration());  // 增加電影時長

            screening.setEndTime(sessionTime.getTime());
            screening.setMovie(movie);
            screening.setAuditorium(auditorium);
            screening.setCreateDate(new Date());
            screening.setModifyDate(new Date());

            screeningRepository.save(screening);
            sessionTime.add(Calendar.MINUTE, 30);  // 增加30分鐘緩衝

            if (hourOfDay > endHour || hourOfDay < endHourmin || sessionTime.get(Calendar.DAY_OF_YEAR) != currentDay) {
                break; // 確保不超過指定的結束時間，且不跨越到新的一天
            }
        }
    }


}
