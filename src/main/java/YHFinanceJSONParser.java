package main.java;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class YHFinanceJSONParser {
    public JSONArray stockRecommendationJsonParser(JSONObject jsonData) {
        try {
            JSONArray stockList = jsonData.getJSONObject("finance").getJSONArray("result").getJSONObject(0).getJSONArray("quotes");
            JSONArray stockObjectParsedArray = new JSONArray();

            for (int i = 0; i < stockList.length(); i++) {
                JSONObject stockObject = stockList.getJSONObject(i);
                JSONObject stockObjectParsed = new JSONObject();

                String stockType = stockObject.getString("quoteType");
                String stockShortName = stockObject.getString("shortName");
                String stockSymbol = stockObject.getString("symbol");
                String stockExchange = stockObject.getString("exchange");
                double stockRegularMarketPrice = stockObject.getDouble("regularMarketPrice");

                stockObjectParsed.put("stockType",stockType);
                stockObjectParsed.put("stockShortName",stockShortName);
                stockObjectParsed.put("stockSymbol",stockSymbol);
                stockObjectParsed.put("stockExchange",stockExchange);
                stockObjectParsed.put("stockRegularMarketPrice",stockRegularMarketPrice);

                stockObjectParsedArray.put(stockObjectParsed);
            }
            return stockObjectParsedArray; //Return an array of the parsed objects
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray newsJsonParser(JSONObject jsonData) {
        try {
            ArrayList<String> financeStockAffectedList = new ArrayList<String>(); //Keep track of the affected stocks from the news
            JSONArray newsArticles = jsonData.getJSONObject("data").getJSONArray("contents");
            JSONArray articleObjectParsedArray = new JSONArray();

            for (int i = 0; i < newsArticles.length(); i++) {
                JSONObject newsArticleContent = newsArticles.getJSONObject(i).getJSONObject("content");
                JSONObject articleObjectParsed = new JSONObject();

                String newsArticleTitle = newsArticleContent.getString("title");
                String newsArticleSummary = newsArticleContent.getString("summary");
                String newsArticlePublishDate = newsArticleContent.getString("pubDate");
                String newsUrl = newsArticleContent.getJSONObject("canonicalUrl").getString("url");

                try {
                    //Todo: Change to SG Timezone!
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                    DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                    LocalDateTime date = LocalDateTime.parse(newsArticlePublishDate, formatter);
                    newsArticlePublishDate = date.format(newFormatter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray financeStocksAffected = newsArticleContent.getJSONObject("finance").getJSONArray("stockTickers");
                    for (int j = 0; j < financeStocksAffected.length(); j++) {
                        String financeStockAffected = financeStocksAffected.getJSONObject(j).getString("symbol");
                        financeStockAffectedList.add(financeStockAffected);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                articleObjectParsed.put("pubDate", newsArticlePublishDate);
                articleObjectParsed.put("title", newsArticleTitle);
                articleObjectParsed.put("summary", newsArticleSummary);
                articleObjectParsed.put("url", newsUrl);
                articleObjectParsed.put("stocks", financeStockAffectedList);
                articleObjectParsedArray.put(articleObjectParsed);
            }
            return articleObjectParsedArray; //Returns an array of the parsed news articles
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}