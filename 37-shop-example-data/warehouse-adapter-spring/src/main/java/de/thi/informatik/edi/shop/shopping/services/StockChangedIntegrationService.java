package de.thi.informatik.edi.shop.shopping.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class StockChangedIntegrationService {
    private StockChangedSubscriberService stocks;
    private StockChangedMessageProducerService stockChanged;

    public StockChangedIntegrationService(@Autowired StockChangedSubscriberService stocks,
            @Autowired StockChangedMessageProducerService stockChanged) {
        this.stocks = stocks;
        this.stockChanged = stockChanged;
    }

    @PostConstruct
    private void init() {

        this.stockChanged.handle(this.stocks.getStockChanges());
    }
}
