package Functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepAnalyticsResult;
import ru.yandex.practicum.sleeptracker.SleepSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SleepnessNightFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalyticsResult analyze(List<SleepSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalyticsResult(
                    "Количество бессонных ночей",
                    0
            );
        }

        LocalDate startDate = sessions.getFirst().getSleepTime().toLocalDate();
        LocalDate endDate = sessions.getLast().getSleepTime().toLocalDate();

        if (sessions.getFirst().getSleepTime().getHour() >= 12) {
            startDate = startDate.plusDays(1);
        }

        long totalNights = Period.between(startDate, endDate).getDays();

        Set<LocalDate> nightsWithSleep = new HashSet<>();

        sessions.stream()
                .filter(this::isNightSleep)
                .forEach(session -> {
                    LocalDateTime sleepTime = session.getSleepTime();
                    LocalDateTime wakeTime = session.getWakeTime();

                    LocalDate curDate = sleepTime.toLocalDate();
                    LocalDate wakeDate = wakeTime.toLocalDate();

                    while (!curDate.isAfter(wakeDate)) {
                        if (intersectsNightInterval(sleepTime, wakeTime, curDate)) {
                            nightsWithSleep.add(curDate);
                        }

                        curDate = curDate.plusDays(1);
                    }
                });

        long sleepnessNights = totalNights - nightsWithSleep.size();

        return new SleepAnalyticsResult(
                "Количество бессонных ночей",
                Math.max(0, sleepnessNights)
        );
    }

    private boolean isNightSleep(SleepSession session) {
        LocalDateTime sleepTime = session.getSleepTime();
        LocalDateTime wakeTime = session.getWakeTime();

        LocalDate sleepDate = sleepTime.toLocalDate();
        LocalDate wakeDate = wakeTime.toLocalDate();

        if (!sleepDate.equals(wakeDate)) {
            return true;
        }

        return intersectsNightInterval(sleepTime, wakeTime, sleepDate);
    }

    private boolean intersectsNightInterval(LocalDateTime sleepTime, LocalDateTime wakeTime, LocalDate nightDate) {
        LocalDateTime nightStart = nightDate.atTime(0, 0);
        LocalDateTime nightEnd = nightDate.atTime(6, 0);

        return !(wakeTime.isBefore(nightStart) || sleepTime.isAfter(nightEnd));
    }
}
