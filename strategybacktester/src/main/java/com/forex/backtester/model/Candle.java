package com.forex.backtester.model;

import java.time.LocalDateTime;

/**
 * Represents a single candlestick data point (OHLC).
 * This is an immutable data object.
 */
public class Candle {
    private final LocalDateTime dateTime;
    private final double open;
    private final double high;
    private final double low;
    private final double close;

    public Candle(LocalDateTime dateTime, double open, double high, double low, double close) {
        this.dateTime = dateTime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }

    // Getters for all fields
    public LocalDateTime getDateTime() { return dateTime; }
    public double getOpen() { return open; }
    public double getHigh() { return high; }
    public double getLow() { return low; }
    public double getClose() { return close; }

    @Override
    public String toString() {
        return "Candle{" + "dateTime=" + dateTime + ", O=" + open +
               ", H=" + high + ", L=" + low + ", C=" + close + '}';
    }
}