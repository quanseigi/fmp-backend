package com.fmp.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fmp.backend.model.Movie;
import com.fmp.backend.model.MovieLike;
import com.fmp.backend.model.User;
import com.fmp.backend.payload.MovieLikeRequest;
import com.fmp.backend.repository.MovieRepository;
import com.fmp.backend.repository.MovieLikeRepository;
import com.fmp.backend.service.UserService;
import com.fmp.backend.util.JwtTokenUtil;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/movie")
public class MovieController {

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private MovieLikeRepository movieLikeRepository;


	// Get all movies
	@GetMapping("/list")
	public List<Movie> getAllMovies() {

		List<Movie> movies = movieRepository.findAll();
		User user = this.getCurrentUser();
		
		if (user != null) {
			List<MovieLike> movieLikes = movieLikeRepository.findByUser(user);
			//List<Long> movieIds = new ArrayList<Long>;
			
			for (Movie m : movies) {
				for (MovieLike ml : movieLikes) {
					if (ml.getMovie().getId() == m.getId()) {
						m.setIsLiked(ml.getIsLike());
						continue;
					}
				}
			}
		}

		return movies;
	}

	// Share a movie
	@PostMapping("/share")
	public Movie shareMovie(@RequestBody Movie movie) {
		Movie search = movieRepository.findByVideoId(movie.getVideoId());

		if (search == null) {
			User user = this.getCurrentUser();
			if (user != null) {
				movie.setShareBy(user);
				return movieRepository.save(movie);
			}
		}

		return null;
	}

	// Share a movie
	@PostMapping("/like")
	public String likeMovie(@RequestBody MovieLikeRequest likeRequest) {
		Movie movie = movieRepository.findById(likeRequest.getMovieId()).orElse(null);
		User user = this.getCurrentUser();
		if (user != null && movie != null) {
			List<MovieLike> movieUserLikes = movieLikeRepository.findByMovieAndUser(movie, user);
			if (movieUserLikes.isEmpty()) {
				MovieLike like = new MovieLike(user, movie, likeRequest.getIsLike());
				movieLikeRepository.save(like);
			}
		}

		return null;
	}

	private User getCurrentUser() {
		String token = JwtTokenUtil.getToken();
		if (token != null) {
			User user = userService.getUserFromJwt(token);
			return user;
		}
		return null;
	}

}
