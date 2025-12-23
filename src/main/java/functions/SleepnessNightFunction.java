package functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepAnalyticsResult;
import ru.yandex.practicum.sleeptracker.SleepSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SleepnessNightFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalyticsResult analyze(List<SleepSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalyticsResult("Количество бессонных ночей", 0L);
        }

        LocalDate firstDate = sessions.getFirst().getSleepTime().toLocalDate();
        LocalDate lastDate = sessions.getLast().getWakeTime().toLocalDate();

        if (sessions.getFirst().getSleepTime().getHour() >= 12) {
            firstDate = firstDate.plusDays(1);
        }

        Set<LocalDate> allNights = new HashSet<>();
        LocalDate currentDate = firstDate;
        while (!currentDate.isAfter(lastDate)) {
            allNights.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        Set<LocalDate> nightsWithSleep = new HashSet<>();
        for (SleepSession session : sessions) {
            LocalDateTime sleepTime = session.getSleepTime();
            LocalDateTime wakeTime = session.getWakeTime();

            LocalDate date = sleepTime.toLocalDate();
            while (!date.isAfter(wakeTime.toLocalDate())) {

                LocalDateTime nightStart = date.atTime(0, 0);
                LocalDateTime nightEnd = date.atTime(6, 0);

                if (!(wakeTime.isBefore(nightStart) || sleepTime.isAfter(nightEnd))) {
                    nightsWithSleep.add(date);
                }
                date = date.plusDays(1);
            }
        }

        long sleeplessNights = allNights.stream()
                .filter(night -> !nightsWithSleep.contains(night))
                .count();

        return new SleepAnalyticsResult("Количество бессонных ночей", sleeplessNights);
    }
}
