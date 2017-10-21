package com.example.douglas.upcomingmovies.model;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Douglas on 21/10/2017.
 */

public class Movie {
    private long id;
    private String title;
    private JSONArray genreIds;
    private String releaseDate;
    private String posterPath;
    private RoundedBitmapDrawable image;
    private String overview;


    public Movie(long id, String title, JSONArray genreIds, String releaseDate, String posterPath,String overview){
        this.id = id;
        this.title = title;
        this.genreIds = genreIds;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.overview = overview;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public JSONArray getGenreIds() {
        return genreIds;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public RoundedBitmapDrawable getImage(){ return image;}

    public String getPosterPath() {
        return posterPath;
    }

    @Override
    public String toString() {
        String movieInfoRepresentation = "";
        String title="Name:\n"+this.title;
        String genre="\nGenre:\n"+getGenreString(this.genreIds);
        String releaseDate= "\nRelease Date:\n"+this.getReleaseDate();
        movieInfoRepresentation = title+genre+releaseDate;
        return movieInfoRepresentation;
    }

    public String getFullDescrition() {
        String movieInfoRepresentation = "";
        String title="Name:\n"+getTitle()+"\n";
        String genre="\nGenre:\n"+getGenreString(this.genreIds)+"\n";
        String overview="\nOverview:\n"+getOverview()+"\n";
        String releaseDate= "\nRelease Date:\n"+getReleaseDate()+"\n";
        movieInfoRepresentation = title+genre+overview+releaseDate+"\n";
        return movieInfoRepresentation;
    }



    public void setImage(RoundedBitmapDrawable image) {
        this.image = image;
    }

    public static String[] toStringArray(JSONArray array) {
        if(array==null)
            return null;

        String[] strArray=new String[array.length()];
        for(int i=0; i<strArray.length; i++) {
            strArray[i]=array.optString(i);
        }
        return strArray;
    }

    private String getGenreString(JSONArray genreIds) {
        String[] genreIdsArray = toStringArray(genreIds);
        if(genreIds.length() == 0){
            return "No Genre";
        }
        switch (Integer.valueOf(genreIdsArray[0])){
            case 28:
                return "Action";
            case 12:
                return "Adventure";
            case 16:
                return "Animation";
            case 35:
                return "Comedy";
            case 80:
                return "Crime";
            case 99:
                return "Documentary";
            case 18:
                return "Drama";
            case 10751:
                return "Family";
            case 14:
                return "Fantasy";
            case 36:
                return "History";
            case 27:
                return "Horror";
            case 10402:
                return "Music";
            case 9648:
                return "Mystery";
            case 10749:
                return "Romance";
            case 878:
                return "Science Fiction";
            case 10770:
                return "TV Movie";
            case 53:
                return "Thriller";
            case 10752:
                return "War";
            case 37:
                return "Western";
            default:
                return "No Genre";
        }
    }


}
