package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by brucegatete on 6/26/17.
 */

public class User {

    //list all the attributes
    public String name;
    public long uid;
    public  String profileImageUrl;
    public String screenName;


    //deserialize the JSON
    public static User fromJSON (JSONObject json) throws JSONException{

        User user = new User();

        // extrat and fill out values
        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.profileImageUrl = json.getString("profile_image_url");
        user.screenName = json.getString("screen_name");

        return user;


    }
}
