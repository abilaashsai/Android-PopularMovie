package app.com.example.android.popularmovies;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.com.example.android.popularmovies.data.MovieContract;

public class MainActivityFragment extends Fragment {
    ImageAdapter imageAdapter;
    private GridView mGridView;
    public int mPosition = GridView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    List<String> movieUpdate;
    TextView sort_order_det;
    String join;

    public interface Callback {
        public void onItemSelected(Uri selected);
    }

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.popularmov, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovie();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateMovie() {
        MFetchFavourite mFetchFavourite = new MFetchFavourite(this);

        MFetchRelease fetchRelease = new MFetchRelease(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        join = prefs.getString(getString(R.string.pref_sort_order_key),
                getString(R.string.pref_sort_order_default));
        if (join.equals("top_rated")) {
            sort_order_det.setText("Sorted by Highest Rated Movies");
            fetchRelease.execute(join);
        } else if (join.equals("popular")) {
            sort_order_det.setText("Sorted by Most Popular Movies");
            fetchRelease.execute(join);
        } else if (join.equals("favourite")) {
            sort_order_det.setText("Favourite movies");
            ArrayList<String> image = new ArrayList<>();
            Cursor cursor = getContext().getContentResolver().query(
                    MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
            cursor.moveToFirst();
            cursor.moveToNext();
            while (!cursor.isAfterLast()) {
                image.add(cursor.getString(2));
                cursor.moveToNext();
            }
            cursor.close();
            mFetchFavourite.execute(image);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovie();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
        sort_order_det = (TextView) rootview.findViewById(R.id.sort_order_detail);
        movieUpdate = new ArrayList<String>(new ArrayList<String>());
        imageAdapter = new ImageAdapter(getContext(), (ArrayList<String>) movieUpdate);
        mGridView = (GridView) rootview.findViewById(R.id.grid_view);
        mGridView.setAdapter(imageAdapter);
        updateMovie();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pass = (String) imageAdapter.getItem(position);
                ((Callback) getActivity())
                        .onItemSelected(Uri.parse(pass)
                        );
                mPosition = position;
            }
        });

        if (mPosition != GridView.INVALID_POSITION) {
            mGridView.smoothScrollToPosition(mPosition);
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        return rootview;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

}