package com.fmp.backend.payload;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class MovieLikeRequest {

	@NotBlank
    private Long movieId;

    @NotBlank
    private Boolean isLike;

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	public Boolean getIsLike() {
		return isLike;
	}

	public void setIsLike(Boolean isLike) {
		this.isLike = isLike;
	}
}
