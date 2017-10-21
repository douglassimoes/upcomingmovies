package com.example.douglas.upcomingmovies.model;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Douglas on 21/10/2017.
 */

public class Movie {
    public long id;
    public String title;
    public String genreIds;
    public String releaseDate;
    public String posterPath;
    public Bitmap image;

    public Movie(long id, String title, String genreIds, String releaseDate, String posterPath){
        this.id = id;
        this.title = title;
        this.genreIds = genreIds;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenreIds() {
        return genreIds;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Bitmap getImage(){ return image;}

    public String getPosterPath() {
        return posterPath;
    }

    @Override
    public String toString() {
        Map<String, String> dictionary = new HashMap<String, String>();
        dictionary.put("id",String.valueOf(this.id));
        dictionary.put("title",this.title);
        dictionary.put("poster_path",this.posterPath);
        dictionary.put("genre_ids", String.valueOf(this.genreIds));
        dictionary.put("release_date", this.releaseDate);
        return dictionary.toString();
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
