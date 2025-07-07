package com.develeveling.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

// This set of records maps the nested structure of the GitHub GraphQL response.
@JsonIgnoreProperties(ignoreUnknown = true)
public record ContributionCalendarDto(
        Data data
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(User user) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record User(ContributionsCollection contributionsCollection) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ContributionsCollection(ContributionCalendar contributionCalendar) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ContributionCalendar(
            int totalContributions,
            List<Week> weeks
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Week(List<ContributionDay> contributionDays) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ContributionDay(
            int contributionCount,
            String date
    ) {}
}