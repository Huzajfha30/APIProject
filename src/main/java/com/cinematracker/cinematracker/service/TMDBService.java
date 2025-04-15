package com.cinematracker.cinematracker.service;

import com.cinematracker.cinematracker.dto.TMDBResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class TMDBService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.url.now_playing}")
    private String apiUrl;

    public TMDBResponseDto getNowPlayingMovies() {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("api_key", apiKey)
                .queryParam("language", "en-US")
                .queryParam("page", 1)
                .build().toUriString();
        return restTemplate.getForObject(url, TMDBResponseDto.class);
    }
}
