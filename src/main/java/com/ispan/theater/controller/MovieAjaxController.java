package com.ispan.theater.controller;

import com.ispan.theater.domain.Movie;
import com.ispan.theater.service.MovieService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
