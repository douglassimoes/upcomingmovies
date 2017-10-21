package com.example.douglas.upcomingmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.douglas.upcomingmovies.model.Movie;

import java.util.List;

/**
 * Created by Douglas on 21/10/2017.
 */

public class MovieServiceAdapter extends RecyclerView.Adapter<MovieServiceAdapter.MovieServiceViewHolder> {

    private List<Movie> mMovieData;
    private OnBottomReachedListener onBottomReachedListener;

    @Override
    public MovieServiceAdapter.MovieServiceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movieservice_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieServiceAdapter.MovieServiceViewHolder movieViewHolder, int position) {
        Movie dataForThisMovie = mMovieData.get(position);
        movieViewHolder.mMovieServiceTextView.setText(dataForThisMovie.toString());
        movieViewHolder.mMovieServiceImageView.setImageBitmap(dataForThisMovie.getImage());
        if(position == mMovieData.size() -1 ){
            onBottomReachedListener.onBottomReached(position);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.size();
    }

    public class MovieServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public int position;
        private Context context;
        public final TextView mMovieServiceTextView;
        public final AppCompatImageView mMovieServiceImageView;

        public MovieServiceViewHolder(View view) {
            super(view);
            mMovieServiceTextView = (TextView) view.findViewById(R.id.tv_movie_service);
            mMovieServiceImageView = (AppCompatImageView) view.findViewById(R.id.iv_movie);
            mMovieServiceTextView.setOnClickListener(this);
            context = view.getContext();

        }

        @Override
        public void onClick(View v) {
            mMovieData.get(this.getAdapterPosition());
            Class movieActivityClass = MovieActivity.class;
            Intent movieActivityIntent = new Intent(context,movieActivityClass);

            movieActivityIntent.putExtra(Intent.EXTRA_TEXT, mMovieServiceTextView.getText());

            context.startActivity(movieActivityIntent);
        }
    }

    public void setMovieData(List<Movie> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    public interface OnBottomReachedListener {

        void onBottomReached(int position);
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }
}
