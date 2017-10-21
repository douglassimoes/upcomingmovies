package com.example.douglas.upcomingmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.douglas.upcomingmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Douglas on 21/10/2017.
 */

public class MovieServiceAdapter extends RecyclerView.Adapter<MovieServiceAdapter.MovieServiceViewHolder> {

    private String[] mMovieData;

    @Override
    public MovieServiceAdapter.MovieServiceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movieservice_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieServiceAdapter.MovieServiceViewHolder movieViewHolder, int position) {
        String dataForThisMovie = mMovieData[position];
        movieViewHolder.mMovieServiceTextView.setText(dataForThisMovie);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.length;
    }

    public class MovieServiceViewHolder extends RecyclerView.ViewHolder {
        public final TextView mMovieServiceTextView;

        public MovieServiceViewHolder(View view) {
            super(view);
            mMovieServiceTextView = (TextView) view.findViewById(R.id.tv_movie_service);
        }
    }

    public void setMovieData(String jsonMovieData) {
        try {
            mMovieData = toStringArray(new JSONObject(jsonMovieData).getJSONArray("results"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }

    public static String[] toStringArray(JSONArray array) {

        if(array==null)
            return null;
        String[] newArray=new String[array.length()];
        for(int i=0; i<newArray.length; i++) {
            newArray[i]=array.optString(i);
        }
        return newArray;
    }
}
