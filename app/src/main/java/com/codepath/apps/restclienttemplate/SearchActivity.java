package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import fragments.SearchTweetsFragment;

/**
 * Created by brucegatete on 7/5/17.
 */

public class SearchActivity extends AppCompatActivity {
    Context context;
    String query;
    TwitterClient client;
    SearchTweetsFragment searchTweetsFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        query = getIntent().getStringExtra("query");
//        if(savedInstanceState == null){
//            SearchTweetsFragment searchFragment = (SearchTweetsFragment)
//                    getSupportFragmentManager().findFragmentById(R.id.action_search);
//
//        }
        //create the user fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

         searchTweetsFragment= SearchTweetsFragment.newInstance(query);

        //make changes
        ft.replace(R.id.content, searchTweetsFragment);
        //commit
        ft.commit();

    }
}
