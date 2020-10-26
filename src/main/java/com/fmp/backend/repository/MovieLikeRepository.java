package com.fmp.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fmp.backend.model.Movie;
import com.fmp.backend.model.MovieLike;
import com.fmp.backend.model.User;

public interface MovieLikeRepository extends JpaRepository<MovieLike, Long> {
	List<MovieLike> findByMovieAndUser(Movie movie, User user);
	List<MovieLike> findByUser(User user);
}
