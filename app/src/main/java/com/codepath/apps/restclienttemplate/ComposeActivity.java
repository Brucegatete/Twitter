package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity{
    //define the button that
    public Button btTweet;
    private TwitterClient client;
    private final int RESULT_OK = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApp.getRestClient();
    }


    //Receive the result from the timeline activity
    public void onSubmit(View v) {
      EditText etName = (EditText) findViewById(R.id.et_simple);
      String body = etName.getText().toString();

//        Log.d("DEBUGG", body);


        client.sendTweet(body, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Tweet tweet = null;
                try {
                     tweet = Tweet.fromJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Debug", tweet.body.toString());
                // Prepare data intent
                Intent data = new Intent();
                // Pass relevant data back as a result
                data.putExtra("New tweet", tweet);
                // Activity finished ok, return the data
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }



//    //method that enables to return to the main activity
//        public void onSubmit(View v) {
//            // closes the activity and returns to first screen
//            this.finish();
    }

