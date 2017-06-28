package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by brucegatete on 6/26/17.
 */

public class User implements Parcelable{

    //list all the attributes
    public String name;
    public long uid;
    public  String profileImageUrl;
    public String screenName;


    protected User(Parcel in) {
        name = in.readString();
        uid = in.readLong();
        profileImageUrl = in.readString();
        screenName = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User() {

    }

    //deserialize the JSON
    public static User fromJSON (JSONObject json) throws JSONException{

        User user = new User();

        // extract and fill out values
        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.profileImageUrl = json.getString("profile_image_url");
        user.screenName = json.getString("screen_name");
        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeLong(uid);
        parcel.writeString(profileImageUrl);
        parcel.writeString(screenName);
    }
}
