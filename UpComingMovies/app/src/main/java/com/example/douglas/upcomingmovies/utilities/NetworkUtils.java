package com.example.douglas.upcomingmovies.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Douglas on 21/10/2017.
 */

/**
 * This class is a module for our application meaning that isolates network functions from the rest
 * of the application.
 */
public class NetworkUtils {
    final static String UP_COMMING_MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/upcoming";
    final static String MOVIES_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    final static String API_KEY = "1f54bd990f1cdfb230adb312546d765d";

    final static String PARAM_API_KEY = "api_key";
    final static String PARAM_LANGUAGE = "language";
    final static String PARAM_PAGE = "page";

    /**
     * This method takes care of the url build. It parses the uri and uses an Uri.Builder to build
     * correct easier and with perfect format.
     * @param pageNumber
     * @return
     */
    public static URL buildUrl(String pageNumber){
        Uri builtUri = Uri.parse(UP_COMMING_MOVIES_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, "en-US")
                .appendQueryParameter(PARAM_PAGE, pageNumber)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e("movies","Build url failed.");
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method build urls, but it is different from the buildUrl, because the Uri building
     * pattern to get images is different.
     * @param posterPath
     * @return
     */
    public static URL buildImgUrl(String posterPath) {
        Uri builtUri = Uri.parse(MOVIES_IMAGE_BASE_URL+posterPath);

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e("movies","Build image url failed.");
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method handles urlConnections and its main reason is to return the api's response to the
     * Activies.
     * @param moviesUrl
     * @return
     */
    public static String getResponseFromHttpUrl(URL moviesUrl){
        HttpURLConnection urlConnection = null;
        String result=null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) moviesUrl.openConnection();
            inputStream = urlConnection.getInputStream();
            String jsonDefault = "UTF-8";
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, jsonDefault), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();
            return result;

        } catch (IOException e) {
            Log.e("movies", "Response from http failed.");
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }

        return null;
    }

    /**
     * This method handle urlConnections and it decodes images using BitmapFactory.
     * @param moviesUrl
     * @return
     */
    public static Bitmap getImageResponseFromHttpUrl(URL moviesUrl){
        HttpURLConnection urlConnection = null;
        String result=null;
        InputStream inputStream = null;
        Bitmap image;
        try {
            urlConnection = (HttpURLConnection) moviesUrl.openConnection();
            inputStream = urlConnection.getInputStream();
            image = BitmapFactory.decodeStream(inputStream);
            return image;
        } catch (IOException e) {
            Log.e("movies", "Response from http failed.");
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }

        return null;
    }


}
