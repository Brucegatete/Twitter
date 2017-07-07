package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import fragments.UserTimelineFragment;

public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;
    String tagline;
    String screenName;
    String profileImageUrl;
    int followersCount;
    int followingCount;
    long uid;
    String self_screen_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        screenName = getIntent().getStringExtra("screen_name");
        tagline = getIntent().getStringExtra("tagline");
        profileImageUrl = getIntent().getStringExtra("profile_image_url");
        followersCount = getIntent().getIntExtra("followers", 0);
        followingCount = getIntent().getIntExtra("following", 0);
        uid = getIntent().getLongExtra("id", 0);
        self_screen_name = getIntent().getStringExtra("user_id");

        //create the user fragment
        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //make changes
        ft.replace(R.id.flContainer, userTimelineFragment);
        //commit
        ft.commit();

        client = TwitterApp.getRestClient();
        if (screenName == null){
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //deserialize the user object
                    User user;
                    try {
                        user = User.fromJSON(response);
                        self_screen_name = user.screenName;
                        //set title of the action bar
                        getSupportActionBar().setTitle("@" + user.screenName);
                        //populate the user headline
                        populateUserHeadline(user);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else{
            client.showUserInfo(screenName, self_screen_name, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    User user = null;
                    ;
                    try {
                        user = User.fromJSON(response);
                        self_screen_name = user.screenName;
                        getSupportActionBar().setTitle("@" + user.screenName);
                        //populate the user headline
                        populateUserHeadline(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    public void populateUserHeadline (User user){
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName.setText(user.name);
        tvTagline.setText(user.tagline);
        tvFollowers.setText(user.followersCount + "Followers");
        tvFollowing.setText(user.followingCount + "Following");

        Glide.with(this).load(user.profileImageUrl).into(ivProfileImage);

    }
}
