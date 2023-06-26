package com.simplymerlin.warriorsplits.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Duration;

public class GsonProvider {

    static Gson gson;

    public static Gson gson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Duration.class, new DurationAdapter());
            gson = builder.create();
        }
        return gson;
    }

}
