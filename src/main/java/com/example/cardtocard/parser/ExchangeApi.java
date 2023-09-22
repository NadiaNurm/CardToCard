package com.example.cardtocard.parser;

import com.example.cardtocard.models.Currency;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangeApi {
    List<Currency> availableCurr = List.of(Currency.values());

    public static HashMap<String, BigDecimal> exchangeList() throws IOException {
        // Setting URL
        String url_str = "https://v6.exchangerate-api.com/v6/8ade3cc4d0c53866403c51d0/latest/USD";

        // Making Request
        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        // Convert to JSON
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonobj = root.getAsJsonObject();

        // Accessing object

        String req_result = String.valueOf(jsonobj.get("conversion_rates")).replaceAll("\\}", "");
        HashMap<String, BigDecimal> conversion_rates = new HashMap<>();
        for (String s : req_result.split(",")) {
            String key = s.split(":")[0].replaceAll("[^A-Z]", "");
            BigDecimal value = new BigDecimal(s.split(":")[1]);
            if (checkCurr(key)) {
                conversion_rates.put(key, value);
            }
        }
        return conversion_rates;
        //for (Map.Entry<String, BigDecimal> entry : conversion_rates.entrySet()){
        //    System.out.println(entry.getKey() +" : "+entry.getValue());
        //}
    }

    public static BigDecimal pairExchange(String base, String target, BigDecimal amount) throws IOException {
        String url_str = "https://v6.exchangerate-api.com/v6/8ade3cc4d0c53866403c51d0/pair/" + target + "/" + base;
        // Making Request
        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        // Convert to JSON
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonobj = root.getAsJsonObject();

        // Accessing object
        String req_result = String.valueOf(jsonobj.get("conversion_rate")).replaceAll("\\}", "");
        BigDecimal b = new BigDecimal(req_result);
        BigDecimal result = amount.multiply(b);
        return result;
    }


    public ExchangeApi() throws IOException {

    }

    public static boolean checkCurr(String currency) {
        try {
            Currency.valueOf(currency);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
