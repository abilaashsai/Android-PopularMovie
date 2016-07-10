package app.com.example.android.popularmovies;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by GSA on 17-04-2016.
 */
public class DFetchData extends AsyncTask<String, Void, Void> {

    private DetailActivityFragment detailActivityFragment;
    private final String LOG_TAG = DFetchData.class.getSimpleName();
    public static String original_title;
    public static String plot_synopsis;
    public static String user_rating;
    public static String release_date;
    public static String icon;
    public static String trailer_l;
    public static byte[] bytes;
    Call<JsonWork> call;
    Call<TrailerFetch> trailerFetchCall;
    static String mForecast;

    public DFetchData(DetailActivityFragment detailActivityFragment) {
        this.detailActivityFragment = detailActivityFragment;

    }


    @Override
    protected Void doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.

        // Will contain the raw JSON response as a string.

        try {

            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
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
                if (detailActivityFragment.img_string.contains(response.body().getResults().get(i).getPosterPath())) {
                    original_title = response.body().getResults().get(i).getOriginalTitle();
                    plot_synopsis = response.body().getResults().get(i).getOverview();
                    String user_rat = response.body().getResults().get(i).getVoteAverage();
                    String trailer_id = String.valueOf(response.body().getResults().get(i).getId());
                    String trailer_baseUrl = "http://api.themoviedb.org/3/movie/" + trailer_id + "/";
                    Retrofit trailer_retrofit = new Retrofit.Builder()
                            .baseUrl(trailer_baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    PopularMovieUrl_Trailer trailer_service = trailer_retrofit.create(PopularMovieUrl_Trailer.class);

                    trailerFetchCall = trailer_service.getUser(BuildConfig.OPEN_MOVIE_DB_API_KEY);

                    Response<TrailerFetch> trailer_fetch = trailerFetchCall.execute();

                    trailer_l = trailer_fetch.body().getResults().get(0).getKey();
                    user_rating = user_rat;

                    release_date = response.body().getResults().get(i).getReleaseDate();

                    icon = response.body().getResults().get(i).getBackdropPath();
                    URL url=new URL("http://image.tmdb.org/t/p/w500/" + icon);
                    //byte[] logoImage = getLogoImage("http://image.tmdb.org/t/p/w500/" + icon);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 0, stream);
                    byte[] byteArray = stream.toByteArray();

                    bytes=stream.toByteArray();
                    break;
                }

            }
            return null;

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);

        } finally {

            call.cancel();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        try {

            Config.YOUTUBE_VIDEO_CODE = trailer_l;
            detailActivityFragment.showYouTubeImageButton();
            detailActivityFragment.original_t.setText(original_title);
            detailActivityFragment.plot_s.setText(plot_synopsis);
            detailActivityFragment.user_r.setText(user_rating);
            detailActivityFragment.release_d.setText(release_date);
            mForecast = String.format("%s - %s - %s", original_title, user_rating, release_date);
            // detailActivityFragment.trailer_vid.setVideoPath(trailer_l);

                  Picasso.with(detailActivityFragment.getContext())
                    .load("http://image.tmdb.org/t/p/w185/" + icon)
                    .into(detailActivityFragment.movie_p);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error on post execute", e);

        }
    }


//    private byte[] getLogoImage(String url){
//        try {
//            URL imageUrl = new URL(url);
//            URLConnection ucon = imageUrl.openConnection();
//
//            InputStream is = ucon.getInputStream();
//            BufferedInputStream bis = new BufferedInputStream(is);
//            //ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(500);
//           // ByteArrayBuffer
//           // ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(500);
//
//            ByteArrayBuffer baf = new ByteArrayBuffer(500);
//            int current = 0;
//            while ((current = bis.read()) != -1) {
//                baf.append((byte) current);
//            }
//
//            return baf.toByteArray();
//        } catch (Exception e) {
//            Log.d("ImageManager", "Error: " + e.toString());
//        }
//        return null;
//    }
}
