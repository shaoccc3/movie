package com.ispan.theater.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.Screening;
@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Integer> {
	
    @Query("select c from Screening c where c.movie = :movie")
    List<Screening> findByMovie(@Param("movie") Movie movie);
    
    @Query(value="select distinct substring(convert(varchar,s.Start_time),1,10) as Start_time,s.movie_id,a.cinema_id from Screening as s join auditorium as a on s.auditorium_id=a.auditorium_id where a.cinema_id= :cinemaId and s.movie_id= :movieId",nativeQuery=true)
    List<Map<String,Object>> findScreeningByDate(@Param(value="cinemaId")Integer cinemaId,@Param(value="movieId")Integer movieId);
    
    @Query(value="select s.Screening_id,substring(convert( varchar,s.Start_time),12,8) as Start_time,a.auditorium_number from Screening as s join auditorium as a on s.auditorium_id=a.auditorium_id where a.cinema_id= :cinemaId and s.Start_time like :date% and s.movie_id= :movieId order by a.auditorium_number asc , s.Start_time asc",nativeQuery=true)
    List<Map<String,Object>> findScreeningByTime(@Param(value="cinemaId")Integer cinemaId,@Param(value="date")String date,@Param(value="movieId")Integer movieId);
    
}
 