package ru.yandex.practicum.sleeptracker;

import java.util.List;

@FunctionalInterface
    public interface SleepAnalysisFunction {
    SleepAnalyticsResult analyze(List<SleepSession> sessions);
}
