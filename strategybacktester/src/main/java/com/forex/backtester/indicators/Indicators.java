package com.forex.backtester.indicators;

import com.forex.backtester.model.Candle;
import java.util.List;

public class Indicators {
    public static double sma(int index, int period, List<Candle> history) {
        if (index < period - 1) return 0;
        double sum = 0.0;
        for (int i = 0; i < period; i++) {
            sum += history.get(index - i).getClose();
        }
        return sum / period;
    }
}