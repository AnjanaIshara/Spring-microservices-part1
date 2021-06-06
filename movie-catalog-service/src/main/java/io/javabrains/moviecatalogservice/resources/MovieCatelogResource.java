package io.javabrains.moviecatalogservice.resources;


import io.javabrains.moviecatalogservice.models.CatelogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catelog")
public class MovieCatelogResource {


    @Autowired
    private RestTemplate restTemplate=new RestTemplate();
    @RequestMapping("/{userId}")
    public List<CatelogItem> getCatalog(@PathVariable("userId") String userId){



        //WebClient.Builder builder=WebClient.builder();

        //get all rated movie ids
        UserRating ratings= restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/"+userId,UserRating.class);
        return ratings.getUserRating().stream().map(rating -> {
            //Rest template
            Movie movie=restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
            //Web client way
            //Movie movie=builder.build().get().uri("http://movie-info-service/movies/"+rating.getMovieId()).retrieve().bodyToMono(Movie.class).block();
            return new CatelogItem(movie.getName(),"Test",rating.getRating());
        }).collect(Collectors.toList());
        //for each movie ID, call movie info service and get details

        //put them alltogether



    }
}
