package com.sp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.Repository.PortfolioRepository;
import com.sp.Repository.StockRepository;
import com.sp.entities.Portfolio;
import com.sp.entities.Stock;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

  // To fetch User by username

    // Alpha vantage URL and key
    private static final String API_URL = "https://www.alphavantage.co/query";
    private static final String API_KEY = "6US23WRZNIBUZFS6";

    // Create a stock and associate it with a portfolio
    public Stock createStock(Stock stock, Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found for this id: " + portfolioId));

        // Set the portfolio on the stock
        stock.setPortfolio(portfolio);

        // Save and return the created stock
        return stockRepository.save(stock);
    }

    // Get stock by ID and associate it with the portfolio
    public Stock getStockById(Long portfolioId, Long stockId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found for this id: " + portfolioId));

        return stockRepository.findById(stockId)
                .filter(stock -> stock.getPortfolio().getId().equals(portfolioId))
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found for this id and portfolio"));
    }

    // Update a stock
    public Stock updateStock(Long portfolioId, Long stockId, Stock stock) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found for this id: " + portfolioId));

        Stock existingStock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found for this id"));

        // Update stock details here (e.g., name, quantity, price)
        existingStock.setUnits(stock.getUnits());
        existingStock.setPurchasePrice(stock.getPurchasePrice());
        existingStock.setCompanyName(stock.getCompanyName());
        existingStock.setPortfolio(portfolio);

        return stockRepository.save(existingStock);
    }

    // Delete a stock
    public void deleteStock(Long portfolioId, Long stockId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found for this id: " + portfolioId));

        Stock stock = stockRepository.findById(stockId)
                .filter(s -> s.getPortfolio().getId().equals(portfolioId))
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found for this id and portfolio"));

        stockRepository.delete(stock);
    }
}
