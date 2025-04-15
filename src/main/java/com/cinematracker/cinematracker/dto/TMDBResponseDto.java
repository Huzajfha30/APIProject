package com.cinematracker.cinematracker.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter

@Data
public class TMDBResponseDto {
    private int page;
    private List<TMDBMovieDto> results;
    private int total_pages;
    private int total_results;

}
