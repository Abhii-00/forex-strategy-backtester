package com.forex.backtester.engine;

import com.forex.backtester.model.BacktestResult;
import com.forex.backtester.model.Candle;
import com.forex.backtester.model.Trade;

public interface BacktestListener {
    void onCandleProcessed(Candle candle);
    void onTradeOpened(Trade trade);
    void onTradeClosed(Trade trade);
    void onBacktestFinished(BacktestResult result);
}