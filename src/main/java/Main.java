package main.java;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class Main {
    private RedditApi redditApi = new RedditApi();
    private TwitterApi twitterApi = new TwitterApi();
    private YHFinanceApi yhApi = new YHFinanceApi();
    private YHFinanceJSONParser yhFinJsonParser = new YHFinanceJSONParser();

    public static void main(String[] args) {
        Main m = new Main();
        int userSetInterval = 1; //This is in minutes
        m.serviceScheduler(m, userSetInterval);
    }

    /* ServiceScheduler: Sets it such that the tweet only gets sent out at the timing interval (in seconds) set by the user */
    private void serviceScheduler(Main m, int tweetInterval) {
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println("Run...");
                if (m.isNYSEOpen()) {
                    m.getYahooFinancialNews(); //Todo: If no update in news, e.g. last tweet is similar to next tweet then no need to send out a tweet...
                    m.getYahooStockRecommendation();
                } else {
                    getSomeMemes();
                }
            }
        }, 0, 60000);
    }

    private boolean isNYSEOpen() {
        try {
            ZoneId zone = ZoneId.of("America/New_York");
            ZonedDateTime zdt = ZonedDateTime.now(zone);

            LocalTime rangeStart = LocalTime.parse("09:30:00");
            LocalTime rangeEnd = LocalTime.parse("16:00:00");

            LocalTime time = zdt.toLocalTime();
            if (!time.isBefore(rangeStart) && time.isBefore(rangeEnd)) {
                System.out.println("Yes");
                return true;
            } else {
                System.out.println("No");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    //When the NYSE is not open, we will send some memes instead!
    private void getSomeMemes() {
        String[] subRedditNames = {"memes","ProgrammerHumor"};
        int max = subRedditNames.length - 1;
        int min = 0;
        String subRedditNameSelected = subRedditNames[new Random().nextInt(max + 1 - min) + min];
        JSONObject redditPost = redditApi.getPostsBySubreddit(subRedditNameSelected);

        if (redditPost != null) {
            String tweetMsg = redditPost.getString("title") + " by " +
                    redditPost.getString("author") +
                    "\n" + redditPost.getString("url");
            twitterApi.sendTweet(tweetMsg);
        }
    }

    private void getYahooFinancialNews() {
        try {
            String region = "SG";
            JSONObject yhNewsJsonRaw = yhApi.getNews(region);
            JSONArray yhNewsJsonParsed = new JSONArray();

            if (yhNewsJsonRaw != null)
                yhNewsJsonParsed = yhFinJsonParser.newsJsonParser(yhNewsJsonRaw);

            for (int i = 0; i < yhNewsJsonParsed.length(); i++) {
                System.out.println("News article " + (i + 1) + " found!");
                System.out.println(yhNewsJsonParsed.get(i));
                String tweetMessage = "Financial News!\n" +
                        "\n" + yhNewsJsonParsed.getJSONObject(i).getString("summary") +
                        "\n" + yhNewsJsonParsed.getJSONObject(i).getString("url");

                twitterApi.sendTweet(tweetMessage);
            }
        } catch (Exception e) {
            System.out.println("Failed to publish article(s) to Twitter!");
            e.printStackTrace();
        }
    }

    private void getYahooStockRecommendation() {
        try {
            String symbol = "INTC";
            String listOfStocksAsString = "";
            JSONObject yhStockRecommendationJsonRaw = yhApi.getRecommendations(symbol);
            JSONArray yhStockRecommendationsJsonParsed = new JSONArray();

            if (yhStockRecommendationJsonRaw != null)
                yhStockRecommendationsJsonParsed = yhFinJsonParser.stockRecommendationJsonParser(yhStockRecommendationJsonRaw);

            for (int i = 0; i < yhStockRecommendationsJsonParsed.length(); i++) {
                System.out.println("Stock recommendation " + (i + 1) + " found!");
                System.out.println(yhStockRecommendationsJsonParsed.get(i));
                JSONObject stockToRecommend = yhStockRecommendationsJsonParsed.getJSONObject(i);
                listOfStocksAsString = listOfStocksAsString + (i + 1) + ". "
                        //+ stockToRecommend.getString("stockShortName")
                        + stockToRecommend.getString("stockSymbol")
                        + " - " + stockToRecommend.getDouble("stockRegularMarketPrice")
                        + " USD\n";
            }

            String tweetMessage = "Financial stocks recommendation!\nWe have got a total of " +
                    yhStockRecommendationsJsonParsed.length() +
                    " for you to look at!\n\n" +
                    listOfStocksAsString;
            twitterApi.sendTweet(tweetMessage);
        } catch (Exception e) {
            System.out.println("Failed to publish recommendation(s) to Twitter!");
            e.printStackTrace();
        }
    }
}
