package ru.yandex.practicum.sleeptracker;

import functions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SleepTrackerApp {

    private List<SleepAnalysisFunction> analysisFunctionList = new ArrayList<>();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public SleepTrackerApp() {


        analysisFunctionList.add(new TotalSessionsFunction());
        analysisFunctionList.add(new MinDurationFunction());
        analysisFunctionList.add(new MaxDurationFunction());
        analysisFunctionList.add(new AverageDurationFunction());
        analysisFunctionList.add(new BadQualitySessionsFunction());
        analysisFunctionList.add(new SleepnessNightFunction());
        analysisFunctionList.add(new ChronotypeFunction());
    }

    public List<SleepSession> loadSleepSession(String filePath) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            return lines
                    .map(line -> line.split(";"))
                    .map(parts -> {

                        LocalDateTime sleepTime = LocalDateTime.parse(parts[0], DATE_TIME_FORMATTER);
                        LocalDateTime wakeTime = LocalDateTime.parse(parts[1], DATE_TIME_FORMATTER);
                        SleepQuality quality = SleepQuality.valueOf(parts[2]);

                        return new SleepSession(sleepTime, wakeTime, quality);
                    })
                    .collect(Collectors.toList());
        }
    }

    public void runAnalysis(List<SleepSession> sessions) {
        analysisFunctionList.stream()
                .map(func -> func.analyze(sessions))
                .forEach(System.out::println);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Пожалуйста, укажите пути к файлу с логами сна");
            return;
        }

        SleepTrackerApp trackerApp = new SleepTrackerApp();

        try {
            List<SleepSession> sessions = trackerApp.loadSleepSession(args[0]);

            System.out.println("Анализ сна: ");
            System.out.println("Загружено сессий сна: " + sessions.size());
            System.out.println();
            trackerApp.runAnalysis(sessions);

        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
    }
}