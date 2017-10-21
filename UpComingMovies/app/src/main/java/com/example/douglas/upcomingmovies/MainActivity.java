package com.example.douglas.upcomingmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.example.douglas.upcomingmovies.utilities.NetworkUtils;

import org.w3c.dom.Text;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private MovieServiceAdapter mMovieServiceAdapter;

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

        mRecyclerView.setAdapter(mMovieServiceAdapter);

        loadMovieData();
    }

    private void makeMovieServiceQuery(){
        URL movieServiceURL = NetworkUtils.buildUrl();
        new UpComingMovieServiceTask().execute(movieServiceURL);

    }

    public class UpComingMovieServiceTask extends AsyncTask<URL,Void,String>{
        @Override
        protected String doInBackground(URL... urls) {
            URL moviesUrl = urls[0];
            String movieServiceResults = null;

            movieServiceResults = NetworkUtils.getResponseFromHttpUrl(moviesUrl);
            return movieServiceResults;
        }

        @Override
        protected void onPostExecute(String movieServiceResults) {
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
        URL movieServiceURL = NetworkUtils.buildUrl();
        new UpComingMovieServiceTask().execute(movieServiceURL);
    }

    private void showMovieView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


}
