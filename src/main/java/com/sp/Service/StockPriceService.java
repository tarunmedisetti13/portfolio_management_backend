package com.sp.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StockPriceService {

    // Replace with your actual Alpha Vantage API key
    private static final String API_KEY = "6US23WRZNIBUZFS6";
    private static final String API_URL = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol={symbol}&apikey={apikey}";

    public String getCurrentPrice(String symbol) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            // Make a GET request to Alpha Vantage API
            String response = restTemplate.getForObject(API_URL, String.class, symbol, API_KEY);
            
            // Log the full response for debugging
            System.out.println("API Response: " + response);
            
            // Parse the response to extract the stock price using Jackson
            String price = parsePriceFromResponse(response);
            
            return price != null ? price : "Price not available";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching stock price";
        }
    }

    private String parsePriceFromResponse(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode globalQuoteNode = rootNode.path("Global Quote");
            String price = globalQuoteNode.path("05. price").asText();
            
            return price.isEmpty() ? null : price;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
