package com.example.douglas.upcomingmovies.utilities;

import android.util.Log;

import com.example.douglas.upcomingmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Douglas on 21/10/2017.
 */

public class JsonHandler {

    public List<Movie> readResultsArray(JSONArray resultsArray) throws IOException {
        List<Movie> results = new ArrayList<Movie>();

        for (int i=0; i < resultsArray.length(); i++) {
            try {
                JSONObject movieInfo = resultsArray.getJSONObject(i);
                long id = movieInfo.getLong("id");
                String posterPath = getPosterPath(movieInfo);
                String title = movieInfo.getString("title");
                String genreIds = movieInfo.getString("genre_ids");
                String releaseDate = movieInfo.getString("release_date");
                results.add(new Movie(id,title,genreIds,releaseDate,posterPath));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.i("movies",results.toString());

        return results;
    }
    public String getPosterPath(JSONObject movieInfo) throws JSONException {
        String poster_path = movieInfo.getString("poster_path");
        if(poster_path == "null"){
            return "";
        }else{
            return poster_path;
        }

    }
}
