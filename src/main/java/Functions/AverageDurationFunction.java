package Functions;

import com.sun.source.tree.BreakTree;
import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepAnalyticsResult;
import ru.yandex.practicum.sleeptracker.SleepSession;

import java.util.List;

public class AverageDurationFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalyticsResult analyze(List<SleepSession> sessions) {
        double averageDuration = sessions.stream()
                .mapToLong(SleepSession::getDurationMinutes)
                .average()
                .orElse(0.0);

        return new SleepAnalyticsResult(
                "Средняя продолжительность сна (в минута)",
                String.format("%.1s", averageDuration)
        );
    }
}
