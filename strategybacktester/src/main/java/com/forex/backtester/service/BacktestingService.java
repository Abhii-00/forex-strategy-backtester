package com.forex.backtester.service;

import com.forex.backtester.core.DynamicStrategyLoader;
import com.forex.backtester.data.CSVLoader;
import com.forex.backtester.engine.BacktestEngine;
import com.forex.backtester.engine.BacktestListener;
import com.forex.backtester.model.Candle;
import com.forex.backtester.strategy.Strategy;
import java.util.List;

public class BacktestingService {
    public void runLiveSimulationWithJavaCode(String csvFilePath, String userJavaCode, BacktestListener listener) {
        new Thread(() -> {
            try {
                Strategy userStrategy = DynamicStrategyLoader.loadStrategyFromCodeString(userJavaCode);
                List<Candle> candles = CSVLoader.loadCandles(csvFilePath);
                new BacktestEngine().runSimulation(candles, userStrategy, listener, 50);
            } catch (Exception e) {
                e.printStackTrace();
                // Consider adding listener.onError(e) to notify the UI of failures.
            }
        }).start();
    }
}