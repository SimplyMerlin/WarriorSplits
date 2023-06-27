package com.simplymerlin.warriorsplits.course;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.simplymerlin.warriorsplits.gson.GsonProvider;
import com.simplymerlin.warriorsplits.segment.SavableSegment;
import com.simplymerlin.warriorsplits.segment.Segment;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
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
                segments = savedCourse.getRun(runType).pb();
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
        savedCourse.putPB(runType, segments);

        File file = FabricLoader.getInstance().getConfigDir().resolve("warriorsplits/" + name + ".json").toFile();
        boolean mkdir = file.getParentFile().mkdirs();
        try (var fw = new FileWriter(file)) {
            fw.write(GsonProvider.gson().toJson(savedCourse));
            fw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Segment> segments() {
        return segments.stream().map(SavableSegment::toSegment).toList();
    }

    public String name() {
        return name;
    }
}
