package com.simplymerlin.warriorsplits;

import com.simplymerlin.warriorsplits.course.Course;
import com.simplymerlin.warriorsplits.segment.Comparison;
import com.simplymerlin.warriorsplits.segment.ComparisonType;
import com.simplymerlin.warriorsplits.segment.Segment;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Timer {

    static Timer instance;
    int currentSplit = 0;
    boolean started = false;
    Instant startTime;
    Instant endTime;

    Map<String, Course> loadedCourses = new HashMap<>();
    Course currentCourse;
    ComparisonType comparison = ComparisonType.PERSONAL_BEST;

    List<Segment> segments;

    public Timer() {
        instance = this;
    }

    public static Timer instance() {
        return instance;
    }

    public void start() {
        reset();
        startTime = Instant.now();
        segments.get(currentSplit).start(startTime);
        started = true;
    }

    public boolean started() {
        return started;
    }

    public void end() {
        endTime = Instant.now();
        started = false;
        currentCourse.save(segments);
    }

    public void reset() {
        segments = currentCourse.segments(comparison);
        startTime = null;
        endTime = null;
        started = false;
        currentSplit = 0;
    }

    public void split() {
        // By doing this, we assure that internally the old split ends and new split starts at the exact same time!
        Instant endTime = segments.get(currentSplit).end(startTime);
        if (segments.size() == currentSplit + 1) {
            end();
            return;
        }
        ++currentSplit;
        segments.get(currentSplit).start(endTime);
    }

    public Course course() {
        return currentCourse;
    }

    public void course(String name) {
        if (name == null) {
            reset();
            currentCourse = null;
            return;
        }
        if (currentCourse != null && name.equals(currentCourse.name())) {
            return;
        }
        if (loadedCourses.containsKey(name)) {
            currentCourse = loadedCourses.get(name);
        } else {
            currentCourse = new Course(name);
            loadedCourses.put(name, currentCourse);
        }
        reset();
    }

    public List<Segment> segments() {
        return segments;
    }

    public Duration time() {
        if (endTime != null) {
            return Duration.between(startTime, endTime);
        }
        if (!started) {
            return Duration.ZERO;
        }
        return Duration.between(startTime, Instant.now());
    }

    public Instant startTime() {
        return startTime;
    }

    public int currentSplit() {
        return currentSplit;
    }

    public ComparisonType comparison() {
        return comparison;
    }

    public void comparison(ComparisonType type) {
        this.comparison = type;
        for (Segment segment : segments) {
            segment.comparisonType(type);
        }
    }
}
