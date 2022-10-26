package main.java;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

public class YHFinanceApi {
    //Symbol: INTC
    public JSONObject getRecommendations(String symbol) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://yh-finance.p.rapidapi.com/stock/v2/get-recommendations?symbol=" + symbol)
                    .get()
                    .addHeader("X-RapidAPI-Key", ApiKey.YAHOO_API_KEY)
                    .addHeader("X-RapidAPI-Host", "yh-finance.p.rapidapi.com")
                    .build();

            Response response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Region: SG
    public JSONObject getNews(String region) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://yh-finance.p.rapidapi.com/news/v2/get-details?uuid=9803606d-a324-3864-83a8-2bd621e6ccbd&region=" + region)
                    .get()
                    .addHeader("X-RapidAPI-Key", ApiKey.YAHOO_API_KEY)
                    .addHeader("X-RapidAPI-Host", "yh-finance.p.rapidapi.com")
                    .build();

            Response response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
