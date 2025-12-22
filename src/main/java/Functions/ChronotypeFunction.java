package Functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepAnalyticsResult;
import ru.yandex.practicum.sleeptracker.SleepSession;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class ChronotypeFunction implements SleepAnalysisFunction {
    public enum Chronotype {
        EARLY_BIRD("Жаворонок"),
        NIGHT_BIRD("Сова"),
        PIGEON("Голубь");

        private final String name;

        Chronotype (String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    public SleepAnalyticsResult analyze(List<SleepSession> sessions) {
        List<SleepSession> nightSessions = sessions.stream()
                .filter(this::isNightForChronotype)
                .toList();

        Map<Chronotype, Long> chronotypeCounts = nightSessions.stream()
                .map(this::classifyNight)
                .collect(Collectors.groupingBy(
                        chronotype -> chronotype, Collectors.counting()
                ));

        Chronotype main = determinMainChronotype(chronotypeCounts);

        return new SleepAnalyticsResult(
                "Преобладающий хронотип",
                main
        );
    }

    private boolean isNightForChronotype(SleepSession session) {
        LocalDateTime sleepTime = session.getSleepTime();
        LocalDateTime wakeTime = session.getWakeTime();

        if (session.getDurationMinutes() < 60) {
            return false;
        }

        int sleepHour = sleepTime.getHour();
        int wakeHour = wakeTime.getHour();

        return (sleepHour >= 18 || sleepHour <= 6) &&
                (wakeHour >= 5 || wakeHour <= 12);
    }

    private Chronotype classifyNight(SleepSession session) {
        int sleepHour = session.getSleepTime().getHour();
        int wakeHour = session.getWakeTime().getHour();

        if (sleepHour < 22 && wakeHour < 7) {
            return Chronotype.EARLY_BIRD;
        }

        if (sleepHour >= 23 && wakeHour >= 9) {
            return Chronotype.NIGHT_BIRD;
        }

        return Chronotype.PIGEON;
    }

    private Chronotype determinMainChronotype(Map<Chronotype, Long> counts) {
        Map<Chronotype, Long> allCounts = new HashMap<>();

        allCounts.put(Chronotype.EARLY_BIRD, counts.getOrDefault(Chronotype.EARLY_BIRD, 0L));
        allCounts.put(Chronotype.NIGHT_BIRD, counts.getOrDefault(Chronotype.NIGHT_BIRD, 0L));
        allCounts.put(Chronotype.PIGEON, counts.getOrDefault(Chronotype.PIGEON, 0L));

        long maxCount = allCounts.values().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);

        long maxCountOccurrences = allCounts.values().stream()
                .filter(count -> count == maxCount)
                .count();

        if (maxCountOccurrences > 1) {
            return Chronotype.PIGEON;
        }

        return allCounts.entrySet().stream()
                .filter(entry -> entry.getValue() == maxCount)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(Chronotype.PIGEON);
    }
}
