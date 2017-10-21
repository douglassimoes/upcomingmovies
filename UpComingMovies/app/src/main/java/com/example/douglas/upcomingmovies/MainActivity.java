package com.example.douglas.upcomingmovies;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.example.douglas.upcomingmovies.model.Movie;
import com.example.douglas.upcomingmovies.utilities.JsonHandler;
import com.example.douglas.upcomingmovies.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private MovieServiceAdapter mMovieServiceAdapter;
    private int lastPageRequested=1;
    private final int MAX_PAGES=10;
    private List<Movie> fetchedMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movieservice);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMovieServiceAdapter = new MovieServiceAdapter();

        mMovieServiceAdapter.setOnBottomReachedListener(new MovieServiceAdapter.OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {
                if(lastPageRequested < MAX_PAGES) {
                    lastPageRequested+=1;
                    loadMovieData();
                }
            }
        });

        mRecyclerView.setAdapter(mMovieServiceAdapter);

        loadMovieData();
    }

    public class UpComingMovieServiceTask extends AsyncTask<URL,Void,List<Movie>>{
        @Override
        protected List<Movie> doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String movieServiceResults = null;

            try {
                movieServiceResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                JSONObject jsonResult = new JSONObject(movieServiceResults);
                JsonHandler jsonHandler = new JsonHandler();
                if(fetchedMovies == null){
                    fetchedMovies = jsonHandler.readResultsArray(jsonResult.getJSONArray("results"));
                    getPosterImage(fetchedMovies);
                }else{
                    List<Movie> newResult = jsonHandler.readResultsArray(jsonResult.getJSONArray("results"));
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

        public List<Movie> getPosterImage(List<Movie> fetchedMovies){
            URL imageUrl = null;
            Bitmap movieImageBitmap = null;
            for (Movie fetchedMovie:fetchedMovies) {
                imageUrl = NetworkUtils.buildImgUrl(fetchedMovie.getPosterPath());
                movieImageBitmap = NetworkUtils.getImageResponseFromHttpUrl(imageUrl);
                fetchedMovie.setImage(movieImageBitmap);
            }
            return fetchedMovies;
        }
        @Override
        protected void onPostExecute(List<Movie> movieServiceResults) {
            if(movieServiceResults != null && !movieServiceResults.equals("")){
                showMovieView();

                mMovieServiceAdapter.setMovieData(movieServiceResults);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_search){
            mMovieServiceAdapter.setMovieData(null);
            loadMovieData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
