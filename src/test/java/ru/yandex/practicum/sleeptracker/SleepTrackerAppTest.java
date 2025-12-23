package ru.yandex.practicum.sleeptracker;

import functions.*;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.SleepAnalyticsResult;
import ru.yandex.practicum.sleeptracker.SleepQuality;
import ru.yandex.practicum.sleeptracker.SleepSession;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class SleepTrackerAppTest {

    @Test
    void testTotalSessionsFunction() {
        List<SleepSession> sessions = Arrays.asList(
                new SleepSession(
                        LocalDateTime.of(2025, 10, 1, 22, 15),
                        LocalDateTime.of(2025, 10, 2, 8, 0),
                        SleepQuality.GOOD
                ),
                new SleepSession(
                        LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 8, 0),
                        SleepQuality.NORMAL
                )
        );

        TotalSessionsFunction function = new TotalSessionsFunction();
        SleepAnalyticsResult result = function.analyze(sessions);

        assertEquals("Общее количество сна", result.getDescription());
        assertEquals(2L, result.getValue());
    }

    @Test
    void testMinDurationFunction() {
        List<SleepSession> sessions = Arrays.asList(
                new SleepSession(
                        LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        SleepQuality.GOOD
                ),
                new SleepSession(
                        LocalDateTime.of(2025, 10, 3, 14, 30),
                        LocalDateTime.of(2025, 10, 3, 15, 20),
                        SleepQuality.NORMAL
                )
        );

        MinDurationFunction function = new MinDurationFunction();
        SleepAnalyticsResult result = function.analyze(sessions);

        assertEquals(50L, result.getValue());
    }

    @Test
    void testBadQualitySessionsFunction() {
        List<SleepSession> sessions = Arrays.asList(
                new SleepSession(
                        LocalDateTime.of(2025, 10, 1, 22, 15),
                        LocalDateTime.of(2025, 10, 2, 8, 0),
                        SleepQuality.GOOD
                ),
                new SleepSession(
                        LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 8, 0),
                        SleepQuality.BAD
                ),
                new SleepSession(
                        LocalDateTime.of(2025, 10, 3, 23, 30),
                        LocalDateTime.of(2025, 10, 4, 6, 20),
                        SleepQuality.BAD
                )
        );

        BadQualitySessionsFunction function = new BadQualitySessionsFunction();
        SleepAnalyticsResult result = function.analyze(sessions);

        assertEquals(2L, result.getValue());
    }

    @Test
    void testSleeplessNightsFunction() {
        List<SleepSession> sessions = Arrays.asList(
                new SleepSession(
                        LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        SleepQuality.GOOD
                ),

                new SleepSession(
                        LocalDateTime.of(2025, 10, 2, 14, 0),
                        LocalDateTime.of(2025, 10, 2, 15, 0),
                        SleepQuality.NORMAL
                ),

                new SleepSession(
                        LocalDateTime.of(2025, 10, 3, 23, 0),
                        LocalDateTime.of(2025, 10, 4, 5, 0),
                        SleepQuality.NORMAL
                )
        );

        SleepnessNightFunction function = new SleepnessNightFunction();
        SleepAnalyticsResult result = function.analyze(sessions);

        assertEquals(1L, result.getValue());
    }

    @Test
    void testChronotypeFunctionEarlyBird() {
        List<SleepSession> sessions = Arrays.asList(
                new SleepSession(
                        LocalDateTime.of(2025, 10, 1, 21, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        SleepQuality.GOOD
                ),
                new SleepSession(
                        LocalDateTime.of(2025, 10, 2, 20, 30),
                        LocalDateTime.of(2025, 10, 3, 6, 30),
                        SleepQuality.GOOD
                )
        );

        ChronotypeFunction function = new ChronotypeFunction();
        SleepAnalyticsResult result = function.analyze(sessions);

        assertEquals(ChronotypeFunction.Chronotype.EARLY_BIRD, result.getValue());
    }

    @Test
    void testChronotypeFunctionNightOwl() {
        List<SleepSession> sessions = Arrays.asList(
                new SleepSession(
                        LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 9, 30),
                        SleepQuality.GOOD
                ),
                new SleepSession(
                        LocalDateTime.of(2025, 10, 3, 0, 30),
                        LocalDateTime.of(2025, 10, 2, 10, 0),
                        SleepQuality.GOOD
                )
        );

        ChronotypeFunction function = new ChronotypeFunction();
        SleepAnalyticsResult result = function.analyze(sessions);

        assertEquals(ChronotypeFunction.Chronotype.NIGHT_BIRD, result.getValue());
    }

    @Test
    void testChronotypeFunctionPigeon() {
        List<SleepSession> sessions = Arrays.asList(
                new SleepSession(
                        LocalDateTime.of(2025, 10, 1, 21, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        SleepQuality.GOOD
                ),
                new SleepSession(
                        LocalDateTime.of(2025, 10, 2, 20, 30),
                        LocalDateTime.of(2025, 10, 3, 6, 30),
                        SleepQuality.GOOD
                ),
                new SleepSession(
                        LocalDateTime.of(2025, 10, 3, 23, 30),
                        LocalDateTime.of(2025, 10, 4, 9, 30),
                        SleepQuality.GOOD
                ),
                new SleepSession(
                        LocalDateTime.of(2025, 10, 4, 0, 30),
                        LocalDateTime.of(2025, 10, 4, 10, 0),
                        SleepQuality.GOOD
                )
        );

        ChronotypeFunction function = new ChronotypeFunction();
        SleepAnalyticsResult result = function.analyze(sessions);

        assertEquals(ChronotypeFunction.Chronotype.PIGEON, result.getValue());
    }
}