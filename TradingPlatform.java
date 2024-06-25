import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

class Stock {
    private final String symbol;
    private double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

class StockMarket {
    private final HashMap<String, Stock> stocks;

    public StockMarket() {
        stocks = new HashMap<>();
        initializeStocks();
    }

    private void initializeStocks() {
        stocks.put("AAPL", new Stock("AAPL", 150.0));
        stocks.put("GOOGL", new Stock("GOOGL", 2800.0));
        stocks.put("AMZN", new Stock("AMZN", 3400.0));
        stocks.put("MSFT", new Stock("MSFT", 290.0));
    }

    public void updatePrices() {
        Random random = new Random();
        for (Stock stock : stocks.values()) {
            double change = random.nextDouble() * 10 - 5;
            stock.setPrice(stock.getPrice() + change);
        }
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }

    public void displayMarket() {
        System.out.println("Current Market Data:");
        for (Stock stock : stocks.values()) {
            System.out.printf("%s: $%.2f%n", stock.getSymbol(), stock.getPrice());
        }
    }
}

class Portfolio {
    private final HashMap<String, Integer> holdings;
    private double cash;

    public Portfolio(double initialCash) {
        holdings = new HashMap<>();
        cash = initialCash;
    }

    public void buyStock(String symbol, int quantity, double price) {
        if (cash < price * quantity) {
            System.out.println("Not enough cash to buy " + quantity + " shares of " + symbol);
            return;
        }
        holdings.put(symbol, holdings.getOrDefault(symbol, 0) + quantity);
        cash -= price * quantity;
        System.out.println("Bought " + quantity + " shares of " + symbol);
    }

    public void sellStock(String symbol, int quantity, double price) {
        if (holdings.getOrDefault(symbol, 0) < quantity) {
            System.out.println("Not enough shares to sell " + quantity + " shares of " + symbol);
            return;
        }
        holdings.put(symbol, holdings.get(symbol) - quantity);
        cash += price * quantity;
        System.out.println("Sold " + quantity + " shares of " + symbol);
    }

    public void displayPortfolio(StockMarket market) {
        System.out.println("Current Portfolio:");
        double totalValue = cash;
        for (String symbol : holdings.keySet()) {
            int quantity = holdings.get(symbol);
            double price = market.getStock(symbol).getPrice();
            double value = quantity * price;
            totalValue += value;
            System.out.printf("%s: %d shares @ $%.2f each = $%.2f%n", symbol, quantity, price, value);
        }
        System.out.printf("Cash: $%.2f%n", cash);
        System.out.printf("Total Portfolio Value: $%.2f%n", totalValue);
    }
}

public class TradingPlatform {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StockMarket market = new StockMarket();
    private static final Portfolio portfolio = new Portfolio(10000.0);

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int choice = getUserChoice();
            handleMenuChoice(choice);
        }
    }

    private static void displayMenu() {
        System.out.println("\n1. Display Market Data");
        System.out.println("2. Buy Stock");
        System.out.println("3. Sell Stock");
        System.out.println("4. Display Portfolio");
        System.out.println("5. Update Market Prices");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    private static int getUserChoice() {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next(); // Clear invalid input
            }
        }
    }

    private static void handleMenuChoice(int choice) {
        switch (choice) {
            case 1 -> market.displayMarket();
            case 2 -> buyStock();
            case 3 -> sellStock();
            case 4 -> portfolio.displayPortfolio(market);
            case 5 -> updateMarketPrices();
            case 6 -> exitProgram();
            default -> System.out.println("Invalid option. Please try again.");
        }
    }

    private static void buyStock() {
        System.out.print("Enter stock symbol to buy: ");
        String buySymbol = scanner.next();
        int buyQuantity = getQuantity();
        Stock buyStock = market.getStock(buySymbol);
        if (buyStock != null) {
            portfolio.buyStock(buySymbol, buyQuantity, buyStock.getPrice());
        } else {
            System.out.println("Stock symbol not found.");
        }
    }

    private static void sellStock() {
        System.out.print("Enter stock symbol to sell: ");
        String sellSymbol = scanner.next();
        int sellQuantity = getQuantity();
        Stock sellStock = market.getStock(sellSymbol);
        if (sellStock != null) {
            portfolio.sellStock(sellSymbol, sellQuantity, sellStock.getPrice());
        } else {
            System.out.println("Stock symbol not found.");
        }
    }

    private static int getQuantity() {
        while (true) {
            System.out.print("Enter quantity: ");
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter an integer: ");
                scanner.next(); // Clear invalid input
            }
        }
    }

    private static void updateMarketPrices() {
        market.updatePrices();
        System.out.println("Market prices updated.");
    }

    private static void exitProgram() {
        scanner.close();
        System.out.println("Exiting...");
        System.exit(0);
    }
}
