package app.com.example.android.popularmovies;

import android.os.AsyncTask;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by GSA on 20-04-2016.
 */
public class MFetchRelease extends AsyncTask<String, Void, String[]> {
    private MainActivityFragment mainActivityFragment;
    private final String LOG_TAG = MFetchRelease.class.getSimpleName();
    Call<JsonWork> call;

    public MFetchRelease(MainActivityFragment mainActivityFragment) {
        this.mainActivityFragment = mainActivityFragment;
    }


    @Override
    protected String[] doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.

        // Will contain the raw JSON response as a string.

        try {


            String baseUrl = "http://api.themoviedb.org/3/movie/" + params[0] + "/";

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            PopularMovieUrl service = retrofit.create(PopularMovieUrl.class);
            call = service.getUser(BuildConfig.OPEN_MOVIE_DB_API_KEY);
            Response<JsonWork> response = call.execute();
            String[] mStringArray = new String[response.body().getResults().size()];
            for (int i = 0; i < response.body().getResults().size(); i++) {
                String imageUrl = "http://image.tmdb.org/t/p/w500/" + response.body().getResults().get(i).getPosterPath();
                mStringArray[i] = imageUrl;
            }

            // Did our content observer get called?  Students:  If this fails, your insert location
            // isn't calling getContext().getContentResolver().notifyChange(uri, null);


            return mStringArray;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            call.cancel();
        }


    }

    @Override
    protected void onPostExecute(String[] result) {
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
