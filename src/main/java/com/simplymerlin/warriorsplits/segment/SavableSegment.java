package com.simplymerlin.warriorsplits.segment;

import java.util.HashMap;
import java.util.Map;

public record SavableSegment(
        String name,
        Map<ComparisonType, Comparison> splits
) {

    public static SavableSegment of(Segment segment) {
        return new SavableSegment(segment.name(), segment.comparisons());
    }

    public static SavableSegment of(String name) {
        return new SavableSegment(name, new HashMap<>());
    }
    
    public Segment toSegment(ComparisonType type) {
        return new Segment(name, Map.copyOf(splits), type);
    }

    public Comparison getComparison(ComparisonType type) {
        return splits.get(type);
    }

    public void putComparison(ComparisonType type, Comparison split) {
        splits.put(type, split);
    }
    
}
