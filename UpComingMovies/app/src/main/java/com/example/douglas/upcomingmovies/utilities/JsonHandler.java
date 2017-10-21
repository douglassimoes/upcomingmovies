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

/**
 * This class handle some of the JSON parsing processes, making the other parts of the code cleaner
 * and easier to understand.
 */
public class JsonHandler {

    /**
     * This method parses the results, filtering what the application need. This method could be
     * easily modified to support more informations on future releases.
     * @param resultsArray
     * @return
     * @throws IOException
     */
    public List<Movie> readResultsArray(JSONArray resultsArray) throws IOException {
        List<Movie> results = new ArrayList<Movie>();

        for (int i=0; i < resultsArray.length(); i++) {
            try {
                JSONObject movieInfo = resultsArray.getJSONObject(i);
                long id = movieInfo.getLong("id");
                String posterPath = getPosterPath(movieInfo);
                String title = movieInfo.getString("title");
                String overview = movieInfo.getString("overview");
                JSONArray genreIds = movieInfo.getJSONArray("genre_ids");
                String releaseDate = movieInfo.getString("release_date");
                results.add(new Movie(id,title,genreIds,releaseDate,posterPath,overview));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    /**
     * This method solves the problem of poster_path=null, that is returned by the api in some cases.
     * @param movieInfo
     * @return
     * @throws JSONException
     */
    public String getPosterPath(JSONObject movieInfo) throws JSONException {
        String poster_path = movieInfo.getString("poster_path");
        if(poster_path == "null"){
            return "";
        }else{
            return poster_path;
        }

    }
}
