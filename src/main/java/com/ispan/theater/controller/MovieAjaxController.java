package com.ispan.theater.controller;

import com.ispan.theater.domain.Movie;
import com.ispan.theater.repository.AuditoriumLevelRepository;
import com.ispan.theater.service.MovieService;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@CrossOrigin
public class MovieAjaxController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private AuditoriumLevelRepository auditoriumLevelRepository;

    @PostMapping("/backstage/movie/find")//test passed
    public String findMovie(@RequestBody String json){
        JSONObject jsonObject = new JSONObject(json);
        JSONObject response = new JSONObject();

        Page<Movie> result = movieService.findMulti1(jsonObject);
        List<Movie> movies = result.getContent();
        long count = result.getTotalElements();
        JSONArray array = new JSONArray();
        if(!movies.isEmpty()){
            for(Movie m : movies){
                JSONObject movie = movieService.movieToJson(m);
                array.put(movie);
            }
        }
        response.put("list", array);
        response.put("count" , count);
        return response.toString();
    }
    @PostMapping("/backstage/movie/uploadPhoto")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {//測試用
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
    @PostMapping("/backstage/movie")
    public String insertMovie(@RequestBody String moviestr){
        JSONObject jsonObject = new JSONObject(moviestr);
        JSONObject response = new JSONObject();
        if(movieService.existsMovieByName(jsonObject.getString("name"))){
            Movie movie = movieService.jsonToMovie(jsonObject);
            response.put("msg", "新增成功");
            response.put("succeed","succeed");
        }
        else{
            response.put("msg", "新增失敗");
            response.put("fail","fail");
        }
        return response.toString();


    }
    @PutMapping("/backstage/movie")
    public String updateMovie(@RequestBody String moviestr){
        JSONObject jsonObject = new JSONObject(moviestr);
        JSONObject response = new JSONObject();
        if(movieService.getMovieById(jsonObject.getInt("id"))!=null){
            Movie movie = movieService.updateMovie(jsonObject);
            response.put("msg", "更新成功");
            response.put("succeed","succeed");
        }
        else{
            response.put("msg", "更新失敗");
            response.put("fail","fail");
        }
        return response.toString();

    }
    @GetMapping("/backstage/movie/photo/{id}")
    public String getMoviePhoto(@PathVariable("id") Integer id){
        Movie movie = movieService.getMovieById(id);
        String photoBase64;
        if(movie != null){
            photoBase64 = movie.getImage();
            return photoBase64;
        } else {
            return null;
        }
    }

}
