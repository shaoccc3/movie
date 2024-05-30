package com.ispan.theater.service;

import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.MoviePicture;
import com.ispan.theater.dto.MoviePicDto;
import com.ispan.theater.repository.MoviePictureRepository;
import com.ispan.theater.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MoviePictureService {
    @Autowired
    private MoviePictureRepository moviePictureRepository;
    @Autowired
    private MovieRepository movieRepository;
    public List<MoviePicDto> getMoviePictures(Integer movieId) {
        return moviePictureRepository.findByMovieId(movieId);
    }
    public boolean insertMoviePicture(Map<String,Byte[]> pictures, Integer movieId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if( movie !=null){
            for(Map.Entry<String, Byte[]> entry : pictures.entrySet()){
                MoviePicture moviePicture = new MoviePicture();
                moviePicture.setMovie(movie);
                moviePicture.setPicture(entry.getValue());
                moviePicture.setFilename(entry.getKey());
                moviePictureRepository.save(moviePicture);
            }
            return true;
        }
        return false;
    }
    public void deleteMoviePicture(List<Integer> mpIds) {
        for(Integer mpId : mpIds){
            moviePictureRepository.deleteById(mpId);
        }
    }
}
