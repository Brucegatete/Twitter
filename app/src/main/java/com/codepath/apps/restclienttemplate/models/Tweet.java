package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by brucegatete on 6/26/17.
 */

public class Tweet implements Parcelable{
    public  String body;
    public long uid;
    public User user;
    public String createdAt;



    protected Tweet(Parcel in) {
        body = in.readString();
        uid = in.readLong();
        createdAt = in.readString();
        user = in.readParcelable(User.class.getClassLoader());

    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

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
        return tweet;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
//        out.writeInt(mData);
//        out.writeString(mName);
//        out.writeParcelable(mInfo, i);
        parcel.writeString(body);
        parcel.writeLong(uid);
        parcel.writeString(createdAt);
        parcel.writeParcelable(user, i);

    }
}
