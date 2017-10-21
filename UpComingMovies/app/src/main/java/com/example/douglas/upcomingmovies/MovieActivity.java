package com.example.douglas.upcomingmovies;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.widget.TextView;

import com.example.douglas.upcomingmovies.utilities.NetworkUtils;

import java.io.InputStream;
import java.net.URL;

public class MovieActivity extends AppCompatActivity {

    private TextView mMovieMessageDisplay;
    private AppCompatImageView mMovieServiceImageView;

    /**
     * This activity is started by an Intent when the user click on any view. The MainActivity
     * passes two parameters for this activity: the movie full description and the poster path
     * image.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        mMovieMessageDisplay = (TextView) findViewById(R.id.tv_movie_message_display);

        mMovieServiceImageView = (AppCompatImageView) findViewById(R.id.iv_movie_intent);


        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            String textEntered = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            mMovieMessageDisplay.setText(textEntered);
        }
        /**
         * It was passed the movie poster URI through an Intent parameter called imageUri. With
         * that parameter, we can then get the poster using an AsyncTask.
         */
        if (intentThatStartedThisActivity.hasExtra("imageUri")){
            String imagePosterURI = intentThatStartedThisActivity.getStringExtra("imageUri");

            new ImageServiceTask().execute(imagePosterURI);
        }
    }

    /**
     * This class is an AsysncTask that is used just for download the image. This helps
     * the application because it will avoid freeze the app for network stuff.
     */
    private class ImageServiceTask extends AsyncTask<String,Void,Bitmap>{

            @Override
            protected Bitmap doInBackground(String... params) {
                String posterPath = params[0];
                URL imageUrl = NetworkUtils.buildImgUrl(posterPath);
                Resources res = getResources();
                Bitmap movieImageBitmap= null;
                if(posterPath == "" || posterPath == null){
                    InputStream imageNotAvailable = res.openRawResource(R.raw.not_available);
                    movieImageBitmap = BitmapFactory.decodeStream(imageNotAvailable);
                    return movieImageBitmap;
                }else{
                    movieImageBitmap = NetworkUtils.getImageResponseFromHttpUrl(imageUrl);
                    return movieImageBitmap;
                }
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if(bitmap != null){
                    mMovieServiceImageView.setImageBitmap(bitmap);
                }
            }
        }
 }

