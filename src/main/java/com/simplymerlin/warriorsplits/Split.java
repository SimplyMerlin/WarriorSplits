package com.simplymerlin.warriorsplits;

import java.time.Duration;
import java.time.Instant;

public class Split {

    String name;
    Duration bestTime;
    Duration bestRelativeTime;

    Duration relativeTime;
    Duration personalBestTime;
    Instant startTime;
    Instant endTime;

    public Split(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void start() {
        startTime = Instant.now();
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void end(Instant relative) {
        endTime = Instant.now();
        Duration time = Duration.between(startTime, endTime);
        if (bestTime == null || time.compareTo(bestTime) < 0) {
            bestTime = time;
        }
        relativeTime = Duration.between(relative, endTime);
        if (bestRelativeTime == null || relativeTime.compareTo(bestRelativeTime) < 0) {
            bestRelativeTime = relativeTime;
        }
    }

    public void reset() {
        startTime = null;
        endTime = null;
        relativeTime = null;
    }

    public Duration getBestTime() {
        return bestTime;
    }

    public Duration getRelativeTime() {
        return relativeTime;
    }

    public Duration getRelativeTime(Instant timerStart) {
        if (relativeTime != null) {
            return relativeTime;
        }
        if (timerStart == null) {
            return Duration.ZERO;
        }
        return Duration.between(timerStart, Instant.now());
    }

    public Duration getBestRelativeTime() {
        return bestRelativeTime;
    }

    public Duration getPersonalBestTime() {
        return personalBestTime;
    }

    public void setPersonalBestTime() {
        this.personalBestTime = relativeTime;
    }

    public boolean hasEnded() {
        return endTime != null;
    }
}
