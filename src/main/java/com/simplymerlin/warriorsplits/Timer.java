package com.simplymerlin.warriorsplits;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Timer {

    static Timer instance;

    int currentSplit = 0;
    List<Split> splits;

    boolean started = false;
    Instant startTime;
    Instant endTime;
    Duration bestTime;

    public Timer() {
        instance = this;
        splits = new ArrayList<>();
        for (int x = 1; x < 4; x++) {
            for (int y = 1; y < 4; y++) {
                splits.add(new Split("[M" + x + "-" + y + "]"));
            }
        }
        splits.add(new Split("[B4-2]"));
    }

    public static Timer getInstance() {
        return instance;
    }

    public void startTimer() {
        resetTimer();
        startTime = Instant.now();
        splits.get(currentSplit).start();
        started = true;
    }

    public boolean isStarted() {
        return started;
    }

    public void endTimer() {
        endTime = Instant.now();
        started = false;
    }

    public void resetTimer() {
        if (endTime != null) {
            Duration time = Duration.between(startTime, endTime);
            if (bestTime == null || time.compareTo(bestTime) < 0) {
                bestTime = time;
                for (Split split : splits) {
                    split.setPersonalBestTime();
                }
            }
        }
        for (Split split : splits) {
            split.reset();
        }
        startTime = null;
        endTime = null;
        started = false;
        currentSplit = 0;
    }

    public void split() {
        splits.get(currentSplit).end(startTime);
        if (splits.size() == currentSplit + 1) {
            endTimer();
            return;
        }
        ++currentSplit;
        splits.get(currentSplit).start();
    }

    public List<Split> getSplits() {
        return splits;
    }

    public Duration getTime() {
        if (endTime != null) {
            return Duration.between(startTime, endTime);
        }
        if (!started) {
            return Duration.ZERO;
        }
        return Duration.between(startTime, Instant.now());
    }

    public Instant getStartTime() {
        return startTime;
    }

    public int getCurrentSplit() {
        return currentSplit;
    }
}
