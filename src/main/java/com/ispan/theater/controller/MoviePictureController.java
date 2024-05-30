package com.ispan.theater.controller;

import com.google.api.client.json.Json;
import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.MoviePicture;
import com.ispan.theater.dto.MoviePicDto;
import com.ispan.theater.service.MoviePictureService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class MoviePictureController {
    @Autowired
    private MoviePictureService moviePictureService;

    @GetMapping("/moviePicture/{id}")
    public ResponseEntity getMoviePicture(@PathVariable Integer id) {
        return ResponseEntity.ok(moviePictureService.getMoviePictures(id));
    }

    @PostMapping("/admin/moviePicture/{id}")
    public ResponseEntity<?> addMoviePicture(@PathVariable Integer id, @RequestParam("files") List<MultipartFile> files) throws IOException {
        boolean insert = moviePictureService.insertMoviePicture(files, id);
        if (insert) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/admin/moviePicture")
    public void deleteMoviePicture(@RequestParam("lists") List<Integer> lists) {
        moviePictureService.deleteMoviePicture(lists);
    }
}
