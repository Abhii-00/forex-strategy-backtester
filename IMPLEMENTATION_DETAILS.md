# Implementation Details & Architecture

This document outlines the technical architecture and specific library implementations of the Forex Strategy Evaluator.

## 1. Technology Stack
- **Language:** Java 17
- **Build System:** Maven
- **GUI Framework:** JavaFX (with SwingNode bridging)
- **Quantitative Engine:** TA4J (Version 0.15)
- **Charting Library:** JFreeChart
- **Database:** SQLite (via `sqlite-jdbc`)
- **JSON Parsing:** Google Gson
- **Scripting:** Apache Groovy (JSR-223 ScriptEngine)

## 2. Architectural Design (SOLID Principles)
The codebase is heavily modularized to adhere to SOLID principles, ensuring components are decoupled and maintainable.

### `com.quant.data`
- **`DataLoader`**: Handles external network requests via `java.net.HttpURLConnection`. It fetches JSON from Alpha Vantage, handles rate-limit errors gracefully, and utilizes `Gson` to parse the structure. It maps the data into a `TreeMap` to guarantee strict chronological ordering before injecting it into a TA4J `BarSeries`.

### `com.quant.engine`
- **`IndicatorEngine`**: Acts as a centralized factory and cache for technical indicators. It ensures that expensive mathematical operations (like 200-period EMAs or Standard Deviations) are calculated once and shared cleanly across the application.

### `com.quant.simulation`
- **`TradeSimulator`**: Implements a strict, realistic backtesting loop. Unlike naive evaluators, it forces candle-by-candle progression to prevent look-ahead bias and explicitly applies `slippagePercentage` mathematically to every entry and exit price.

### `com.quant.strategy`
- **`TradingStrategy` (Interface)**: Defines the contract for all strategies.
- Concrete Implementations: `MacdStrategy`, `RsiStrategy`, `SmaCrossoverStrategy`, `StochasticStrategy`.
- **`CustomScriptStrategy`**: A bridging class that defers strategy construction to the Groovy execution engine.

### `com.quant.ui`
- **`ChartGenerator`**: A factory that abstracts away the extreme complexities of JFreeChart. It converts TA4J datasets into `OHLCDataset` and `TimeSeriesCollection`. Crucially, it implements `updateChartData()`, a highly optimized method that mutates existing chart datasets in-place, enabling the 50-FPS Animated Playback without memory leaks or UI freezing.
- **`MainApplication`**: The JavaFX entry point. It utilizes a `BorderPane` layout containing a `TabPane` for the Chart and Script Editor. It strictly enforces thread safety by executing API calls and simulations on background Threads, while restricting UI mutations and JFreeChart repaints to `Platform.runLater` and `SwingUtilities.invokeLater`.

### `com.quant.db`
- **`DatabaseManager`**: Manages the embedded SQLite connection. It utilizes `PreparedStatement` batching to ensure that inserting hundreds of individual trade logs happens in a single, high-performance transaction.

## 3. The Groovy ScriptEngine Integration
The application uses the `javax.script` API to embed Groovy.
1. The `ScriptEngineManager` spins up a Groovy context at runtime.
2. The `IndicatorEngine` is injected into the context via `scriptEngine.put("indicatorEngine", engine)`.
3. The user's text from the `TextArea` is evaluated.
4. Java reflection casts the returned Groovy object back into a strictly-typed TA4J `Strategy` object, merging the dynamic scripting world perfectly into the compiled Java backend.
