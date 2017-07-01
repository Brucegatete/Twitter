package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    //define all the attributes
    TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    ImageView ivReply;

    //Request code for receiving the data sent between activities
    private final int REQUEST_CODE = 20;
    private final int RESULT_OK = 12;

    //swipe container for reflesh.
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        //initiate the Twitter client
        client = TwitterApp.getRestClient();
        // find the recycler view
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        // initiate the arraylist (data sources)
        tweets = new ArrayList<>();
        //construct the adapter from this datasource
        tweetAdapter = new TweetAdapter(tweets);
        // RecyclerView setup (layout manager,  use the adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        //set the adapter
        rvTweets.setAdapter(tweetAdapter);
        //set the image button for replying
        ivReply = (ImageButton) findViewById(R.id.ivReply);
        //set an onClickListener
        populateTimeline();


        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tweetAdapter.clear();
                populateTimeline();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    //method to populate the timeline
    private void populateTimeline (){
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // for each entry, deserializwe the JSOn object
                for(int i = 0; i < response.length(); i++ ){
                    // convert each object to a tweet model
                    Tweet tweet = null;
                    try {
                        tweet = Tweet.fromJSON(response.getJSONObject(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // add that tweet model to our data source(the arrayList)
                    tweets.add(tweet);
                    // notify the adapter that we have added the item
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
            i.putExtra("code", REQUEST_CODE);
            startActivityForResult(i, REQUEST_CODE);
        }


    // handle the result of the sub-activity
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == RESULT_OK && (requestCode == REQUEST_CODE || requestCode == tweetAdapter.getREP_CODE())) {
                // Extract name value from result extras
                Tweet tweet = data.getParcelableExtra("New Tweet");
                //add the tweet to the first row of the view
                tweets.add(0, tweet);
                //notify the adapter that a new item has been inserted
                tweetAdapter.notifyItemInserted(0);

                //scroll back to the first position
                rvTweets.scrollToPosition(0);
            }
        }

    public int getREQUEST_CODE() {
        return REQUEST_CODE;
    }


}

