package app.com.example.android.popularmovies;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    private static final String MOVIE_SHARE_HASHTAG = " #MovieApp";


    private static final String[] FORECAST_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER,
    };
    static final int ID = 0;
    static final int urll = 1;
    static final int TITLE = 2;
    static final int POSTER = 3;
    static final int SYNOPSIS = 4;
    static final int USER_RATING = 5;
    static final int RELEASE_DATE = 6;
    static final int TRAILER = 7;


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
    ImageView fav_button;

    private String url;
    private String image;
    private Uri mUri;
    private ShareActionProvider mShareActionProvider;


    //@InjectView(R.id.youtube_view) YouTubePlayerView youTubePlayerView;
    //@InjectView(R.id.videoView1) VideoView videoView;
    //@InjectView(R.id.trailer_video) VideoView trailer_vid;
    YouTubePlayerSupportFragment youTubePlayerView;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (DFetchData.mForecast != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());

        }
    }

    public void showYouTubeImageButton() {
        trailer_but.setImageResource(R.drawable.trailer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("hai", "hai");
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
        Log.v("hai", "haiiii");

        if (mUri != null) {
            url_string = "popular";
            img_string = mUri.toString();
            image = img_string;
            url = url_string;

            final Intent intent1 = new Intent(getActivity(), YouTubePlayerActivity.class);


            final LoaderManager.LoaderCallbacks myCallBack = this;
            Cursor cursor = getContext().getContentResolver().query(
                    MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    MovieContract.MovieEntry.COLUMN_MOVIE_URL + " = ?",
                    new String[]{String.valueOf(mUri)},
                    null);
            Log.v("hai", "hai");
            if (cursor.moveToFirst()) {
                fav_button.setImageResource(R.drawable.star_on);
                showYouTubeImageButton();
                original_t.setText(cursor.getString(1));
                plot_s.setText(cursor.getString(4));
                user_r.setText(cursor.getString(5));
                release_d.setText(cursor.getString(6));
                DFetchData.mForecast = String.format("%s - %s - %s", cursor.getString(1), cursor.getString(5), cursor.getString(6));
                Config.YOUTUBE_VIDEO_CODE = cursor.getString(7);
            } else {
                fav_button.setImageResource(R.drawable.star_off);
            }
            cursor.close();

            fav_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Cursor cursor = getContext().getContentResolver().query(
                            MovieContract.MovieEntry.CONTENT_URI,
                            new String[]{MovieContract.MovieEntry._ID},
                            MovieContract.MovieEntry.COLUMN_MOVIE_URL + " = ?",
                            new String[]{String.valueOf(mUri)},
                            null);
                    Log.v("hai", "db1");
                    if (cursor.moveToFirst()) {
                        getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                                MovieContract.MovieEntry.COLUMN_MOVIE_URL + " = ?",
                                new String[]{String.valueOf(mUri)});
                        fav_button.setImageResource(R.drawable.star_off);
                        Log.v("hai", "db2");

                    } else {

                        ContentValues testValues = new ContentValues();

                        //    title, poster,synopsis,user_rating,release_date
                        //    original_title,icon,plot_synopsis, user_rating,release_data
                        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, DFetchData.original_title);
                        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_URL, String.valueOf(mUri));
                        Log.v("hai", "hai" + mUri);
                        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, DFetchData.bytes);
                        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS, DFetchData.plot_synopsis);
                        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_USER_RATING, DFetchData.user_rating);
                        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, DFetchData.release_date);
                        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TRAILER, Config.YOUTUBE_VIDEO_CODE);
                        Uri uri = getContext().
                                getContentResolver().
                                insert(MovieContract.MovieEntry.CONTENT_URI, testValues);
                        //  getLoaderManager().initLoader(FORECAST_LOADER, null, myCallBack);
                        //   ContentValues testValues = new ContentValues();
                        //   testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, image);
                        //      Uri uri = getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, testValues);
                        fav_button.setImageResource(R.drawable.star_on);
                        Log.v("hai", "db3");

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

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, DFetchData.mForecast + MOVIE_SHARE_HASHTAG);

        return shareIntent;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        Uri weatherForLocationUri = MovieContract.MovieEntry.CONTENT_URI;
        //     Loader<Cursor> cursor= (Loader<Cursor>) new CursorLoader(getActivity(),
        return new CursorLoader(getActivity(),
                weatherForLocationUri,
                new String[]{MovieContract.MovieEntry._ID},
                MovieContract.MovieEntry.COLUMN_MOVIE_POSTER + " = ?",
                new String[]{},
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
        } else {
//            ContentValues testValues = new ContentValues();
//
//            //    title, poster,synopsis,user_rating,release_date
//            //    original_title,icon,plot_synopsis, user_rating,release_data
//            testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, DFetchData.original_title);
//            testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, DFetchData.bytes);
//            testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS, DFetchData.plot_synopsis);
//            testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_USER_RATING, DFetchData.user_rating);
//            testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, DFetchData.release_date);
//            testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TRAILER, Config.YOUTUBE_VIDEO_CODE);
//            Uri uri = getContext().
//                    getContentResolver().
//                    insert(MovieContract.MovieEntry.CONTENT_URI, testValues);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}



