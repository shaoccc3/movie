package com.ispan.theater.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
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
    private HttpSession httpSession;
	@Autowired
	private JsonWebTokenUtility jwtu;

	@PostMapping
	public void save(@RequestBody Comment comment) {
	    // Validate user authentication token
	    String authToken = "eyJwMmMiOjEyMDAwMCwicDJzIjoiUWRpQkRUMGI2bXFQVDNDc1lpNHF0ZEg3ZnNWWk1HUE9RMnVIRXhsUzFRR2FPWm1LTnM2eEwySVZRa0Y1d2Vxb2lpb0FId1ZaaXliOXVNNjdfWjhSdXciLCJhbGciOiJQQkVTMi1IUzUxMitBMjU2S1ciLCJlbmMiOiJBMjU2R0NNIn0.f0wHN4mODfc2tynXdjsvySDojMDVq2OxZdXq3yhi8BYYj95ZsnMmiA.WH_OrxmtYliGn-Hb.ZuKcSD5YolMBID8cXlwfI-CyjD2fpD6FP87DPOmSVwJYlZD4_-Q6TyFYS8a6aZkjbn3NsI2wVYDvn7xevJS2WhMwjVSQkV5xvubHP1eLvRzylKkNGXHoogPnFj8lDB8Z2erp916WXzQOjaqA.YVBWhOYlVJsD535cvEI5Sw";
	    if (jwtu.validateEncryptedToken(authToken) != null) {
	        // Check if the user is logged in
	        if (comment.getUserId() != null) {
	            Integer userId = comment.getUserId().getId();
	            // Ensure comment content is not empty
	            if (comment.getContent() != null && !comment.getContent().isEmpty()) {
	                // Set creation time
	                comment.setCreatetime(LocalDateTime.now());
	                // Call service class method to save the comment
	                commentService.insertComment(comment);
	            } else {
	                // Handle empty comment content
	                System.out.println("Comment content is empty.");
	            }
	        } else {
	            // Handle case where user ID is not available
	            System.out.println("User ID is missing.");
	        }
	    } else {
	        // Handle invalid authentication token
	        System.out.println("Invalid authentication token.");
	    }
	}
	@GetMapping
	public Map<String, Object> list(@RequestParam Movie movieId) {
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
	@DeleteMapping("/{commentId}")
    public void delete(@PathVariable Integer commentId) {
        commentRepository.deleteById(commentId);
    }

    @PutMapping("/{commentId}")
    public void update(@PathVariable Integer commentId, @RequestBody Comment updatedComment) {
        Optional<Comment> existingCommentOptional = Optional.of(commentService.findCommentById(commentId));
        if (existingCommentOptional.isPresent()) {
            Comment existingComment = existingCommentOptional.get();
            existingComment.setContent(updatedComment.getContent());
            existingComment.setRate(updatedComment.getRate());

            // Update other fields as needed
            commentRepository.save(existingComment);
        } else {
            // Handle if the comment doesn't exist
            throw new RuntimeException("Comment with id " + commentId + " not found.");
        }
    }

	
	
}
////select * from comment as c join movie as m on c.movie_id=m.movie_id where c.user_id=1