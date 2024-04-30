package com.ispan.theater.controller;

import com.ispan.theater.domain.Movie;
import com.ispan.theater.service.MovieService;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MovieAjaxController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/backstage/movie")//test passed
    public String findMovie(@RequestParam("name") String name,@RequestParam("page") Integer page){
        System.out.println(name);
        JSONObject response = new JSONObject();
        long count = movieService.count(name);
        response.put("count", count);
        List<Movie> result = movieService.getMovieByName(name,page);
        JSONArray array = new JSONArray();
        if(result!=null && !result.isEmpty()){
            for(Movie m : result){
                JSONObject movie = movieService.movieToJson(m);
                array.put(movie);
            }
        }
        response.put("movie", array);
        return response.toString();
    }
    @PostMapping("/backstage/movie/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        Movie movie = movieService.getMovieById(2);
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("上傳的檔案為空");
            }
            byte[] imageData = file.getBytes();
            String base64Photo =  Base64.encodeBase64String(imageData);
            movie.setImage(base64Photo);
            movieService.saveMovie(movie);

            return ResponseEntity.ok("上傳成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("上傳失敗: " + e.getMessage());
        }
    }

}
