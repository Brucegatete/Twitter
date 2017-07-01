package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by brucegatete on 6/26/17.
 */

public class Tweet{
    public  String body;
    public long uid;
    public User user;
    public String createdAt;
    public boolean retweeted;
    public boolean liked;




    public Tweet() {

    }

    //deserialize the data
    public  static Tweet fromJSON (JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        //Extract the values from JSON;
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        tweet.liked = jsonObject.getBoolean("favorited");
        return tweet;

    }

}
