package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.codepath.apps.restclienttemplate.models.Tweet;

import fragments.TweetsListFragment;
import fragments.TweetsPagerAdapter;

public class TimelineActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener{
    //define all the attributes

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

        //set the image button for replying
        ivReply = (ImageButton) findViewById(R.id.ivReply);
        //set an onClickListener

        //get the view pager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        //set the adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(), this));
        //setup the TabLayout to the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);



//        // Lookup the swipe container view
//        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
//        // Setup refresh listener which triggers new data loading
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                tweetAdapter.clear();
//                populateTimeline();
//                swipeContainer.setRefreshing(false);
//            }
//        });
//        // Configure the refreshing colors
//        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }


    //method that launches the composer
        public void launchComposeView(MenuItem miCompose) {
            Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
            i.putExtra("code", REQUEST_CODE);
            startActivityForResult(i, REQUEST_CODE);
        }


    public void onProfileView(MenuItem item) {
        //launch the profile view
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }


    @Override
    public void onTweetSelected(Tweet tweet) {
        Intent i = new Intent(TimelineActivity.this, DetailedActivity.class);
        i.putExtra("tweet", tweet);
        startActivity(i);
    }

    // handle the result of the sub-activity
//        @Override
//        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//            if (resultCode == RESULT_OK && (requestCode == REQUEST_CODE || requestCode == tweetAdapter.getREP_CODE())) {
//                // Extract name value from result extras
//                Tweet tweet = data.getParcelableExtra("New Tweet");
//                //add the tweet to the first row of the view
//                tweets.add(0, tweet);
//                //notify the adapter that a new item has been inserted
//                tweetAdapter.notifyItemInserted(0);
//
//                //scroll back to the first position
//                rvTweets.scrollToPosition(0);
//            }
//        }

//    public int getREQUEST_CODE() {
//        return REQUEST_CODE;
//    }

}

