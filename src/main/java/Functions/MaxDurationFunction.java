package Functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepAnalyticsResult;
import ru.yandex.practicum.sleeptracker.SleepSession;

import java.util.List;

public class MaxDurationFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalyticsResult analyze(List<SleepSession> sessions) {
        long maxDuration = sessions.stream()
                .mapToLong(SleepSession::getDurationMinutes)
                .max()
                .orElse(0);

        return new SleepAnalyticsResult(
                "Максимальная продолжительность сессии (в минутах)",
                maxDuration
        );
    }
}
