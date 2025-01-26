package com.sp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sp.Repository.PortfolioRepository;
import com.sp.Repository.StockRepository;
import com.sp.Repository.UserRepository;
import com.sp.entities.Portfolio;
import com.sp.entities.PortfolioWithStocksResponse;
import com.sp.entities.Stock;
import com.sp.entities.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockPriceService stockPriceService;

    public List<Portfolio> getAllPortfolios() {
        return portfolioRepository.findAll();
    }

    public List<Portfolio> getPortfolioByUsername(String username) {
        List<Portfolio> portfolios = portfolioRepository.findByUserUsername(username);
        return portfolios; // Return either empty list or portfolios as needed
    }

 public String getUsernameByPortfolioId(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with ID: " + portfolioId));
        return portfolio.getUser().getUsername(); // Assuming the `User` entity has a `getUsername` method
    }
    public Portfolio createPortfolio(Portfolio portfolio, String username) {
        // Fetch the User entity using the username
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User with username " + username + " not found.");
        }

        // Set the user to the Portfolio
        portfolio.setUser(user);

        // Save and return the Portfolio
        return portfolioRepository.save(portfolio);
    }

    public Portfolio updatePortfolio(Long id, Portfolio updatedPortfolio) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found with id: " + id));

        if (updatedPortfolio.getName() != null) portfolio.setName(updatedPortfolio.getName());
        if (updatedPortfolio.getDescription() != null) portfolio.setDescription(updatedPortfolio.getDescription());

        return portfolioRepository.save(portfolio);
    }

    public void deletePortfolio(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found with id: " + id));
        portfolioRepository.delete(portfolio);
    }

    public PortfolioWithStocksResponse getPortfolioWithStocksById(Long portfolioId) {
        // Fetch the portfolio
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found with id: " + portfolioId));

        // Fetch the stocks associated with this portfolio
        List<Stock> stocks = stockRepository.findByPortfolioId(portfolioId);

        // Return the combined response
        return new PortfolioWithStocksResponse(portfolio, stocks);
    }
 public BigDecimal getPortfolioTotalValueById(Long portfolioId) {
        Portfolio portfolio = getPortfolioById(portfolioId); // Fetch the portfolio
        List<Stock> stocks = portfolio.getStocks(); // Assuming you have a method to get stocks from the portfolio

        BigDecimal totalValue = BigDecimal.ZERO;

        // Calculate the total value of the portfolio by fetching current prices
        for (Stock stock : stocks) {
            String symbol = stock.getTickerSymbol();
            String priceString = stockPriceService.getCurrentPrice(symbol); // Fetch the current price as a string

            try {
                BigDecimal currentPrice = new BigDecimal(priceString); // Convert the price string to BigDecimal
                totalValue = totalValue.add(currentPrice.multiply(BigDecimal.valueOf(stock.getUnits()))); // Add stock value to total
            } catch (NumberFormatException e) {
                // Handle invalid price format (e.g., if priceString is not a valid number)
                System.out.println("Error converting price for stock: " + symbol + " with price: " + priceString);
            }
        }

        return totalValue; // Return the total value of the portfolio
    }


    public ResponseEntity<BigDecimal> getTotalPurchasePrice(Long portfolioId) {
        BigDecimal totalPurchasePrice = BigDecimal.ZERO;

        // Retrieve the portfolio by ID
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with id: " + portfolioId));

        // Iterate through stocks in the portfolio
        for (Stock stock : portfolio.getStocks()) {
            // Calculate purchase price for each stock and add to total
            BigDecimal stockPurchasePrice = stock.getPurchasePrice().multiply(BigDecimal.valueOf(stock.getUnits()));
            totalPurchasePrice = totalPurchasePrice.add(stockPurchasePrice);
        }

        return ResponseEntity.ok(totalPurchasePrice);
    }

    public Portfolio getPortfolioById(Long portfolioId) {
        return portfolioRepository.findById(portfolioId).orElse(null);
    }
}
