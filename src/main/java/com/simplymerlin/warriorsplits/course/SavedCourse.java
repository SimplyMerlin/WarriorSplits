package com.simplymerlin.warriorsplits.course;

import com.simplymerlin.warriorsplits.segment.SavableSegment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavedCourse {

    private Map<RunType, Run> runs = new HashMap<>();

    public void putRun(RunType runType, Run run) {
        runs.put(runType, run);
    }

    public void putRun(RunType runType, List<SavableSegment> pb, List<SavableSegment> gold) {
        putRun(runType, new Run(pb, gold));
    }

    public void putPB(RunType runType, List<SavableSegment> pb) {
        if (hasRun(runType)) {
            putRun(runType, pb, getRun(runType).gold());
        } else {
            putRun(runType, pb, null);
        }
    }

    public void putGold(RunType runType, List<SavableSegment> gold) {
        if (hasRun(runType)) {
            putRun(runType, getRun(runType).pb(), gold);
        } else {
            putRun(runType, null, gold);
        }
    }

    public boolean hasRun(RunType runType) {
        return runs.containsKey(runType);
    }

    public Run getRun(RunType runType) {
        return runs.get(runType);
    }

    public record Run(
            List<SavableSegment> pb,
            List<SavableSegment> gold
    ) {

    }

}
