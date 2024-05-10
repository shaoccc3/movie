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
    
    @Query("select distinct s.startTime from Screening as s join Movie as m on s.movie.id=m.id where s.auditorium.id= :auditoriumId")
    List<String> findScreeningByDate(@Param(value="auditoriumId")Integer auditoriumId);
    
    @Query(value="select s.Screening_id,s.Start_time from Screening as s join movie as m on s.movie_id=m.movie_id where s.auditorium_id= :auditoriumId and s.Start_time like :date%",nativeQuery=true)
    List<Map<String,Object>> findScreeningByTime(@Param(value="auditoriumId")Integer auditoriumId,@Param(value="date")String date);
    
}
