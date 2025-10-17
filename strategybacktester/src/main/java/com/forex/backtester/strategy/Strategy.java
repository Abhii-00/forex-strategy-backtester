package com.backtester.strategy;

import com.backtester.model.Candle;
import java.util.List;

public interface Strategy {
    enum Signal { BUY, SELL, HOLD }
    Signal checkForSignal(int currentIndex, List<Candle> history);
}