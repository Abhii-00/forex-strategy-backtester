package com.backtester.engine;

import com.backtester.model.BacktestResult;
import com.backtester.model.Candle;
import com.backtester.model.Trade;

public interface BacktestListener {
    void onCandleProcessed(Candle candle);
    void onTradeOpened(Trade trade);
    void onTradeClosed(Trade trade);
    void onBacktestFinished(BacktestResult result);
}