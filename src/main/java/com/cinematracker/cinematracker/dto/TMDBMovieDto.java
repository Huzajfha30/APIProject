package com.cinematracker.cinematracker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyy-MM-dd")
    private Date releaseDate;
    @JsonProperty("vote_average")
    private Double rating;
    @JsonProperty("vote_count")
    private Integer votes;
}
