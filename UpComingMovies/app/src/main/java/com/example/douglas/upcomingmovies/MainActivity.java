package com.example.douglas.upcomingmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.douglas.upcomingmovies.utilities.NetworkUtils;

import org.w3c.dom.Text;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mApiResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApiResultTextView = (TextView) findViewById(R.id.tv_api_result);
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
                mApiResultTextView.setText(movieServiceResults);
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
            makeMovieServiceQuery();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
