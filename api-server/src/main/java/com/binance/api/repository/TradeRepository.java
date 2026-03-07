package com.binance.api.repository;

import com.binance.api.model.Trade;
import com.binance.api.model.TradeId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, TradeId> {

    @Query("SELECT t FROM Trade t WHERE t.id.symbol = :symbol ORDER BY t.id.tradeTime DESC")
    List<Trade> findRecentBySymbol(@Param("symbol") String symbol, Pageable pageable);
}
