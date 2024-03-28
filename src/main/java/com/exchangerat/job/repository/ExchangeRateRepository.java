package com.exchangerat.job.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import com.exchangerat.job.model.ExchangeRate;

@SuppressWarnings("rawtypes")
public interface ExchangeRateRepository extends MongoRepository<ExchangeRate, String>{


    
}
