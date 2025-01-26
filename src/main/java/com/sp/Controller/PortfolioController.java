package com.sp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sp.Service.PortfolioService;
import com.sp.entities.Portfolio;
import com.sp.entities.PortfolioWithStocksResponse;
import com.sp.entities.Stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping
    public List<Portfolio> getAllPortfolios() {
        return portfolioService.getAllPortfolios();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Portfolio> getPortfolioById(@PathVariable Long id) {
        Portfolio portfolio = portfolioService.getPortfolioById(id);
        return ResponseEntity.ok(portfolio);
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<Portfolio>> getPortfolioByUsername(@PathVariable String username) {
        List<Portfolio> portfolios = portfolioService.getPortfolioByUsername(username);
        if (portfolios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>()); // Empty list but a success response
        }
        return ResponseEntity.ok(portfolios); // If portfolios exist, return them as usual
    }

    @PostMapping
    public ResponseEntity<Portfolio> createPortfolio(@RequestBody Portfolio portfolio, @RequestParam("username") String username) {
        Portfolio createdPortfolio = portfolioService.createPortfolio(portfolio, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPortfolio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Portfolio> updatePortfolio(@PathVariable Long id, @RequestBody Portfolio portfolio) {
        Portfolio updatedPortfolio = portfolioService.updatePortfolio(id, portfolio);
        return ResponseEntity.ok(updatedPortfolio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePortfolio(@PathVariable Long id) {
        portfolioService.deletePortfolio(id);
        return ResponseEntity.ok("Portfolio deleted successfully.");
    }

    // Correcting the endpoint for fetching portfolio with stocks
    @GetMapping("/{portfolioId}/with-stocks")
    public PortfolioWithStocksResponse getPortfolioWithStocks(@PathVariable Long portfolioId) {
        return portfolioService.getPortfolioWithStocksById(portfolioId);
    }
}
