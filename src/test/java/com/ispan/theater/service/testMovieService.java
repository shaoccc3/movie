package com.ispan.theater.service;

import com.ispan.theater.domain.Category;
import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.Rated;
import com.ispan.theater.repository.CategoryRepository;
import com.ispan.theater.repository.MovieRepository;
import com.ispan.theater.repository.RatedRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.List;

@SpringBootTest
public class testMovieService {
    @Autowired
    private MovieService movieService;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private RatedRepository ratedRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testInsertMovie() {
        Rated rated = new Rated();


        JSONObject movieJson = new JSONObject().
                put("name","Saving Private Ryan").
                put("description","TextText").
                put("director","Steven Spielberg").
                put("categoryCode","e").
                put("duration",170).
                put("price",500).
                put("ratedCode","c").
                put("releaseDate","1998-09-26").
                put("endDate","1998-11-26");
        JSONObject movieJson1 = new JSONObject().
                put("name","Brokeback Mountain").
                put("description","TextText").
                put("director","李安").
                put("categoryCode","b,c").
                put("price",400).
                put("duration",134).
                put("ratedCode","d").
                put("releaseDate","2006-01-20").
                put("endDate","2006-02-28");

//        Movie movie = movieService.jsonToMovie(movieJson);
//        Movie movie1 = movieService.jsonToMovie(movieJson1);


    }
    @Test
    public void testUpdateMovie(){
//        JSONObject updateJson1 = new JSONObject().put("name","Saving Private Ryan")
//                .put("endDate","1998-08-08");
//        JSONObject updateJson2= new JSONObject().put("name","Brokeback Mountain")
//                .put("price",400);
//                Movie update = movieService.updateMovie(updateJson1);
//        Movie update2 = movieService.updateMovie(updateJson2);
    }
    @Test
    public void testFind(){
        JSONObject findJson = new JSONObject()
                .put("rows",1).put("start",2).put("order","price").put("dir","false");



        List<Movie> movies = movieService.multiFind(findJson);
        //Page<Movie> moviepage = movieService.findMulti1(findJson);
        //List<Movie> list = moviepage.getContent();
//        for (Movie movie : list) {
//            System.out.println(movie);
//        }
        for (Movie movie : movies) {
            System.out.println(movie);
        }
//        List<Movie> movies1 = movieRepository.fineMovieByNameLike("m");
//        for (Movie movie : movies1) {
//            System.out.println(movie);
//        }
    }
}
