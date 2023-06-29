package com.simplymerlin.warriorsplits.segment;

import java.time.Duration;

public record Comparison(
        Duration relativeTime,
        Duration length
) {

    public static Comparison of(Segment segment) {
        return new Comparison(segment.relativeTime(), segment.length());
    }

}
