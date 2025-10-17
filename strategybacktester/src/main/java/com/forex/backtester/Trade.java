package com.forex.backtester;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Trade {
    private final SimpleStringProperty type;
    private final SimpleStringProperty openTime;
    private final SimpleDoubleProperty openPrice;
    private final SimpleStringProperty closeTime;
    private final SimpleDoubleProperty closePrice;
    private final SimpleDoubleProperty profit;

    public Trade(String type, String openTime, double openPrice, String closeTime, double closePrice, double profit) {
        this.type = new SimpleStringProperty(type);
        this.openTime = new SimpleStringProperty(openTime);
        this.openPrice = new SimpleDoubleProperty(openPrice);
        this.closeTime = new SimpleStringProperty(closeTime);
        this.closePrice = new SimpleDoubleProperty(closePrice);
        this.profit = new SimpleDoubleProperty(profit);
    }

    public String getType() { return type.get(); }
    public String getOpenTime() { return openTime.get(); }
    public double getOpenPrice() { return openPrice.get(); }
    public String getCloseTime() { return closeTime.get(); }
    public double getClosePrice() { return closePrice.get(); }
    public double getProfit() { return profit.get(); }
}