package com.backtester.model;

import java.util.List;

public class BacktestResult {
    
    private final List<Trade> trades;
    private double totalProfitLoss;
    private int totalTrades;
    private int winningTrades;
    private int losingTrades;
    private double winRate; // As a percentage

    public BacktestResult(List<Trade> trades) {
        this.trades = trades;
    }

    // Call this method after the backtest is finished to calculate the summary
    public void calculateFinalStatistics() {
        this.totalTrades = trades.size();
        if (totalTrades == 0) return;

        for (Trade trade : trades) {
            if (trade.getProfitLoss() > 0) {
                winningTrades++;
            } else {
                losingTrades++;
            }
            totalProfitLoss += trade.getProfitLoss();
        }

        this.winRate = ((double) winningTrades / totalTrades) * 100.0;
    }

    // Getters for all fields
    public List<Trade> getTrades() { return trades; }
    public double getTotalProfitLoss() { return totalProfitLoss; }
    public int getTotalTrades() { return totalTrades; }
    public int getWinningTrades() { return winningTrades; }
    public int getLosingTrades() { return losingTrades; }
    public double getWinRate() { return winRate; }
}