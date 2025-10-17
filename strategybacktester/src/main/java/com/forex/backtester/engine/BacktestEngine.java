package com.forex.backtester.engine;

import com.forex.backtester.model.BacktestResult;
import com.forex.backtester.model.Candle;
import com.forex.backtester.model.Trade;
import com.forex.backtester.strategy.Strategy;
import java.util.ArrayList;
import java.util.List;

public class BacktestEngine {
    public void runSimulation(List<Candle> candles, Strategy strategy, BacktestListener listener, long speedMs) {
        // SAFEGUARD: If no listener is provided, we can't send updates.
        if (listener == null) {
            System.err.println("CRITICAL WARNING: BacktestListener is null. The UI will not receive any live updates.");
        }

        List<Trade> closedTrades = new ArrayList<>();
        Trade currentTrade = null;
        for (int i = 1; i < candles.size(); i++) {
            Candle currentCandle = candles.get(i);

            // NULL CHECK: Only send updates if the listener actually exists.
            if (listener != null) {
                listener.onCandleProcessed(currentCandle);
            }

            Strategy.Signal signal = strategy.checkForSignal(i, candles);

            if (currentTrade == null) {
                if (signal == Strategy.Signal.BUY) {
                    currentTrade = new Trade(Trade.TradeType.BUY, currentCandle.getDateTime(), currentCandle.getOpen());
                    if (listener != null) listener.onTradeOpened(currentTrade);
                }
            } else {
                if (signal == Strategy.Signal.SELL) {
                    currentTrade.close(currentCandle.getDateTime(), currentCandle.getOpen());
                    closedTrades.add(currentTrade);
                    if (listener != null) listener.onTradeClosed(currentTrade);
                    currentTrade = null;
                }
            }
            try {
                Thread.sleep(speedMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        BacktestResult result = new BacktestResult(closedTrades);
        result.calculateFinalStatistics();

        // NULL CHECK: Send the final report only if the listener exists.
        if (listener != null) {
            listener.onBacktestFinished(result);
        }
    }
}