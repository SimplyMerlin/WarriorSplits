package com.simplymerlin.warriorsplits.course;

import com.google.gson.JsonObject;
import com.simplymerlin.warriorsplits.gson.GsonProvider;
import com.simplymerlin.warriorsplits.segment.SavableSegment;
import com.simplymerlin.warriorsplits.segment.Segment;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Course {

    String name;
    List<SavableSegment> segments = new ArrayList<>();

    public Course(String name) {
        this.name = name;
        for (int x = 1; x < 4; x++) {
            for (int y = 1; y < 4; y++) {
                segments.add(new SavableSegment("[M" + x + "-" + y + "]", null, null));
            }
        }
        segments.add(new SavableSegment("[B4-1]", null, null));
    }

    public void save(List<Segment> newSegments) {
        Duration runTime = newSegments.get(newSegments.size() - 1).relativeTime();
        Duration savedRunTime = segments.get(segments.size() - 1).relativeTime();
        if (savedRunTime == null || runTime.compareTo(savedRunTime) < 0) {
            segments.clear();
            for (Segment newSegment : newSegments) {
                segments.add(SavableSegment.savableSegment(newSegment));
            }
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("pb", GsonProvider.gson().toJsonTree(segments));
        System.out.println(jsonObject);
    }

    public List<Segment> segments() {
        return segments.stream().map(SavableSegment::toSegment).toList();
    }

    public String name() {
        return name;
    }
}
