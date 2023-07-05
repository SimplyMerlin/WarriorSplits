package com.simplymerlin.warriorsplits;

import com.simplymerlin.warriorsplits.course.Course;
import com.simplymerlin.warriorsplits.segment.ComparisonType;
import com.simplymerlin.warriorsplits.segment.Segment;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public class Timer {

    static Timer instance;
    int currentSplit = 0;
    boolean started = false;
    Instant startTime;
    Instant endTime;

    Course currentCourse;
    ComparisonType comparison = ComparisonType.PERSONAL_BEST;

    List<Segment> segments;

    int currentMonth;

    public Timer() {
        instance = this;
        int month = LocalDateTime.now().getMonthValue();
        int yearMonths = (LocalDateTime.now().getYear() - 2023) * 12;
        currentMonth = yearMonths + month - 5;
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
        if (currentCourse != null) {
            segments = currentCourse.segments(comparison);
        }
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
            currentCourse = null;
            return;
        }
        if (currentCourse != null && name.equals(currentCourse.name())) {
            return;
        }
        currentCourse = new Course(name);
        reset();
    }

    public void monthlyCourse(String course) {
        String name = "month/" + currentMonth + "/" + course;
        course(name);
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
