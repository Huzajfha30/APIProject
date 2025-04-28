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

    public int getPage() {
        return page;
    }

    public List<TMDBMovieDto> getResults() {
        return results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setResults(List<TMDBMovieDto> results) {
        this.results = results;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }
}
