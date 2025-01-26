package com.sp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sp.Service.ResourceNotFoundException;
import com.sp.Service.StockService;
import com.sp.entities.Stock;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/portfolios/{portfolioId}/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    // Get stock by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getStockById(@PathVariable Long portfolioId, @PathVariable Long id) {
        try {
            Stock stock = stockService.getStockById(portfolioId, id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Stock fetched successfully",
                "stock", stock
            ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    // Create a new stock for a specific portfolio
    @PostMapping
    public ResponseEntity<?> createStock(@PathVariable Long portfolioId, @RequestBody Stock stock) {
        try {
            Stock createdStock = stockService.createStock(stock, portfolioId);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Stock created successfully",
                "stock", createdStock
            ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "An error occurred while creating the stock"
            ));
        }
    }

    // Update a stock
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStock(@PathVariable Long portfolioId, @PathVariable Long id, @RequestBody Stock stock) {
        try {
            Stock updatedStock = stockService.updateStock(portfolioId, id, stock);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Stock updated successfully",
                "stock", updatedStock
            ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "An error occurred while updating the stock"
            ));
        }
    }

    // Delete a stock
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStock(@PathVariable Long portfolioId, @PathVariable Long id) {
        try {
            stockService.deleteStock(portfolioId, id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Stock deleted successfully"
            ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "An error occurred while deleting the stock"
            ));
        }
    }
}
