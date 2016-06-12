package app.com.example.android.popularmovies;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import app.com.example.android.popularmovies.data.MovieContract;
import butterknife.ButterKnife;
import butterknife.InjectView;


public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    static final String DETAIL_URI = "URI";

    private static final int FORECAST_LOADER = 0;

    private static final String[] FORECAST_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_URL,
    };
    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_URL = 1;
    static final int COL_MOVIE_NAME = 2;

    String url_string;
    String img_string;
    @InjectView(R.id.original_tit)
    TextView original_t;
    @InjectView(R.id.user_rat)
    TextView user_r;
    @InjectView(R.id.release_dat)
    TextView release_d;
    @InjectView(R.id.plot_syn)
    TextView plot_s;
    @InjectView(R.id.movie_poster)
    ImageView movie_p;
    @InjectView(R.id.trailer_button)
    ImageButton trailer_but;
    @InjectView(R.id.favourite)
    Button fav_button;

    private String url;
    private String image;
    private Uri mUri;


    //@InjectView(R.id.youtube_view) YouTubePlayerView youTubePlayerView;
    //@InjectView(R.id.videoView1) VideoView videoView;
    //@InjectView(R.id.trailer_video) VideoView trailer_vid;
    YouTubePlayerSupportFragment youTubePlayerView;

    public DetailActivityFragment() {
    }

    public void showYouTubeImageButton() {
        trailer_but.setImageResource(R.drawable.trailer);
        fav_button.setBackgroundColor(Color.BLACK);
        fav_button.setTextColor(Color.WHITE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.inject(this, rootView);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailActivityFragment.DETAIL_URI);
        }

       // Intent intent = getActivity().getIntent();
        //if (intent == null) {
        // } else {
        // if(intent!=null){
        //   Log.v("hai","intent not null");
        // }

        //Bundle extras = intent.getExtras();
        if (null != mUri) {


            url_string = "popular";
            img_string = mUri.toString();
            Log.v("hai", "hereeeeeeee" + img_string);
            image = img_string;
            url=url_string;

            final Intent intent1 = new Intent(getActivity(), YouTubePlayerActivity.class);


            final LoaderManager.LoaderCallbacks myCallBack = this;
            Cursor cursor = getContext().getContentResolver().query(
                    MovieContract.MovieEntry.CONTENT_URI,
                    new String[]{MovieContract.MovieEntry._ID},
                    MovieContract.MovieEntry.COLUMN_MOVIE_URL + " = ?",
                    new String[]{image},
                    null);
            if (cursor.moveToFirst()) {
                fav_button.setText("NOT FAVOURITE");
            } else {
                fav_button.setText("MARK AS FAVOURITE");
            }
            cursor.close();

            fav_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fav_button.setBackgroundColor(Color.GRAY);
                    fav_button.setTextColor(Color.BLACK);

                    Cursor cursor = getContext().getContentResolver().query(
                            MovieContract.MovieEntry.CONTENT_URI,
                            new String[]{MovieContract.MovieEntry._ID},
                            MovieContract.MovieEntry.COLUMN_MOVIE_URL + " = ?",
                            new String[]{image},
                            null);
                    if (cursor.moveToFirst()) {
                        getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                                MovieContract.MovieEntry.COLUMN_MOVIE_URL + " = ?",
                                new String[]{image});
                        fav_button.setText("MARK AS FAVOURITE");
                    } else {
                        ContentValues testValues = new ContentValues();
                        Log.v("hai", "db" + image);
                        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_URL, image);
                        Uri uri = getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, testValues);
                        fav_button.setText("NO FAVOURITE");
                    }
                    cursor.close();
                    //       getLoaderManager().initLoader(FORECAST_LOADER, null, myCallBack);


                }
            });
            trailer_but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(intent1);
                }
            });
            DFetchData fetchData = new DFetchData(this);

            fetchData.execute(url_string);

        }
//        ContentValues testValues =new ContentValues();
//        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_URL, "ccc");
//        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE, "ABCDEF");
//
//        Uri uri=getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,testValues);
//
//
//        Log.v("hai", "inserted");
//
//        Log.v("hai","Success adding into database");


        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v("hai", "loader");


        Uri weatherForLocationUri = MovieContract.MovieEntry.CONTENT_URI;
        Log.v("hai", String.valueOf(weatherForLocationUri));
        //     Loader<Cursor> cursor= (Loader<Cursor>) new CursorLoader(getActivity(),
        return new CursorLoader(getActivity(),
                weatherForLocationUri,
                new String[]{MovieContract.MovieEntry._ID},
                MovieContract.MovieEntry.COLUMN_MOVIE_URL + " = ?",
                new String[]{"ddd"},
                null);
        //  if(!cursor.moveToFirst()){
        //      Log.v("hai","yes no data");
        //   }else{
        //       Log.v("hai","yes data");
        //   }
        //   return (Loader<Cursor>) cursor;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        //   while (!data.isAfterLast()){
        //     Log.v("hai",data.getString(0));
        //   Log.v("hai",data.getString(1));
        // Log.v("hai",data.getString(2));
        // data.moveToNext();
        // }
        if (data.moveToFirst()) {
            Log.v("hai", "hey");
        } else {
            ContentValues testValues = new ContentValues();
            testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_URL, image);
            Uri uri = getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, testValues);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.v("hai", "end");

    }
}



