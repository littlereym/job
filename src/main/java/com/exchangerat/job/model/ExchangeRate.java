package com.exchangerat.job.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "exchange_rates")
public class ExchangeRate  {
    @Id
    private String id;
    private Date date;
    private String USD_NTD;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Date  getDate() {
        return date;
    }
    public void setDate(Date  date) {
        this.date = date;
    }
    public String getUSD_NTD() {
        return USD_NTD;
    }
    public void setUSD_NTD(String usdToNtd) {
        this.USD_NTD = usdToNtd;
    }


    
}
