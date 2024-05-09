package com.ispan.theater.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ispan.theater.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
	List<Comment> findAllByForeignId(Integer foreignID);
}
