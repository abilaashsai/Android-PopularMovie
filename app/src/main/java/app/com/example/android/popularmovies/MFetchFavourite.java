package app.com.example.android.popularmovies;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import retrofit2.Call;

public class MFetchFavourite extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {
    private MainActivityFragment mainActivityFragment;
    private final String LOG_TAG = MFetchRelease.class.getSimpleName();
    Call<JsonWork> call;

    public MFetchFavourite(MainActivityFragment mainActivityFragment) {
        this.mainActivityFragment = mainActivityFragment;
    }

    @Override
    protected ArrayList<String> doInBackground(ArrayList<String>... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.

        // Will contain the raw JSON response as a string.
        try {
            ArrayList<String> mStringArray=new ArrayList<>();
            for (String image:
                 params[0]) {
                String imageUrl = image;
                mStringArray.add(imageUrl);
            }

              return mStringArray;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
    //        call.cancel();
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
