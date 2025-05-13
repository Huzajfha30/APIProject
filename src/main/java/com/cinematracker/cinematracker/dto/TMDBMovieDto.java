package com.cinematracker.cinematracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter

public class TMDBMovieDto { //matcher en enkelt film i results[]-listen
    private Long id;
    private String title;

    @JsonProperty("release_date")
    private Date releaseDate;
    @JsonProperty("vote_average")
    private double rating;
    @JsonProperty("vote_count")
    private int voteCount;
    @JsonProperty("poster_path")
    private String posterPath;

}
