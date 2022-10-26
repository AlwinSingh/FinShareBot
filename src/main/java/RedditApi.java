package main.java;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class RedditApi {
    public JSONObject getPostsBySubreddit(String subRedditName) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.reddit.com/r/" + subRedditName + "/new.json")
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            return selectAThread(new JSONObject(response.body().string()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject selectAThread(JSONObject subredditData) {
        JSONArray subredditThreads = subredditData.getJSONObject("data").getJSONArray("children");

        if (subredditThreads.length() > 0) {
            Random random = new Random();
            int max = subredditThreads.length() - 1;
            int min = 0;
            int randomNumber = random.nextInt(max + 1 - min) + min;
            JSONObject selectedThreadObj = subredditThreads.getJSONObject(randomNumber).getJSONObject("data");
            //System.out.println(selectedThreadObj);
            JSONObject simplifiedThreadObj = new JSONObject();
            simplifiedThreadObj.put("author", selectedThreadObj.getString("author"));
            simplifiedThreadObj.put("url", selectedThreadObj.getString("url"));
            simplifiedThreadObj.put("title", selectedThreadObj.getString("title"));
            return simplifiedThreadObj;
        } else {
            return null;
        }
    }
}
