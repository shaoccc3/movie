package com.ispan.theater.controller;

import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.Screening;
import com.ispan.theater.service.MovieService;
import com.ispan.theater.service.ScreeningService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
public class ScreeningAjaxController {
    @Autowired
    private ScreeningService screeningService;
    @Autowired
    private MovieService movieService;

    @PostMapping("/backstage/screening/find")
    public String findScreening(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray array = new JSONArray();
        Page<Screening> result = screeningService.findScreenings(jsonObject);
        List<Screening> screenings = result.getContent();
        if(!screenings.isEmpty()) {
            for(Screening screening : screenings) {
                JSONObject screeningObject = screeningService.screenToJSON(screening);
                array.put(screeningObject);
            }
        }
        jsonObject.put("list", array);
        jsonObject.put("count", result.getTotalElements());
        return jsonObject.toString();
    }
    @GetMapping("/backstage/screening/{movieId}")
    public String screeningByMovieId(@PathVariable Integer movieId) {
        JSONObject response = new JSONObject();
        JSONArray array = new JSONArray();
        List<Screening> result = screeningService.findScreeningsByMovieId(movieId);
        if(!result.isEmpty()) {
            response.put("list", array);
            response.put("count", result.size());
            response.put("success", true);
            return response.toString();
        }
        response.put("noresult",true);
        return response.toString();
    }
    @PostMapping("/backstage/screening")
    public String insertScreening(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject response = new JSONObject();
        if (jsonObject.has("movieId")) {
            Movie movie = movieService.getMovieById(jsonObject.getInt("movieId"));
            Screening screening = screeningService.createScreening(movie, jsonObject);
            response.put("success", true);
            response.put("message", screening.toString());
        }
        else{
            response.put("fail", false);
            response.put("message", "電影不存在");
        }
        return response.toString();
    }
}
