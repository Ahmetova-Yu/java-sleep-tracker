package functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepAnalyticsResult;
import ru.yandex.practicum.sleeptracker.SleepSession;

import java.util.List;

public class MinDurationFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalyticsResult analyze(List<SleepSession> sessions) {
        long minDuration = sessions.stream()
                .mapToLong(SleepSession::getDurationMinutes)
                .min()
                .orElse(0);

        return new SleepAnalyticsResult(
                "Минимальная продолжительность сессии (в минутах)",
                minDuration
        );
    }
}
