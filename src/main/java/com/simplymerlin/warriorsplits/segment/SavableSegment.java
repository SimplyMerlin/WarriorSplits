package com.simplymerlin.warriorsplits.segment;

import java.time.Duration;

public record SavableSegment(
        String name,
        Duration relativeTime,
        Duration length
) {

    public static SavableSegment savableSegment(Segment segment) {
        return new SavableSegment(segment.name(), segment.relativeTime(), segment.length());
    }
    
    public Segment toSegment() {
        return new Segment(name, relativeTime, length);
    }
    
}
