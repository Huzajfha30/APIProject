package com.cinematracker.cinematracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class MoiveSnapshotDTO {
    private String title;
    private Date releaseDate;
    private double rating;
    private int votes;



}
