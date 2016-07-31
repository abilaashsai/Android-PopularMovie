package app.com.example.android.popularmovies;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import app.com.example.android.popularmovies.data.MovieContract;
import retrofit2.Call;

public class MFetchFavourite extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {
    private MainActivityFragment mainActivityFragment;
    private final String LOG_TAG = MFetchRelease.class.getSimpleName();

    public MFetchFavourite(MainActivityFragment mainActivityFragment) {
        this.mainActivityFragment = mainActivityFragment;
    }

    @Override
    protected ArrayList<String> doInBackground(ArrayList<String>... params) {
        try {
            ArrayList<String> mStringArray = new ArrayList<>();
            for (String image :
                    params[0]) {
                String imageUrl = image;

                mStringArray.add(imageUrl);
            }

            return mStringArray;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        super.onPostExecute(result);
        try {
            if (result != null) {
                mainActivityFragment.movieUpdate.clear();
            }

            for (String strs : result) {
                mainActivityFragment.movieUpdate.add(strs);
            }
            mainActivityFragment.imageAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error on post execute", e);
        }


    }
}
