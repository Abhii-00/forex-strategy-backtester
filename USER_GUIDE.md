# Forex Strategy Evaluator - User Guide

Welcome to the Forex Strategy Evaluator! This application is a powerful, desktop-based quantitative trading platform that allows you to backtest trading strategies using real historical Forex data.

## Getting Started

### 1. Prerequisites
- **Java 17** or higher installed.
- **Maven** installed and added to your system PATH.
- An **Alpha Vantage API Key**. You can get a free one at [alphavantage.co](https://www.alphavantage.co/support/#api-key). 

### 2. Launching the Application
Open your terminal or command prompt, navigate to the project directory, and run:
```bash
mvn clean compile javafx:run
```

## How to Use the UI

When the application opens, you will see a Control Panel at the top.
1. **API Key:** Paste your Alpha Vantage API key here. (If you are testing `EUR/USD`, you can temporarily type the word `demo`).
2. **Pair:** Select the currency pair you want to backtest.
3. **Timeframe:** Defaults to DAILY candlesticks.
4. **Strategy:** Choose from pre-built strategies (MACD, RSI, SMA Crossover, Stochastic) or select **Custom Script**.
5. **Animate Playback:** Check this box if you want to watch the trades execute candle-by-candle in real-time. Leave it unchecked for instantaneous results.
6. **Run Backtest:** Click this to start!

## Custom Scripting Engine (Groovy)

The true power of this platform lies in the **Script Editor** tab. By selecting **Custom Script** from the strategy dropdown, you can write your own trading algorithms on the fly using Groovy. 

Your script has access to `indicatorEngine`, which contains the raw price data and calculation methods. The script must return a TA4J `Strategy` object.

### Example Script 1: RSI + EMA Trend Filter
Buys when the asset is oversold (RSI < 30) but trending upwards (Price > 200 EMA). Sells when overbought (RSI > 70).

```groovy
import org.ta4j.core.BaseStrategy
import org.ta4j.core.indicators.RSIIndicator
import org.ta4j.core.indicators.EMAIndicator
import org.ta4j.core.rules.CrossedDownIndicatorRule
import org.ta4j.core.rules.CrossedUpIndicatorRule
import org.ta4j.core.rules.OverIndicatorRule
import org.ta4j.core.rules.UnderIndicatorRule

def closePrice = indicatorEngine.getClosePrice()
def series = indicatorEngine.getSeries()

def rsi = new RSIIndicator(closePrice, 14)
def ema200 = new EMAIndicator(closePrice, 200)

def entryRule = new UnderIndicatorRule(rsi, series.numOf(30))
        .and(new OverIndicatorRule(closePrice, ema200))

def exitRule = new OverIndicatorRule(rsi, series.numOf(70))
        .or(new UnderIndicatorRule(closePrice, ema200))

return new BaseStrategy("RSI + EMA Filter", entryRule, exitRule)
```

### Example Script 2: Bollinger Band Mean Reversion
Buys when price crosses below the lower band, sells when it crosses above the upper band.

```groovy
import org.ta4j.core.BaseStrategy
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator
import org.ta4j.core.indicators.SMAIndicator
import org.ta4j.core.rules.CrossedDownIndicatorRule
import org.ta4j.core.rules.CrossedUpIndicatorRule

def closePrice = indicatorEngine.getClosePrice()
def sma20 = new SMAIndicator(closePrice, 20)
def stdDev = new StandardDeviationIndicator(closePrice, 20)

def bbMiddle = new BollingerBandsMiddleIndicator(sma20)
def bbLower = new BollingerBandsLowerIndicator(bbMiddle, stdDev, indicatorEngine.getSeries().numOf(2.0))
def bbUpper = new BollingerBandsUpperIndicator(bbMiddle, stdDev, indicatorEngine.getSeries().numOf(2.0))

def entryRule = new CrossedDownIndicatorRule(closePrice, bbLower)
def exitRule = new CrossedUpIndicatorRule(closePrice, bbUpper)

return new BaseStrategy("Bollinger Mean Reversion", entryRule, exitRule)
```

## Viewing Your Results
- **Interactive Chart:** Zoom in using your mouse wheel. Green arrows signify BUY orders, Red arrows signify SELL orders.
- **Trade Log:** The bottom table provides a detailed log of every trade executed, including exact entry/exit prices and the net profit percentage.
- **Database:** All results are automatically saved to `results.db` in your project folder!
