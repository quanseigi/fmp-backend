package com.fmp.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fmp.backend.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
	Movie findByVideoId(String videoId);
	Optional<Movie> findById(Long Id);
}