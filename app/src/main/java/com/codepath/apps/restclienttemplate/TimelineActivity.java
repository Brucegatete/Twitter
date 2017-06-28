package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    //Request code for receiving the data sent between activities
    private final int REQUEST_CODE = 20;
    private final int RESULT_OK = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApp.getRestClient();

        // find the recycler view
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);

        // init the arraylist (data sources)
        tweets = new ArrayList<>();
        //construct the adapter from this datasource
        tweetAdapter = new TweetAdapter(tweets);
        // RecyclerView setup (layout manager,  use the adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        //set the adapter
        rvTweets.setAdapter(tweetAdapter);

        populateTimeline();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    private void populateTimeline (){
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
              //  Log.d("TwitterClient", response.toString());
                // for each entry, deserializwe the JSOn object
                for(int i = 0; i < response.length(); i++ ){
                    // convert each object to a tweet model

                    // add that tweet model to our data source

                    // notify the adapter that we have added the item
                    Tweet tweet = null;
                    try {
                        tweet = Tweet.fromJSON(response.getJSONObject(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    tweets.add(tweet);
                    tweetAdapter.notifyItemInserted(tweets.size() - 1);;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

        });

    }

    //method that launches the composer
        public void launchComposeView(MenuItem miCompose) {
            Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
            startActivityForResult(i, REQUEST_CODE);
        }


    //  Time to handle the result of the sub-activity
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            // REQUEST_CODE is defined above
            if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
                // Extract name value from result extras
               Tweet tweet = data.getParcelableExtra("New tweet");
                tweets.add(0, tweet);
                tweetAdapter.notifyItemInserted(0);
                rvTweets.scrollToPosition(0);
            }
    }

}
