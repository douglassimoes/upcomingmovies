package com.example.douglas.upcomingmovies;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.douglas.upcomingmovies.model.Movie;
import com.example.douglas.upcomingmovies.utilities.JsonHandler;
import com.example.douglas.upcomingmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private MovieServiceAdapter mMovieServiceAdapter;
    private int lastPageRequested=1;
    private final int MAX_PAGES=10;
    private List<Movie> fetchedMovies;

    /**
     * This method will create everything needed for start the application.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Recycler view make the action of calling views easier and it is more efficient than
         * call findViewById everytime. It makes the application more smooth maybe even more
         * user-friendly.
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movieservice);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMovieServiceAdapter = new MovieServiceAdapter();

        /**
         * The number max of pages was locked for avoid overflow of data. This could be
         * changed for a future release. Therefore this code means that everytime the user
         * reach(by scrolling) the last movie, the application will do one more request.
         */
        mMovieServiceAdapter.setOnBottomReachedListener(new MovieServiceAdapter.OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {
                if(lastPageRequested < MAX_PAGES) {
                    lastPageRequested+=1;
                    loadMovieData();
                }
            }
        });

        /**
         * The adapter is the component able to create new items and populates them on the screen
         * with data.
         */
        mRecyclerView.setAdapter(mMovieServiceAdapter);

        loadMovieData();
    }

    /**
     * This AsyncTask is almost a must have because the application need to do all the computations
     * before the draw of the next frame, creating a AsysncTask is a modular aproach leaving the
     * application free to be rendered while the content is being fetched.
     */
    public class UpComingMovieServiceTask extends AsyncTask<URL,Void,List<Movie>>{

        /**
         * This method is reponsible for call the http request and parse the first part of the
         * api's response.
         *
         * @param urls
         * @return fetchedMovies
         */
        @Override
        protected List<Movie> doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String movieServiceResults = null;

            try {
                movieServiceResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                JSONObject jsonResult = new JSONObject(movieServiceResults);
                JsonHandler jsonHandler = new JsonHandler();
                JSONArray results = jsonResult.getJSONArray("results");
                if(fetchedMovies == null){
                    fetchedMovies = jsonHandler.readResultsArray(results);
                    getPosterImage(fetchedMovies);
                }else{
                    List<Movie> newResult = jsonHandler.readResultsArray(results);
                    getPosterImage(newResult);
                    fetchedMovies.addAll(newResult);
                }
                return fetchedMovies;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * This method get movie posters. Sometimes the api returns null for poster path, so this
         * method shows poster image not available on the application.
         * @param fetchedMovies
         * @return updatedFetchedMovies
         */
        public List<Movie> getPosterImage(List<Movie> fetchedMovies){
            URL imageUrl = null;
            Bitmap movieImageBitmap = null;
            String posterPath=null;
            Resources res = getResources();
            for (Movie fetchedMovie:fetchedMovies) {
                posterPath = fetchedMovie.getPosterPath();
                if(posterPath == "" || posterPath == null){
                    InputStream imageNotAvailable = res.openRawResource(R.raw.not_available);
                    movieImageBitmap = BitmapFactory.decodeStream(imageNotAvailable);
                    RoundedBitmapDrawable roundedImage = RoundedBitmapDrawableFactory.create(res, movieImageBitmap);
                    roundedImage.setCornerRadius(Math.max(movieImageBitmap.getWidth(), movieImageBitmap.getHeight()) / 0.1f);
                    fetchedMovie.setImage(roundedImage);
                }else{
                    imageUrl = NetworkUtils.buildImgUrl(posterPath);
                    movieImageBitmap = NetworkUtils.getImageResponseFromHttpUrl(imageUrl);
                    RoundedBitmapDrawable roundedImage = RoundedBitmapDrawableFactory.create(res, movieImageBitmap);
                    roundedImage.setCornerRadius(Math.max(movieImageBitmap.getWidth(), movieImageBitmap.getHeight()) / 0.1f);
                    fetchedMovie.setImage(roundedImage);
                }
            }
            return fetchedMovies;
        }

        /**
         * This method set the data to the adapter and it changes the visibility of an error message
         * that could appear during the application execution.
         * @param movieServiceResults
         */
        @Override
        protected void onPostExecute(List<Movie> movieServiceResults) {
            if(movieServiceResults != null && !movieServiceResults.equals("")){
                showMovieView();

                mMovieServiceAdapter.setMovieData(movieServiceResults);
            }
        }
    }

    /**
     * This method inflate application menu items.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    /**
     * This method sees if the item selected is the item Search. If the search item was clicked
     * this method will search through the movies list for the most probable result, this is for
     * preparing the application for future releases.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_search){
//            mMovieServiceAdapter.setMovieData(null);
//            loadMovieData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method calls the AsyncTask and set the visibility of possible error messages on the
     * screen.
     */
    private void loadMovieData() {
        showMovieView();
        URL movieServiceURL = NetworkUtils.buildUrl(String.valueOf(this.lastPageRequested));
        new UpComingMovieServiceTask().execute(movieServiceURL);
    }

    private void showMovieView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


}
