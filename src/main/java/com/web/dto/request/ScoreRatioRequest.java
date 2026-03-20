package com.web.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreRatioRequest {

    private Long id;

    private String name;

    private Float percent;

    private Long semesterId;
}