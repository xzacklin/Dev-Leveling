package com.develeveling.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubRepoDto(
        String name,
        @JsonProperty("created_at")
        ZonedDateTime createdAt
) {}