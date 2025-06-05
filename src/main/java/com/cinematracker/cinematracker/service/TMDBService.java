package com.cinematracker.cinematracker.service;

import com.cinematracker.cinematracker.dto.TMDBMovieDto;
import com.cinematracker.cinematracker.dto.TMDBResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TMDBService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${tmdb.api.key}")
    private String apiKey;


    @Value("${tmdb.api.url.upcoming}")
    private String upcomingUrl;



    public TMDBResponseDto getUpcomingMovies() {
        String url = UriComponentsBuilder.fromHttpUrl(upcomingUrl)
                .queryParam("api_key", apiKey)
                .queryParam("language", "da-DK") // Match sprog!
                .queryParam("region", "DK")     // Match land!
                .queryParam("page", 1)
                .build().toUriString();

        return restTemplate.getForObject(url, TMDBResponseDto.class);
    }



}


