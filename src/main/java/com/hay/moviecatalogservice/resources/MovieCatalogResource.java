package com.hay.moviecatalogservice.resources;

import com.hay.moviecatalogservice.models.CatalogItem;
import com.hay.moviecatalogservice.models.Movie;
import com.hay.moviecatalogservice.models.Rating;
import com.hay.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating userRating = restTemplate.getForObject(
                "http://ratings-data-service/ratingsdata/users/" + userId,
                UserRating.class
        );

        return userRating.getUserRating().stream().map(rating -> {
            Movie movie = restTemplate.getForObject(
                    "http://movie-info-service/movies/" + rating.getMovieId(), Movie.class
            );
            return new CatalogItem(movie.getName(), "Desc", rating.getRating());
        })
        .collect(Collectors.toList());


    }

}

//            Movie movie = webClientBuilder.build()
//                    .get()
//                    .uri("http://localhost:8082/movies/" + rating.getMovieId())
//                    .retrieve()
//                    .bodyToMono(Movie.class)
//                    .block();