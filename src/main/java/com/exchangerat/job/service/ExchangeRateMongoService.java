package com.exchangerat.job.service;

import org.springframework.stereotype.Service;

import com.exchangerat.job.model.ExchangeRate;

@Service
public interface ExchangeRateMongoService {
    
    public void saveExchangeRateMongo(ExchangeRate exchangeRate);

}
