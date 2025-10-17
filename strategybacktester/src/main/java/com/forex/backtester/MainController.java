package com.forex.backtester;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {

    // --- Asset Controls ---
    @FXML private ComboBox<String> assetComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextArea strategyTextArea;
    @FXML private Button runButton;

    // --- Main Display ---
    @FXML private LineChart<Number, Number> priceChart;

    // --- Results Table and Columns ---
    @FXML private TableView<Trade> resultsTable;
    @FXML private TableColumn<Trade, String> colType;
    @FXML private TableColumn<Trade, String> colOpenTime;
    @FXML private TableColumn<Trade, Double> colOpenPrice;
    @FXML private TableColumn<Trade, String> colCloseTime;
    @FXML private TableColumn<Trade, Double> colClosePrice;
    @FXML private TableColumn<Trade, Double> colProfit;


    @FXML
    private void runBacktest() {
        System.out.println("Run Backtest button was clicked!");
        String selectedAsset = assetComboBox.getValue();
        System.out.println("Selected Asset: " + selectedAsset);
        System.out.println("Strategy Code: " + strategyTextArea.getText());
        
        // Example of how to add a trade to the table
        Trade exampleTrade = new Trade("BUY", "2025-10-17 10:00", 1.0500, "2025-10-17 12:00", 1.0550, 50.0);
        resultsTable.getItems().add(exampleTrade);
    }

    @FXML
    public void initialize() {
        populateAssetComboBox();
        setupResultsTable();
    }

    private void populateAssetComboBox() {
        assetComboBox.setItems(FXCollections.observableArrayList(
            "EUR/USD", "USD/JPY", "GBP/USD", "USD/CHF", "AUD/USD", "USD/CAD", "NZD/USD",
            "XAU/USD (Gold)", "XAG/USD (Silver)", "WTI_USD (US Oil)", "BRENT_USD (Brent Oil)"
        ));
    }

    private void setupResultsTable() {
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colOpenTime.setCellValueFactory(new PropertyValueFactory<>("openTime"));
        colOpenPrice.setCellValueFactory(new PropertyValueFactory<>("openPrice"));
        colCloseTime.setCellValueFactory(new PropertyValueFactory<>("closeTime"));
        colClosePrice.setCellValueFactory(new PropertyValueFactory<>("closePrice"));
        colProfit.setCellValueFactory(new PropertyValueFactory<>("profit"));
    }
}