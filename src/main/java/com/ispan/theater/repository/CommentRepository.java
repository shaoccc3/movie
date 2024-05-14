package com.ispan.theater.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ispan.theater.domain.Comment;
import com.ispan.theater.domain.Movie;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
	
	
    List<Comment> findAllByMovieId(Movie movie);
	
	
//	@Query("from comment")
//	Page<Comment>findNewComment(Pageable pgb);
}
