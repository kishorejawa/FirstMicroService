package com.example.moviecatalogservice.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.moviecatalogservice.controller.model.CatalogItem;
import com.example.moviecatalogservice.controller.model.Movie;
import com.example.moviecatalogservice.controller.model.Rating;
import com.example.moviecatalogservice.controller.model.userRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private WebClient.Builder webClientBuilder;
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId")String userId)
	{
		
		String name ="Transformer";
		String desc = "Action Thirller";
		int rating = 8;
		String url="http://localhost:8081/movies/";
		
		//RestTemplate restTemplate = new RestTemplate();
		
		
		userRating ls= restTemplate.getForObject("http://localhost:8082/ratingData/users/"+userId,userRating.class);
				
				/*Arrays.asList(
				new Rating("Ghilli",4),
				new Rating("Thupakki",5),
				new Rating("Kathithi",4),
				new Rating("Villu", 1));*/

		return ls.getUserRating().stream().map(ratings ->{ 
			Movie movie=restTemplate.getForObject(url+ratings.getMovieId(), Movie.class);
			
			/*Movie movie=webClientBuilder.build()
				.get()
				.uri(url+ratings.getMovieId())
				.retrieve()
				.bodyToMono(Movie.class)
				.block();*/
			return new CatalogItem(movie.getMovieId(),desc,ratings.getRating());
		}).collect(Collectors.toList());
				
		
		/*
		 * return Collections.singletonList( new CatalogItem(name,desc,rating ));
		 */
		
	}
}
