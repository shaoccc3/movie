package com.ispan.theater.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.theater.domain.Comment;
import com.ispan.theater.repository.CommentRepository;


@CrossOrigin
@RestController
//@RequestMapping("/comment")
public class CommentController {
	@Autowired
	CommentRepository commentRepository;
	@PostMapping("/comment")
	public void save(@RequestBody Comment comment) {
		commentRepository.save(comment);
		
		
	}
	@GetMapping
	public Map<String, Object> list(@RequestParam Integer foreignId) {
		Map<String,Object> map = new HashMap<>();
		List<Comment> comments= commentRepository.findAllByForeignId(foreignId);
		
		map.put("rate", BigDecimal.ZERO);
		List<Comment> commentList = comments.stream().filter(comment -> comment.getRate()!=null).collect(Collectors.toList());
		commentList.stream().map(Comment::getRate).reduce(BigDecimal::add).ifPresent(res ->{
			map.put("rate", res.divide(BigDecimal.valueOf(commentList.size()),1,RoundingMode.HALF_UP));
		});
		
		return map;
	}
	
	
}
