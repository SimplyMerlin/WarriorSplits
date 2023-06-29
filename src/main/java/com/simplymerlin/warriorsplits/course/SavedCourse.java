package com.simplymerlin.warriorsplits.course;

import com.simplymerlin.warriorsplits.segment.SavableSegment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavedCourse {

    private Map<RunType, List<SavableSegment>> runs = new HashMap<>();

    public void putRun(RunType runType, List<SavableSegment> run) {
        runs.put(runType, run);
    }

    public boolean hasRun(RunType runType) {
        return runs.containsKey(runType);
    }

    public List<SavableSegment> getRun(RunType runType) {
        return runs.get(runType);
    }

}
