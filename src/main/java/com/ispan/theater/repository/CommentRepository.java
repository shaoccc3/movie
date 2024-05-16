package com.ispan.theater.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ispan.theater.domain.Comment;
import com.ispan.theater.domain.Movie;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
	
	
    List<Comment> findAllByMovieId(Movie movie);
    
//    @Query(value = "SELECT c.*, u.userPhoto, u.userLastname, u.userId, m.movieId, m.categoryCode, m.description, m.director, m.image, m.modifyDate, m.name, m.nameEng FROM [User] u JOIN Comment c ON c.userId = u.userId JOIN Movie m ON m.movieId = c.movieId WHERE c.movieId = 2 ORDER BY c.rate DESC", nativeQuery = true)
//    List<Comment> findCommentsByMovieIdByRate(@Param("movieId") Integer movieId);
    }

	
	
//	@Query("from comment")
//	Page<Comment>findNewComment(Pageable pgb);

