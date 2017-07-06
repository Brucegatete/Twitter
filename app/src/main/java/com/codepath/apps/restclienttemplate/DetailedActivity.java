package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * Created by brucegatete on 7/5/17.
 */

public class DetailedActivity extends AppCompatActivity{
    private Tweet tweet;

    TwitterClient client;

    public  TextView tvSreenName;
    public  TextView tvRelativeTime;
    public ImageView ivProfileImage;
    public TextView tvUsername;
    public TextView tvBody;
    public ImageView ivReply;
    public TextView tvLikeCount;
    public ImageView ivHeart;
    public ImageView ivRetweet;
    public TextView tvRetweetCount;

    private final int REP_CODE = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        client = TwitterApp.getRestClient();
        tweet = getIntent().getParcelableExtra("tweet");
        Toast.makeText(this, tweet.body, Toast.LENGTH_LONG).show();
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvLikeCount = (TextView) findViewById(R.id.tvLikeCount);
        tvRetweetCount = (TextView) findViewById(R.id.tvRetweetCount);
        ivRetweet = (ImageView) findViewById(R.id.ivRetweet);
        ivHeart = (ImageView) findViewById(R.id.ivHeart);
        ivReply = (ImageView) findViewById(R.id.ivReply);
        tvUsername = (TextView) findViewById(R.id.tvUserName);
        tvRelativeTime = (TextView) findViewById(R.id.tvRelativeTime);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvSreenName = (TextView) findViewById(R.id.tvScreenName);
        tvLikeCount.setText(tweet.likeCount + "");
        tvRelativeTime.setText(TweetAdapter.getRelativeTimeAgo(tweet.createdAt));
        tvUsername.setText(tweet.user.screenName);
        tvBody.setText(tweet.body);
        Glide.with(this).load(tweet.user.profileImageUrl).into(ivProfileImage);

        ivReply.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailedActivity.this, ComposeActivity.class);
                i.putExtra("screen_name", tweet.user.screenName);
                i.putExtra("uid", tweet.uid);
                i.putExtra("code", REP_CODE);
                startActivityForResult(i, REP_CODE);
            }
        });

        ivHeart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //client = TwitterApp.getRestClient();
                if (!tweet.liked) {
                    client.sendFavorite(Long.toString(tweet.uid), new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            ivHeart.setPressed(true);
                            tvLikeCount.setText((tweet.likeCount + 1) + "");
                            tweet.liked = true;
                            tweet.likeCount += 1;
                            Toast.makeText(DetailedActivity.this, "liked", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Toast.makeText(DetailedActivity.this, "failed", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                    });
                }else {

                    client.unSendFavorite(Long.toString(tweet.uid), new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Toast.makeText(DetailedActivity.this, "unliked", Toast.LENGTH_LONG).show();
                            ivHeart.setPressed(false);
                            tvLikeCount.setText((tweet.likeCount - 1) + "");
                            tweet.liked = false;
                            tweet.likeCount -= 1;
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });

                }
            }
        });

        ivRetweet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (!tweet.retweeted){
                    client.reTweet(Long.toString(tweet.uid), new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            ivRetweet.setPressed(true);
                            tvRetweetCount.setText((tweet.retweetCount + 1) + "");
                            tweet.retweeted = true;
                            tweet.retweetCount += 1;
                            Toast.makeText(DetailedActivity.this, "Retweeted", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }else{
                    client.unReTweet(Long.toString(tweet.uid), new JsonHttpResponseHandler(){

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            ivRetweet.setPressed(false);
                            tvRetweetCount.setText((tweet.retweetCount - 1) + "");
                            tweet.retweeted = false;
                            tweet.retweetCount -= 1;
                            Toast.makeText(DetailedActivity.this, "unRetweeted", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }


            }
        });

    }
}
