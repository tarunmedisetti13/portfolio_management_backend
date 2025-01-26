package com.sp.entities;
import com.sp.entities.Portfolio;
import com.sp.entities.Stock;

import java.util.List;

public class PortfolioWithStocksResponse {

    private Portfolio portfolio;
    private List<Stock> stocks;

    public PortfolioWithStocksResponse(Portfolio portfolio, List<Stock> stocks) {
        this.portfolio = portfolio;
        this.stocks = stocks;
    }

    // Getters and Setters

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }
}
