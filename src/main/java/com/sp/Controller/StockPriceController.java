package com.sp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sp.Service.StockPriceService;


@RestController
@RequestMapping("/api/stocks")
public class StockPriceController {

    @Autowired
    private StockPriceService stockPriceService;

    // Endpoint to get the current price of a stock by symbol
    @GetMapping("/getprice")
    public String getCurrentPrice(@RequestParam String symbol) {
        return stockPriceService.getCurrentPrice(symbol);
    }
}
