package com.backtester.model;

import java.time.LocalDateTime;

public class Trade {

    public enum TradeType { BUY, SELL }
    public enum TradeStatus { OPEN, CLOSED }

    private final TradeType type;
    private final LocalDateTime entryTime;
    private final double entryPrice;

    private LocalDateTime exitTime;
    private double exitPrice;
    private double profitLoss;
    private TradeStatus status;

    public Trade(TradeType type, LocalDateTime entryTime, double entryPrice) {
        this.type = type;
        this.entryTime = entryTime;
        this.entryPrice = entryPrice;
        this.status = TradeStatus.OPEN;
    }

    public void close(LocalDateTime exitTime, double exitPrice) {
        this.exitTime = exitTime;
        this.exitPrice = exitPrice;
        this.status = TradeStatus.CLOSED;
        
        // Calculate profit or loss
        if (this.type == TradeType.BUY) {
            this.profitLoss = this.exitPrice - this.entryPrice;
        } else { // SELL
            this.profitLoss = this.entryPrice - this.exitPrice;
        }
    }
    
    // Getters for all fields
    public TradeType getType() { return type; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public double getEntryPrice() { return entryPrice; }
    public LocalDateTime getExitTime() { return exitTime; }
    public double getExitPrice() { return exitPrice; }
    public double getProfitLoss() { return profitLoss; }
    public TradeStatus getStatus() { return status; }
}