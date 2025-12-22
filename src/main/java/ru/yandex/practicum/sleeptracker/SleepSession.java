package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SleepSession {
    private LocalDateTime sleepTime;
    private LocalDateTime wakeTime;
    private SleepQuality quality;

    public SleepSession(LocalDateTime sleepTime, LocalDateTime wakeTime, SleepQuality quality) {
        this.sleepTime = sleepTime;
        this.wakeTime = wakeTime;
        this.quality = quality;
    }

    public LocalDateTime getSleepTime() {
        return sleepTime;
    }

    public LocalDateTime getWakeTime() {
        return wakeTime;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    public long getDurationMinutes() {
        return ChronoUnit.MINUTES.between(sleepTime, wakeTime);
    }

    public boolean isNightTime() {
        return sleepTime.getHour() >= 18 || wakeTime.getHour() <= 12;
    }

    public void setSleepTime(LocalDateTime sleepTime) {
        this.sleepTime = sleepTime;
    }

    public void setWakeTime(LocalDateTime wakeTime) {
        this.wakeTime = wakeTime;
    }

    public void setQuality(SleepQuality quality) {
        this.quality = quality;
    }
}
