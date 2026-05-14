# Project Walkthrough: Forex Strategy Evaluator

This document provides a high-level walkthrough of the application's features and how the different components tie together to create a seamless backtesting experience.

## 1. The Core Application Loop
When you hit "Run Backtest", the application initiates a multi-threaded sequence of events to ensure the UI remains responsive while heavy lifting occurs in the background.

1. **Data Loading:** The `DataLoader` connects to the Alpha Vantage REST API, downloads the historical Forex data as JSON, and parses it into chronological `Bar` objects for the TA4J engine.
2. **Indicator Calculation:** The `IndicatorEngine` processes the raw price data, calculating complex technical indicators like MACD, RSI, ATR, Stochastic, and Bollinger Bands natively.
3. **Strategy Building:** The application either instantiates a hard-coded Java strategy class or invokes the Groovy Scripting Engine to dynamically compile your custom strategy on the fly.
4. **Simulation:** The `TradeSimulator` runs a strict candle-by-candle evaluation, checking entry and exit rules while accurately applying real-world slippage (e.g., 0.01%) to every trade execution.

## 2. Visualization & Playback
Once the mathematical simulation is complete, the data moves to the UI layer.

- **Instant Mode:** The `ChartGenerator` uses JFreeChart to instantly render the entire historical price dataset as a beautiful Candlestick chart. It overlays the Bollinger Bands and precisely plots Green/Red arrows on the exact timestamps where trades occurred.
- **Animated Playback Mode:** If animation is enabled, the application spawns an `Animation Loop`. It slices the historical data candle-by-candle, pushing rapid updates to the JFreeChart dataset. This creates a "smooth playback" effect, allowing you to watch the trading algorithm execute decisions in real-time.

## 3. Analytics & Logging
- **Metrics Dashboard:** Calculates the Win/Loss Rate, Cumulative Profit/Loss Percentage, and the Maximum Drawdown experienced by the account.
- **Trade Table:** A JavaFX `TableView` dynamically populates with a row for every completed trade, detailing the entry and exit parameters.

## 4. Database Persistence
Without requiring any user intervention, the `DatabaseManager` connects to a local embedded SQLite database (`results.db`). It executes SQL transactions to save the high-level backtest metadata (strategy used, pair, total P/L) alongside a massive relational table containing every single individual trade log for future analysis.
