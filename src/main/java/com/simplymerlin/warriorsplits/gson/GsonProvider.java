package com.simplymerlin.warriorsplits.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.simplymerlin.warriorsplits.segment.SavableSegment;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.ArrayList;

public class GsonProvider {

    static Gson gson;

    static Type listOfSavableSegment = new TypeToken<ArrayList<SavableSegment>>() {}.getType();

    public static Gson gson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Duration.class, new DurationAdapter());
            gson = builder.create();
        }
        return gson;
    }

    public static Type listOfSavableSegment() {
        return listOfSavableSegment;
    }

}
