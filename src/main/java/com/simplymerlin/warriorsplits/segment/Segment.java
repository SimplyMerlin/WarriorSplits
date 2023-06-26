package com.simplymerlin.warriorsplits.segment;

import java.time.Duration;
import java.time.Instant;

public class Segment {

    private final String name;
    private Duration relativeTime;
    private Instant startTime;
    private Instant endTime;

    private Duration comparisonRelativeTime;
    private Duration comparisonLength;

    public Segment(String name) {
        this.name = name;
    }

    public Segment(String name, Duration comparisonRelativeTime, Duration comparisonLength) {
        this.name = name;
        this.comparisonRelativeTime = comparisonRelativeTime;
        this.comparisonLength = comparisonLength;
    }

    public String name() {
        return name;
    }

    public Instant startTime() {
        return startTime;
    }

    public void start() {
        startTime = Instant.now();
    }

    public void start(Instant instant) {
        startTime = instant;
    }

    public Instant endTime() {
        return endTime;
    }

    public Instant end(Instant relative) {
        endTime = Instant.now();
        relativeTime = Duration.between(relative, endTime);
        return endTime;
    }

    public void reset() {
        startTime = null;
        endTime = null;
        relativeTime = null;
    }

    public Duration relativeTime() {
        return relativeTime;
    }

    public Duration relativeTime(Instant timerStart) {
        if (relativeTime != null) {
            return relativeTime;
        }
        if (timerStart == null) {
            return Duration.ZERO;
        }
        return Duration.between(timerStart, Instant.now());
    }

    public Duration length() {
        return Duration.between(startTime, endTime);
    }

    public boolean ended() {
        return endTime != null;
    }

    public Duration comparisonLength() {
        return comparisonLength;
    }

    public Duration comparisonRelativeTime() {
        return comparisonRelativeTime;
    }

    public void comparisonLength(Duration comparisonLength) {
        this.comparisonLength = comparisonLength;
    }

    public void comparsionRelativeTime(Duration comparisonRelativeTime) {
        this.comparisonRelativeTime = comparisonRelativeTime;
    }
}
