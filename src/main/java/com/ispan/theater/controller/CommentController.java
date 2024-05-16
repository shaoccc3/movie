package com.ispan.theater.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ispan.theater.domain.Comment;
import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.User;
import com.ispan.theater.repository.CommentRepository;
import com.ispan.theater.repository.MovieRepository;
import com.ispan.theater.service.CommentService;
import com.ispan.theater.service.UserService;
import com.ispan.theater.util.JsonWebTokenUtility;

import jakarta.servlet.http.HttpSession;


@CrossOrigin
@RestController
@RequestMapping("comment")
public class CommentController {
	@Autowired
	private MovieRepository movr;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private CommentService commentService;
	@Autowired
	private UserService userService;
	@Autowired
    private HttpSession httpSession;
	@Autowired
	private JsonWebTokenUtility jwtu;
	@PostMapping
	public void save(@RequestBody Comment comment ,@RequestParam String token) {
		
		if (token != null) {
	    	System.out.println(token);
	        // 解碼TOKEN
	        String authToken = jwtu.validateEncryptedToken(token);
	        System.out.println(authToken);
	        if (authToken != null) {
	        	// 解碼TOKEN
	            JSONObject obj = new JSONObject(authToken);
	            Integer userId = obj.getInt("userid");

		        System.out.println(userId);

	            	//輸入USERID
					comment.getUserId().setId(userId);
					comment.getMovieId().setId(3);//設置電影ID
					comment.setCreatetime(LocalDateTime.now());
					// Call service class method to save the comment
					commentService.insertComment(comment);

	        } else {
	            // 錯誤的TOKEN
	            System.out.println("未認證token.");
	        }
	    } else {
	        // 遺失
	        System.out.println("Ttoken遺失");
	        }
	    }

	
	@GetMapping
	public Map<String, Object> list(@RequestParam Movie movieId) {
		System.out.println(movieId);
		Map<String,Object> map = new HashMap<>();
		List<Comment> comments= commentRepository.findAllByMovieId(movieId);
		
		comments.forEach((comment)->System.out.println(comment.getRate())); //列印
		
		map.put("rate", BigDecimal.ZERO);
		List<Comment> commentList = comments.stream().filter(comment -> comment.getRate()!=null).collect(Collectors.toList());
		commentList.stream().map(Comment::getRate).reduce(BigDecimal::add).ifPresent(res ->{
			map.put("rate", res.divide(BigDecimal.valueOf(commentList.size()),1,RoundingMode.HALF_UP));
		});
		
		List<Comment> rootComments = comments.stream().filter(comment -> comment.getPid()== null).collect(Collectors.toList());
		for(Comment rootComment :rootComments) {
			rootComment.setChildren(comments.stream().filter(comment -> rootComment.getCommentId().equals(comment.getPid())).collect(Collectors.toList()));
		}
		
		map.put("comments", rootComments);
		return map;
	}
	
//	@GetMapping("/rate/{movieid}")
//	public void findRate(Integer movieid) {
//		commentRepository.findCommentsByMovieIdByRate(movieid);
//	}
	
	@DeleteMapping("/{commentId}")
    public void delete(@PathVariable Integer commentId) {
        commentRepository.deleteById(commentId);
    }

	@PutMapping("/{commentId}")
	public void update(@PathVariable Integer commentId, @RequestBody Comment updatedComment, @RequestParam String token) {
	    if (token != null) {
	        System.out.println(token);
	        // 解碼TOKEN
	        String authToken = jwtu.validateEncryptedToken(token);
	        System.out.println(authToken);
	        if (authToken != null) {
	            // 解碼TOKEN
	            JSONObject obj = new JSONObject(authToken);
	            Integer userId = obj.getInt("userid");

	            System.out.println(userId);

	            // 檢查該評論是否存在並且屬於該使用者
	            Comment existingComment = commentService.findCommentById(commentId);
	            if (existingComment != null && existingComment.getUserId() != null && existingComment.getUserId().getId().equals(userId)) {
	                // 更新評論內容
	                existingComment.setContent(updatedComment.getContent());
	                existingComment.setRate(updatedComment.getRate());
	                commentRepository.save(existingComment);
	            } else {
	                // 如果評論不存在或不屬於該使用者，拒絕更新
	                System.out.println("使用者不存在,評論不屬於該用戶");
	            }
	        } else {
	            // 錯誤的TOKEN
	            System.out.println("錯誤TOKEN");
	        }
	    } else {
	        // 遺失
	        System.out.println("Token is missing.");
	    }
	}

}
////select * from comment as c join movie as m on c.movie_id=m.movie_id where c.user_id=1