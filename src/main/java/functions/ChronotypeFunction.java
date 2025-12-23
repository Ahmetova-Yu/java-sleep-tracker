package functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepAnalyticsResult;
import ru.yandex.practicum.sleeptracker.SleepSession;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChronotypeFunction implements SleepAnalysisFunction {
    public enum Chronotype {
        NIGHT_BIRD("Сова"),
        EARLY_BIRD("Жаворонок"),
        PIGEON("Голубь");

        private final String displayName;

        Chronotype(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    @Override
    public SleepAnalyticsResult analyze(List<SleepSession> sessions) {
        List<SleepSession> validNightSessions = sessions.stream()
                .filter(session -> session.getDurationMinutes() >= 60)
                .filter(this::isNightSession)
                .toList();

        if (validNightSessions.isEmpty()) {
            return new SleepAnalyticsResult("Хронотип пользователя", Chronotype.PIGEON);
        }

        Map<Chronotype, Long> chronotypeCounts = validNightSessions.stream()
                .map(this::classifyNight)
                .collect(Collectors.groupingBy(
                        chronotype -> chronotype,
                        Collectors.counting()
                ));

        Chronotype predominantChronotype = determinePredominantChronotype(chronotypeCounts);

        return new SleepAnalyticsResult("Хронотип пользователя", predominantChronotype);
    }

    private boolean isNightSession(SleepSession session) {
        int sleepHour = session.getSleepTime().getHour();

        return sleepHour >= 18 || sleepHour < 6;
    }

    private Chronotype classifyNight(SleepSession session) {
        LocalTime sleepTime = session.getSleepTime().toLocalTime();
        LocalTime wakeTime = session.getWakeTime().toLocalTime();

        boolean isNightBird;

        if (sleepTime.getHour() >= 0 && sleepTime.getHour() <= 6) {
            isNightBird = wakeTime.isAfter(LocalTime.of(9, 0));

        } else {
            isNightBird = sleepTime.isAfter(LocalTime.of(23, 0)) &&
                    wakeTime.isAfter(LocalTime.of(9, 0));
        }

        boolean isEarlyBird = sleepTime.isBefore(LocalTime.of(22, 0)) &&
                wakeTime.isBefore(LocalTime.of(7, 0));

        if (isNightBird) {
            return Chronotype.NIGHT_BIRD;
        } else if (isEarlyBird) {
            return Chronotype.EARLY_BIRD;
        } else {
            return Chronotype.PIGEON;
        }
    }

    private Chronotype determinePredominantChronotype(Map<Chronotype, Long> counts) {
        long nightBirdCount = counts.getOrDefault(Chronotype.NIGHT_BIRD, 0L);
        long earlyBirdCount = counts.getOrDefault(Chronotype.EARLY_BIRD, 0L);
        long pigeonCount = counts.getOrDefault(Chronotype.PIGEON, 0L);

        long maxCount = Math.max(nightBirdCount, Math.max(earlyBirdCount, pigeonCount));

        int maxCountTypes = 0;
        if (nightBirdCount == maxCount) maxCountTypes++;
        if (earlyBirdCount == maxCount) maxCountTypes++;
        if (pigeonCount == maxCount) maxCountTypes++;

        if (maxCountTypes > 1) {
            return Chronotype.PIGEON;
        }

        if (nightBirdCount == maxCount) {
            return Chronotype.NIGHT_BIRD;
        } else if (earlyBirdCount == maxCount) {
            return Chronotype.EARLY_BIRD;
        } else {
            return Chronotype.PIGEON;
        }
    }
}
