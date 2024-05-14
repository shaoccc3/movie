package com.ispan.theater.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.Screening;
@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Integer>, JpaSpecificationExecutor<Screening> {
	
    @Query("select c from Screening c where c.movie = :movie")
    List<Screening> findByMovie(@Param("movie") Movie movie);
    
    @Query(value="select distinct s.Start_time,s.movie_id,a.cinema_id from Screening as s join auditorium as a on s.auditorium_id=a.auditorium_id where a.cinema_id= :cinemaId and s.movie_id= :movieId",nativeQuery=true)
    List<Map<String,Object>> findScreeningByDate(@Param(value="cinemaId")Integer cinemaId,@Param(value="movieId")Integer movieId);
    
    @Query(value="select s.Screening_id,s.Start_time from Screening as s join auditorium as a on s.auditorium_id=a.auditorium_id where a.cinema_id= :cinemaId and s.Start_time like :date% and s.movie_id= :movieId",nativeQuery=true)
    List<Map<String,Object>> findScreeningByTime(@Param(value="cinemaId")Integer cinemaId,@Param(value="date")String date,@Param(value="movieId")Integer movieId);

}
