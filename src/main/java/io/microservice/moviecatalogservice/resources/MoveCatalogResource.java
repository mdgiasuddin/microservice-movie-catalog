package io.microservice.moviecatalogservice.resources;

import io.microservice.moviecatalogservice.models.CatalogItem;
import io.microservice.moviecatalogservice.models.Movie;
import io.microservice.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MoveCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating ratings = restTemplate.getForObject("http://localhost:9083/rating/user/gias", UserRating.class);

        return ratings.getUserRatings().stream().map(rating -> {

            // RestTemplate method
            Movie movie = restTemplate.getForObject("http://localhost:9082/movie/" + rating.getMovieId(), Movie.class);


            // WebClientBuilder method
            /*Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:9082/movie/" + rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();*/

            return new CatalogItem(movie.getName(), "Test", 4);
        }).collect(Collectors.toList());
    }
}
