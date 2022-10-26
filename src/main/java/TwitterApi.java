package main.java;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterApi {
    public void sendTweet(String msg) {
        try {
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(ApiKey.TWITTER_API_CONSUMER_KEY)
                    .setOAuthConsumerSecret(ApiKey.TWITTER_API_CONSUMER_SECRET_KEY)
                    .setOAuthAccessToken(ApiKey.TWITTER_ACCESS_TOKEN)
                    .setOAuthAccessTokenSecret(ApiKey.TWITTER_ACCESS_TOKEN_SECRET);

            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();
            twitter.updateStatus(msg);
            System.out.println("Successfully sent a tweet!");
        } catch (Exception e) {
            System.out.println("Tweet failed to send!");
            e.printStackTrace();
        }
    }
}
