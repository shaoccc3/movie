package com.ispan.theater.repository;

import com.ispan.theater.dao.MovieDao;
import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.Rated;
import com.ispan.theater.util.DatetimeConverter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> , JpaSpecificationExecutor<Movie>,MovieDao {
    @Query("select c from Movie c where c.name = :name")
    public Movie findByName(@Param("name") String name);
    @Query("select c from Movie c where c.name like concat('%', :name ,'%')")
    public List<Movie> fineMovieByNameLike(@Param("name") String name);
    @Query("select count(*) from Movie c where c.name like concat('%', :name ,'%')")
    public long countByNameLike(String name);

}