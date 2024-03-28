package com.exchangerat.job.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exchangerat.job.model.ExchangeRate;
import com.exchangerat.job.repository.ExchangeRateRepository;
import com.exchangerat.job.service.ExchangeRateMongoService;

@Service("ExchangeRateMongoService")
public class ExchangeRateMongoServiceImp implements ExchangeRateMongoService{
    
    @Autowired
    private final ExchangeRateRepository exchangeRateRepository;
    
    public ExchangeRateMongoServiceImp(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }
    @Override
    public void saveExchangeRateMongo(ExchangeRate exchangeRate) {
        // 实现保存逻辑
        exchangeRateRepository.save(exchangeRate);
    }


}
