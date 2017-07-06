package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by brucegatete on 7/3/17.
 */

public class TweetsListFragment extends Fragment implements TweetAdapter.TweetAdapterListener{
    public interface TweetSelectedListener{
        //handle tweet selection
        public void onTweetSelected(Tweet tweet);

    }

    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the layout
        View v = inflater.inflate(R.layout.fragments_tweets_list, container, false);
        // find the recycler view
        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweet);
        // initiate the arraylist (data sources)
        tweets = new ArrayList<>();
        //construct the adapter from this datasource
        tweetAdapter = new TweetAdapter(tweets, this);
        // RecyclerView setup (layout manager,  use the adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(getContext()));
        //set the adapter
        rvTweets.setAdapter(tweetAdapter);
        return  v;
    }

    public void addItems (JSONArray response) {
        try {
            for(int i = 0; i < response.length(); i++ ) {
                // convert each object to a tweet model
                Tweet tweet = null;
                tweet = Tweet.fromJSON(response.getJSONObject(i));
                // add that tweet model to our data source(the arrayList)
                tweets.add(tweet);
                // notify the adapter that we have added the item
                tweetAdapter.notifyItemInserted(tweets.size() - 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemSelected(View view, int position) {
        Tweet tweet = tweets.get(position);
        //Toast.makeText(getContext(), tweet.body, Toast.LENGTH_LONG).show();
        ((TweetSelectedListener)getActivity()).onTweetSelected(tweet);
    }
}

