package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by brucegatete on 6/26/17.
 */

public class TweetAdapter extends  RecyclerView.Adapter<TweetAdapter.ViewHolder> implements View.OnClickListener{

    private List<Tweet> mTweets;
    Context context;
    private final int REP_CODE = 10;
    private TwitterClient  client;
    private  TweetAdapterListener mListener;


    //define an interface required by the viewholder
    public interface TweetAdapterListener {
        public void onItemSelected (View view, int position);
    }


    //pass in the tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets, TweetAdapterListener listener) {
        mTweets = tweets;
        mListener = listener;
    }

    public int getREP_CODE() {
        return REP_CODE;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet,parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return  viewHolder;
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

    //bind the values based on the position of the element

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //get the data according to position
        final Tweet tweet = mTweets.get(position);

        // populate the views according to the data
        holder.tvUserName.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvRelativeTime.setText(getRelativeTimeAgo(tweet.createdAt));
        holder.tvScreenName.setText("@" + tweet.user.screenName);
        holder.btReTweet.findViewById(R.id.ivRetweet);
        holder.tvLikeCount.setText(tweet.likeCount + "");
        holder.tvRetweetCount.setText(tweet.retweetCount + "");
        holder.ivHeart.setSelected(tweet.liked);
        holder.ivProfileImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent (context, ProfileActivity.class);
                i.putExtra("screen_name", tweet.user.screenName);
                (context).startActivity(i);
            }
        });

        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .into(holder.ivProfileImage);

        // instantiate the client
        client = TwitterApp.getRestClient();

        holder.ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ComposeActivity.class);
                i.putExtra("screen_name", tweet.user.screenName);
                i.putExtra("uid", tweet.uid);
                i.putExtra("code", REP_CODE);
                ((Activity) context).startActivityForResult(i, REP_CODE);
            }
        });

        holder.btReTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tweet.retweeted){
                    client.reTweet(Long.toString(tweet.uid), new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            holder.btReTweet.setPressed(true);
                            holder.tvRetweetCount.setText((tweet.retweetCount + 1) + "");
                            tweet.retweeted = true;
                            tweet.retweetCount += 1;
                            Toast.makeText(context, "Retweeted", Toast.LENGTH_SHORT).show();
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

                            holder.btReTweet.setPressed(false);
                            holder.tvRetweetCount.setText((tweet.retweetCount - 1) + "");
                            tweet.retweeted =  false;
                            tweet.retweetCount -= 1;
                            Toast.makeText(context, "unRetweeted", Toast.LENGTH_SHORT).show();
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

        holder.ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //client = TwitterApp.getRestClient();
                if (!tweet.liked) {
                    client.sendFavorite(Long.toString(tweet.uid), new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            holder.ivHeart.setPressed(true);
                            holder.tvLikeCount.setText((tweet.likeCount + 1) + "");
                            tweet.liked = true;
                            tweet.likeCount += 1;
                            Toast.makeText(context, "liked", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Toast.makeText(context, "failed", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(context, "unliked", Toast.LENGTH_LONG).show();
                            holder.ivHeart.setPressed(false);
                            holder.tvLikeCount.setText((tweet.likeCount - 1) + "");
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
    }

    // create ViewHolder class
    public  class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvRelativeTime;
        public  TextView tvScreenName;
        public ImageView ivReply;
        public ImageView btReTweet;
        public  ImageView ivHeart;
        public TextView tvLikeCount;
        public TextView tvRetweetCount;

        public ViewHolder(View itemView) {
            super(itemView);
            //perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvRelativeTime = (TextView) itemView.findViewById(R.id.tvRelativeTime);
            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
            ivReply = (ImageView) itemView.findViewById(R.id.ivReply);
            btReTweet = (ImageView) itemView.findViewById(R.id.ivRetweet);
            ivHeart = (ImageView) itemView.findViewById(R.id.ivHeart);
            tvLikeCount = (TextView) itemView.findViewById(R.id.tvLikeCount);
            tvRetweetCount = (TextView) itemView.findViewById(R.id.tvRetweetCount);

//            ivProfileImage.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View view) {
//                    int position = getAdapterPosition();
//                    mListener.onItemSelected(view, position);
//                }
//            });


            //handle row click event
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        //get the position of row element
                        int position = getAdapterPosition();
                        // fire the listener callback
                        mListener.onItemSelected(view, position);
                    }
                }
            });
        }
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
    //constructors
    @Override
    public int getItemCount() {
        return mTweets.size();
    }
    @Override
    public void onClick(View view) {

    }
}
