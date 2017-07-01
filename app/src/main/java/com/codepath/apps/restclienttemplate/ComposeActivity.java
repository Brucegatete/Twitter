package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity{
    //define the attributes
    private EditText et_simple;
    private TextView tvCounter;
    private TwitterClient client;
    private final int RESULT_OK = 12;
    String screen_name;


    //define and initiate the texteditor watcher for the character count
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s,int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            tvCounter.setText(String.valueOf(s.length()));
        }

        public void afterTextChanged(Editable s) {
            tvCounter.setText(140 - s.toString().length() + " Characters");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApp.getRestClient();
        tvCounter = (TextView) findViewById(R.id.tvCounter);
        et_simple = (EditText) findViewById(R.id.et_simple);
        et_simple.addTextChangedListener(mTextEditorWatcher);
        //set the screen_name on the reply activity
        screen_name =  getIntent().getStringExtra("screen_name");
        if (screen_name != null) {
            et_simple.setText("@" + screen_name);
        }
    }



    //Receive the result from the timeline activity
    public void onSubmit(View v) {
      EditText etName = (EditText) findViewById(R.id.et_simple);
      String body = etName.getText().toString();
        //connect to the network if(Integer.toString(getR))
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
                data.putExtra("New Tweet", tweet);
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
}

