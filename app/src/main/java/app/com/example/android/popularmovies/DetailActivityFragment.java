package app.com.example.android.popularmovies;


import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import app.com.example.android.popularmovies.data.MovieContract;
import butterknife.ButterKnife;
import butterknife.InjectView;


public class DetailActivityFragment extends Fragment {
    static final String DETAIL_URI = "URI";

    private static final int FORECAST_LOADER = 0;
    private static final String MOVIE_SHARE_HASHTAG = " #MovieApp";


    private static final String[] FORECAST_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER,
    };
    static final int ID = 0;
    static final int TITLE = 1;
    static final int urll = 2;
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
    @InjectView(R.id.user_review)
    TextView user_review;

    @InjectView(R.id.favourite)
    ImageView fav_button;

    @InjectView(R.id.user_review_title)
    TextView user_review_title;

    private String url;
    private String image;
    private Uri mUri;
    private ShareActionProvider mShareActionProvider;

    YouTubePlayerSupportFragment youTubePlayerView;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragment, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
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
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.inject(this, rootView);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailActivityFragment.DETAIL_URI);
        }

        if (mUri != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            url_string = prefs.getString(getString(R.string.pref_sort_order_key),
                    getString(R.string.pref_sort_order_default));
            img_string = mUri.toString();
            image = img_string;
            url = url_string;

            final Intent intent1 = new Intent(getActivity(), YouTubePlayerActivity.class);

            Cursor cursor = getContext().getContentResolver().query(
                    MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    MovieContract.MovieEntry.COLUMN_MOVIE_URL + " = ?",
                    new String[]{String.valueOf(mUri)},
                    null);
            if (cursor.moveToFirst()) {
                fav_button.setImageResource(R.drawable.star_on);
                showYouTubeImageButton();
                original_t.setText(cursor.getString(TITLE));
                plot_s.setText(cursor.getString(SYNOPSIS));
                user_r.setText(cursor.getString(USER_RATING));
                release_d.setText(cursor.getString(RELEASE_DATE));
                Config.YOUTUBE_VIDEO_CODE = cursor.getString(TRAILER);
                DFetchData.mForecast = String.format("%s - %s", cursor.getString(TITLE), cursor.getString(TRAILER));
            } else {
                fav_button.setImageResource(R.drawable.star_off);
                DFetchData fetchData = new DFetchData(this);
                fetchData.execute(url_string);
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
                    if (cursor.moveToFirst()) {
                        getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                                MovieContract.MovieEntry.COLUMN_MOVIE_URL + " = ?",
                                new String[]{String.valueOf(mUri)});
                        fav_button.setImageResource(R.drawable.star_off);
                    } else {

                        ContentValues testValues = new ContentValues();
                        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, DFetchData.original_title);
                        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_URL, String.valueOf(mUri));
                        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, DFetchData.bytes);
                        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS, DFetchData.plot_synopsis);
                        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_USER_RATING, DFetchData.user_rating);
                        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, DFetchData.release_date);
                        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TRAILER, Config.YOUTUBE_VIDEO_CODE);
                        Uri uri = getContext().
                                getContentResolver().
                                insert(MovieContract.MovieEntry.CONTENT_URI, testValues);
                        fav_button.setImageResource(R.drawable.star_on);
                    }
                    cursor.close();
                }
            });

            trailer_but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(intent1);
                }
            });
        }
        return rootView;
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, DFetchData.mForecast + MOVIE_SHARE_HASHTAG);

        return shareIntent;
    }
}



