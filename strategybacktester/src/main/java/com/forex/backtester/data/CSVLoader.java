package com.forex.backtester.data;

import com.forex.backtester.model.Candle;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVLoader {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    public static List<Candle> loadCandles(String csvFilePath) {
        List<Candle> candles = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(csvFilePath))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                try {
                    LocalDateTime dt = LocalDateTime.parse(values[0] + " " + values[1], FORMATTER);
                    candles.add(new Candle(dt, Double.parseDouble(values[2]), Double.parseDouble(values[3]), Double.parseDouble(values[4]), Double.parseDouble(values[5])));
                } catch (Exception e) {
                    System.err.println("Skipping malformed line: " + line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candles;
    }
}