package com.ispan.theater.controller;

import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.MoviePicture;
import com.ispan.theater.dto.MoviePicDto;
import com.ispan.theater.service.MoviePictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class MoviePictureController {
    @Autowired
    private MoviePictureService moviePictureService;
    @GetMapping("/moviePiture/{id}")
    public List<MoviePicDto> getMoviePicture(@PathVariable Integer id) {
        return moviePictureService.getMoviePictures(id);
    }
    @PostMapping("/admin/moviePicture/{id}")
    public ResponseEntity<?> addMoviePicture(@PathVariable Integer id, @RequestBody Map<String,Byte[]> moviePictureMap) {
        boolean insert = moviePictureService.insertMoviePicture(moviePictureMap,id);
        if(insert) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
    @DeleteMapping("/admin/moviePicture")
    public void deleteMoviePicture(@RequestParam List<Integer> lists) {
        moviePictureService.deleteMoviePicture(lists);
    }
}
