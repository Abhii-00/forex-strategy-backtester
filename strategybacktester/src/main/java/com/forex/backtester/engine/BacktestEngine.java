package com.backtester.engine;

import com.backtester.model.BacktestResult;
import com.backtester.model.Candle;
import com.backtester.model.Trade;
import com.backtester.strategy.Strategy;
import java.util.ArrayList;
import java.util.List;

public class BacktestEngine {
    public void runSimulation(List<Candle> candles, Strategy strategy, BacktestListener listener, long speedMs) {
        List<Trade> closedTrades = new ArrayList<>();
        Trade currentTrade = null;
        for (int i = 1; i < candles.size(); i++) {
            Candle currentCandle = candles.get(i);
            listener.onCandleProcessed(currentCandle);
            Strategy.Signal signal = strategy.checkForSignal(i, candles);

            if (currentTrade == null) {
                if (signal == Strategy.Signal.BUY) {
                    currentTrade = new Trade(Trade.TradeType.BUY, currentCandle.getDateTime(), currentCandle.getOpen());
                    listener.onTradeOpened(currentTrade);
                }
            } else {
                if (signal == Strategy.Signal.SELL) {
                    currentTrade.close(currentCandle.getDateTime(), currentCandle.getOpen());
                    closedTrades.add(currentTrade);
                    listener.onTradeClosed(currentTrade);
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
        listener.onBacktestFinished(result);
    }
}