package com.ispan.theater.service;

import com.ispan.theater.domain.Category;
import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.Rated;
import com.ispan.theater.repository.*;
import com.ispan.theater.util.DatetimeConverter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MovieActRepository movieActRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private RatedRepository ratedRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }//test passed
    public Movie getMovieById(Integer id) {//test passed
        Optional<Movie> optional = movieRepository.findById(id);
        return optional.orElse(null);
    }
    public List<Movie> getMovieByName(String name,Integer page) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("start", 10*page);
        jsonObject.put("rows", 10);
        return movieRepository.multiConditionFindMovie(jsonObject);
    }
    public Movie jsonToMovie(JSONObject jsonObject) {//test passed
        Movie movie = null;
        if (movieRepository.findByName(jsonObject.getString("name")) == null) {
            movie = new Movie();
            movie.setName(jsonObject.getString("name"));
            movie.setDescription(jsonObject.getString("description"));
            movie.setDirector(jsonObject.getString("director"));
            movie.setCategoryCode(jsonObject.getString("categoryCode"));
            movie.setDuration(jsonObject.getInt("duration"));
            movie.setRatedCode(ratedRepository.findByCode(jsonObject.getString("ratedCode")));
            movie.setReleaseDate(DatetimeConverter.parse(jsonObject.getString("releaseDate"), "yyyy-MM-dd"));
            movie.setEndDate(DatetimeConverter.parse(jsonObject.getString("endDate"), "yyyy-MM-dd"));
            movie.setCreateDate(new Date());
            movie.setModifyDate(new Date());
            movie.setViewer(0);
            movieRepository.save(movie);
            return movie;

        } else {
            return null;
        }
    }
    public List<Movie> multiFind(JSONObject jsonObject) {//test passed
        if (movieRepository.multiConditionFindMovie(jsonObject)!= null) {
            return movieRepository.multiConditionFindMovie(jsonObject);
        }
        else{
            return null;
        }

    }
    public Movie updateMovie(JSONObject jsonObject) {//test pass
        String name=jsonObject.isNull("name")?null :jsonObject.getString("name");
        String description=jsonObject.isNull("description")?null :jsonObject.getString("description");
        String director=jsonObject.isNull("director")?null :jsonObject.getString("director");
        String releaseDate=jsonObject.isNull("releaseDate")?null :jsonObject.getString("releaseDate");
        String endDate=jsonObject.isNull("endDate")?null :jsonObject.getString("endDate");
        Double price=jsonObject.isNull("price")?null :jsonObject.getDouble("price");
        String category=jsonObject.isNull("category")?null :jsonObject.getString("category");
        String rated=jsonObject.isNull("rated")?null :jsonObject.getString("rated");
        Integer duration=jsonObject.isNull("duration")?null :jsonObject.getInt("duration");
        if(movieRepository.findByName(name) == null) {
            return null;
        }
        Movie movie = movieRepository.findByName(name);
        if(director!=null && director.length()>0) {
            movie.setDirector(director);
        }
        if(description!=null && description.length()>0) {
            movie.setDescription(description);
        }
        if(releaseDate!=null && releaseDate.length()>0) {
            movie.setReleaseDate(DatetimeConverter.parse(releaseDate, "yyyy-MM-dd"));
        }
        if(endDate!=null && endDate.length()>0) {
            movie.setEndDate(DatetimeConverter.parse(endDate, "yyyy-MM-dd"));
        }
        if(price!=null && price.doubleValue()>0) {
            movie.setPrice(price);
        }
        if(category!=null && category.length()>0) {
            movie.setCategoryCode(category);
        }
        if(rated!=null && rated.length()>0) {
            Rated temp = ratedRepository.findByCode(rated);
            movie.setRatedCode(temp);
        }
        if(duration!=null && duration>0) {
            movie.setDuration(duration);
        }
        movie.setModifyDate(new Date());
        return movieRepository.save(movie);
    }
    public void deleteMovie(Movie movie) {
        movieRepository.delete(movie);
    }
    public JSONObject movieToJson(Movie movie) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObject.put("name", movie.getName());
        jsonObject.put("description", movie.getDescription());
        jsonObject.put("director", movie.getDirector());
        jsonObject.put("releaseDate", movie.getReleaseDate());
        jsonObject.put("endDate", movie.getEndDate());
        jsonObject.put("price", movie.getPrice());
        String rateStr = movie.getCategoryCode();
        String[] cateArray = rateStr.split(",");
        for(String cate : cateArray) {
            Category temp = categoryRepository.findByCode(cate);
            if(temp!=null) {
                jsonArray.put(temp.toString());
            }
        }
        jsonObject.put("category",jsonArray);
        jsonObject.put("rated", movie.getRatedCode());
        jsonObject.put("duration", movie.getDuration());
        return jsonObject;
    }
    public long count(String name){
        return movieRepository.countByNameLike(name);
    }


}
