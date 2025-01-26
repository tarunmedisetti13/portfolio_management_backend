package com.sp.Repository;
import java.util.List;

//import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sp.entities.Stock;
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByPortfolioId(Long portfolioId);
}

