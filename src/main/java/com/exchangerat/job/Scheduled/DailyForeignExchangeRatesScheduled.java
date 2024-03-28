package com.exchangerat.job.Scheduled;



import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.exchangerat.job.model.ExchangeRate;
import com.exchangerat.job.service.ExchangeRateMongoService;
import com.exchangerat.job.util.UrlUtil;

@Service
public class DailyForeignExchangeRatesScheduled {
    
    @Autowired
    UrlUtil urlUtil;
    @Autowired
    ExchangeRateMongoService exchangeRateMongoService;

    private Gson gson = new Gson();


    // @Scheduled(fixedRate = 5000)
    @Scheduled(cron = "0 0 18 * * ?")
    public void doGetDailyForeignExchangeRates () throws IOException, ParseException{


      String  response = urlUtil.doGet("https://openapi.taifex.com.tw/v1/DailyForeignExchangeRates");

        Type exchangeRateListType = new TypeToken<List<ExchangeRate>>(){}.getType();
        List<ExchangeRate> exchangeRates = new Gson().fromJson(response.replaceAll("/", "_"), exchangeRateListType);

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 遍歷 List，獲取每個物件的 "Date" 和 "USD/NTD" 值
       
        // 連接到 MongoDB 伺服器
        try (
            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
          // 選擇要使用的資料庫
          MongoDatabase database = mongoClient.getDatabase("mydatabase");

          // 選擇要查詢的集合
          MongoCollection<Document> collection = database.getCollection("exchange_rates");

        //   collection.deleteMany(new Document());

       
        for (ExchangeRate rate : exchangeRates) {

             // 將日期字串解析為 Date 物件
            //  Date date = inputFormat.parse(rate.getDate());
            
             // 將 Date 物件轉換為指定格式的日期字串
             Date dateWithTime = new Date(); // 現在的日期和時間
             String formattedDate = outputFormat.format(dateWithTime);//現在時間才對

            rate.setDate(dateWithTime);



            System.out.println("USD/NTD: " + rate.getUSD_NTD());
            
            
    Document existingDocument = collection.find(Filters.eq("date", formattedDate)).first();
    if (existingDocument == null) {
        // 如果不存在相同日期的文件，則將資料存入集合中
        exchangeRateMongoService.saveExchangeRateMongo(rate);
    } else {
        // 如果存在相同日期的文件，則不進行存入操作
        System.out.println("Document with date " + formattedDate + " already exists. Skipping...");
    }
            
            
        }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  

}
