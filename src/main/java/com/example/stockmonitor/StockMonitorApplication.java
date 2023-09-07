package com.example.stockmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class StockMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockMonitorApplication.class, args);
	}
}

// Observer Interface
interface Observer {
	void update(Stock stock);
}

// Concrete Observer: StockView
class StockView implements Observer {
	public void update(Stock stock) {
		System.out.println("StockView: " + stock.getName() + " price updated to " + stock.getPrice());
	}
}

// Stock class
class Stock {
	private String name;
	private double price;
	private List<Observer> observers = new ArrayList<>();

	public Stock(String name, double price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
		notifyObservers();
	}

	public void addObserver(Observer observer) {
		observers.add(observer);
	}

	public void removeObserver(Observer observer) {
		observers.remove(observer);
	}

	private void notifyObservers() {
		for (Observer observer : observers) {
			observer.update(this);
		}
	}
}

// StockMarket class
class StockMarket {
	private List<Stock> stocks = new ArrayList<>();

	public void addStock(Stock stock) {
		stocks.add(stock);
	}

	// Simulate price change
	public void simulatePriceChange() {
		Random random = new Random();
		for (Stock stock : stocks) {
			stock.setPrice(stock.getPrice() + random.nextDouble() - 0.5); // Random price change
		}
	}
}

@RestController
class StockController {

	@GetMapping("/simulatePriceChange")
	public List<Stock> simulatePriceChange() {
		Stock apple = new Stock("Apple", 150);
		Stock google = new Stock("Google", 2800);

		StockView stockView = new StockView();

		apple.addObserver(stockView);
		google.addObserver(stockView);

		StockMarket market = new StockMarket();
		market.addStock(apple);
		market.addStock(google);

		market.simulatePriceChange();

		return Arrays.asList(apple, google);
	}
}
