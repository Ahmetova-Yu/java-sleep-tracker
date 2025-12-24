package functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepAnalyticsResult;
import ru.yandex.practicum.sleeptracker.SleepSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        Set<LocalDate> allNights = Stream.iterate(firstDate, date -> !date.isAfter(lastDate), date -> date.plusDays(1))
                .collect(Collectors.toSet());

        Set<LocalDate> nightsWithSleep = sessions.stream()
                .flatMap(session -> {
                    LocalDateTime sleepTime = session.getSleepTime();
                    LocalDateTime wakeTime = session.getWakeTime();

                    return Stream.iterate(
                                    sleepTime.toLocalDate(),
                                    date -> !date.isAfter(wakeTime.toLocalDate()),
                                    date -> date.plusDays(1)
                            )
                            .filter(date -> intersectsNight(sleepTime, wakeTime, date));
                })
                .collect(Collectors.toSet());

        long sleeplessNights = allNights.stream()
                .filter(night -> !nightsWithSleep.contains(night))
                .count();

        return new SleepAnalyticsResult("Количество бессонных ночей", sleeplessNights);
    }

    private boolean intersectsNight(LocalDateTime sleepTime, LocalDateTime wakeTime, LocalDate date) {
        LocalDateTime nightStart = date.atTime(0, 0);
        LocalDateTime nightEnd = date.atTime(6, 0);

        return !(wakeTime.isBefore(nightStart) || sleepTime.isAfter(nightEnd));
    }
}