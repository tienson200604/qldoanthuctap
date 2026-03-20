package com.web.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreComponentRequest {

    private Long id;

    private Float point;

    private Long scoreRatioId;

    private Long studentRegisId;
}
