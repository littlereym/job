package com.exchangerat.job.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.exchangerat.job.model.ExchangeRate;
import com.exchangerat.job.service.ExchangeRateMongoService;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import io.swagger.annotations.Api;



@RestController
@Controller
@Api(tags = "匯率查詢")
public class mongodbController {
    
    // public void ExchangeRateMongoServiceJava() {
    //     // 連接到 MongoDB 伺服器
    //     try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
    //         // 選擇要使用的資料庫
    //         MongoDatabase database = mongoClient.getDatabase("mydatabase");
    //         // 選擇要查詢的集合
    //         collection = database.getCollection("exchange_rates");
    //     } catch (Exception e) {
    //         throw new RuntimeException("Unable to connect to MongoDB", e);
    //     }
    // }


    // private  MongoCollection<Document> collection;


    // private MongoCollection<Document> getCollection() {
    //     if (collection == null) {
    //         ExchangeRateMongoServiceJava();
    //     }
    //     return collection;
    // }
     @Autowired
    ExchangeRateMongoService exchangeRateMongoService;

   
@PostMapping(value = "/forex")
public Object getInviteCode(@RequestBody Map<String, Object> jSONObject) throws ParseException {
    System.out.println("000");
    //從 DB 取出日期區間內美元/台幣的歷史資料，並針對 API
    // 功能寫 Unit test。日期區間僅限 1 年前~當下日期-1 天，若日期區間不符規
    // 則，response 需回 error code E001，一次僅查詢一種幣別，如：美元 usd。
    List<ExchangeRate> result = new ArrayList<>();
    // 檢查欄位是否存在並且是否有值
    if (!jSONObject.containsKey("startDate") || !jSONObject.containsKey("endDate") || !jSONObject.containsKey("currency")) {
        return new ErrorResponse("E001", "缺少必要的欄位");
    }

    String startDateString = jSONObject.get("startDate").toString();
    String endDateString = jSONObject.get("endDate").toString();
    String currency = jSONObject.get("currency").toString();

    // 檢查日期格式
    if (!isValidDateFormat(startDateString) || !isValidDateFormat(endDateString)) {
        return new ErrorResponse("E001", "日期格式不正確");
    }

    // 檢查日期區間
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    Date startDate = sdf.parse(startDateString);
    Date endDate = sdf.parse(endDateString);
    try {

        if (endDate.before(startDate)) {
            return new ErrorResponse("E001", "結束日期不能早於開始日期");
        }

        // 檢查日期區間是否在一年之內
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date oneYearAgo = calendar.getTime();

        if (startDate.before(oneYearAgo)) {
            return new ErrorResponse("E001", "日期區間僅限一年內");
        }

        // 解析日期字串為 Date 物件
        SimpleDateFormat dFt = new SimpleDateFormat("yyyy-MM-dd");

        // 執行 MongoDB 查詢
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        
         // 選擇要使用的資料庫
         MongoDatabase database = mongoClient.getDatabase("mydatabase");


        MongoCollection<Document> collection = database.getCollection("exchange_rates"); // 使用延遲初始化
        if (collection != null) {
            for (Document document : collection.find(Filters.and(
                    Filters.gte("Date", startDate),
                    Filters.lte("Date", endDate)
            ))) {
                ExchangeRate rate = new ExchangeRate();
                rate.setDate(document.getDate("Date"));
                rate.setUSD_NTD(document.getString("USD_NTD"));
                result.add(rate);
            }
            ErrorResponse error = new ErrorResponse("0000", "成功");

            System.out.println(result.size());
             // 查詢集合中的所有文件
          for (Document document : collection.find()) {
            System.out.println(document.toJson());
        }
            //    JSONObject returnJo = new JsonObject();

            System.out.println(result.size());
            return error;
        } else {
            return new ErrorResponse("E001", "無法連接到數據庫");
        }
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("e==" + e.toString());
        return new ErrorResponse("E001", "出錯");
    }
}


private boolean isValidDateFormat(String dateString) {
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        sdf.setLenient(false);
        sdf.parse(dateString);
        return true;
    } catch (ParseException e) {
        return false;
    }
}

class ErrorResponse {
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

}

public class SuccessResponse {
    private ErrorResponse error;
    private List<Map<String, String>> currency;

    public SuccessResponse(ErrorResponse error, List<Map<String, String>> currency) {
        this.error = error;
        this.currency = currency;
    }

    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }

    public List<Map<String, String>> getCurrency() {
        return currency;
    }

    public void setCurrency(List<Map<String, String>> currency) {
        this.currency = currency;
    }
}
  

}