package com.ispan.theater.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ispan.theater.domain.Comment;
import com.ispan.theater.domain.Movie;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
	
	
	Page<Comment> findAllByMovieIdAndPidIsNull(Movie movieId, Pageable pageable);
	List<Comment> findAllByMovieId(Movie movieId);
    
    @Query(value="SELECT c.*, u.user_id, u.email, u.user_photo, u.user_lastname, m.movie_id, m.category_code, m.description, m.director, m.image, m.modify_date, m.name, m.name_eng, m.rated_code FROM comment c JOIN \"user\" as u ON c.user_id = u.user_id JOIN movie as m ON c.movie_id = m.movie_id WHERE c.movie_id = :movieId order by rate desc",nativeQuery=true)
    List<Comment>findCommentsByMovieIdByRate(@Param("movieId")Integer movieId);

//    @Query(value = "SELECT c.*, u.userPhoto, u.userLastname, u.userId, m.movieId, m.categoryCode, m.description, m.director, m.image, m.modifyDate, m.name, m.nameEng FROM [User] u JOIN Comment c ON c.userId = u.userId JOIN Movie m ON m.movieId = c.movieId WHERE c.movieId = 2 ORDER BY c.rate DESC", nativeQuery = true)
//    List<Comment> findCommentsByMovieIdByRate(@Param("movieId") Integer movieId);
    }

	
	


