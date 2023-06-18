package com.simplymerlin.warriorsplits;

import java.time.Duration;

public class utils {

    public static String durationToString(Duration duration) {
        String minutes = (duration.toMinutesPart() < 10 ? "0" : "") + duration.toMinutesPart();
        String seconds = (duration.toSecondsPart() < 10 ? "0" : "") + duration.toSecondsPart();
        String millis = (duration.toMillisPart() < 10 ? "0" : "") + (duration.toMillisPart() < 100 ? "0" : "") + duration.toMillisPart();

        return minutes + ":" + seconds + "." + millis;
    }

}
