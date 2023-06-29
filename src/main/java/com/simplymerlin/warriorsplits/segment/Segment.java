package com.simplymerlin.warriorsplits.segment;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class Segment {

    private final String name;
    private Duration relativeTime;
    private Instant startTime;
    private Instant endTime;

    private final Map<ComparisonType, Comparison> comparisons;
    private Comparison activeComparison;
    private ComparisonType comparisonType = ComparisonType.PERSONAL_BEST;

    public Segment(String name, Map<ComparisonType, Comparison> comparisons) {
        this.name = name;
        this.comparisons = comparisons;
        loadComparison();
    }

    public Segment(String name, Map<ComparisonType, Comparison> comparisons, ComparisonType comparisonType) {
        this.name = name;
        this.comparisons = comparisons;
        this.comparisonType = comparisonType;
        loadComparison();
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
        if (endTime == null)
            return null;

        return Duration.between(startTime, endTime);
    }

    public boolean ended() {
        return endTime != null;
    }

    public Duration comparisonLength() {
        return activeComparison.length();
    }

    public Duration comparisonRelativeTime() {
        return activeComparison.relativeTime();
    }

    public Map<ComparisonType, Comparison> comparisons() {
        return comparisons;
    }

    public Comparison comparison(ComparisonType type) {
        var comparison = comparisons.get(type);
        if (comparison == null)
            comparison = new Comparison(null, null);
        return comparison;
    }

    public ComparisonType comparisonType() {
        return comparisonType;
    }

    public void comparisonType(ComparisonType comparisonType) {
        this.comparisonType = comparisonType;
        loadComparison();
    }

    private void loadComparison() {
        if (!comparisons.containsKey(comparisonType)) {
            activeComparison = new Comparison(null, null);
            return;
        }
        activeComparison = comparisons.get(comparisonType);
    }
}
