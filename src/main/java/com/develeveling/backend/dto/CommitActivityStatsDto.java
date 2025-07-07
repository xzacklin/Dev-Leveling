package com.develeveling.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CommitActivityStatsDto(
        int total,
        long week,
        List<Integer> days
) {}