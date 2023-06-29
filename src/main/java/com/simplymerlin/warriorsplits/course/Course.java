package com.simplymerlin.warriorsplits.course;

import com.simplymerlin.warriorsplits.gson.GsonProvider;
import com.simplymerlin.warriorsplits.segment.ComparisonType;
import com.simplymerlin.warriorsplits.segment.SavableSegment;
import com.simplymerlin.warriorsplits.segment.Comparison;
import com.simplymerlin.warriorsplits.segment.Segment;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Course {

    String name;
    List<SavableSegment> segments = new ArrayList<>();

    RunType runType = RunType.STANDARD;
    File file;
    SavedCourse savedCourse;

    public Course(String name) {
        this.name = name;
        file = FabricLoader.getInstance().getConfigDir().resolve("warriorsplits/" + name + ".json").toFile();
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                savedCourse = GsonProvider.gson().fromJson(reader, SavedCourse.class);
                if (!savedCourse.hasRun(runType)) {
                    generateSegments();
                    return;
                }
                segments = savedCourse.getRun(runType);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            generateSegments();
            savedCourse = new SavedCourse();
        }
    }

    private void generateSegments() {
        for (int x = 1; x < 4; x++) {
            for (int y = 1; y < 4; y++) {
                segments.add(SavableSegment.of("[M" + x + "-" + y + "]"));
            }
        }
        segments.add(SavableSegment.of("[B4-1]"));
    }

    public void save(List<Segment> newSegments) {
        // PB
        Duration runTime = newSegments.get(newSegments.size() - 1).relativeTime();
        var savedRun = segments.get(segments.size() - 1).getComparison(ComparisonType.PERSONAL_BEST);
        if (savedRun == null || runTime.compareTo(savedRun.relativeTime()) < 0) {
            int i = 0;
            for (Segment newSegment : newSegments) {
                segments.get(i).putComparison(ComparisonType.PERSONAL_BEST, Comparison.of(newSegment));
                i++;
            }
        }

        // GOLD
        int i = 0;
        for (Segment newSegment : newSegments) {
            var oldSegment = segments.get(i).getComparison(ComparisonType.GOLD);
            if (oldSegment == null || newSegment.length().compareTo(oldSegment.length()) < 0 ) {
                segments.get(i).putComparison(ComparisonType.GOLD, Comparison.of(newSegment));
            }
            i++;
        }

        savedCourse.putRun(runType, segments);

        File file = FabricLoader.getInstance().getConfigDir().resolve("warriorsplits/" + name + ".json").toFile();
        boolean mkdir = file.getParentFile().mkdirs();
        try (var fw = new FileWriter(file)) {
            fw.write(GsonProvider.gson().toJson(savedCourse));
            fw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Segment> segments(ComparisonType type) {
        return segments.stream().map(segment -> segment.toSegment(type)).toList();
    }

    public String name() {
        return name;
    }
}
