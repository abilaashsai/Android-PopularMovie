package app.com.example.android.popularmovies;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.picasso.Picasso;

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
    String original_title;
    String plot_synopsis;
    String user_rating;
    String release_date;
    String icon;
    String trailer_l;
    Call<JsonWork> call;
    Call<TrailerFetch> trailerFetchCall;


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
            String baseUrl = "http://api.themoviedb.org/3/movie/" + params[0]+"/";

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
                        PopularMovieUrl service = retrofit.create(PopularMovieUrl.class);

            call = service.getUser(BuildConfig.OPEN_MOVIE_DB_API_KEY);
            Response<JsonWork> response=call.execute();
            String[] mStringArray=new String[response.body().getResults().size()];
            for(int i=0;i<response.body().getResults().size();i++){
                if (detailActivityFragment.img_string.contains(response.body().getResults().get(i).getPosterPath())) {
                    original_title = response.body().getResults().get(i).getOriginalTitle();
                    plot_synopsis = response.body().getResults().get(i).getOverview();
                    String user_rat=response.body().getResults().get(i).getVoteAverage();

                    String trailer_id= String.valueOf(response.body().getResults().get(i).getId());
                    String trailer_baseUrl = "http://api.themoviedb.org/3/movie/"+trailer_id+"/";
                    Retrofit trailer_retrofit = new Retrofit.Builder()
                            .baseUrl(trailer_baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();


                    PopularMovieUrl_Trailer trailer_service = trailer_retrofit.create(PopularMovieUrl_Trailer.class);

                    trailerFetchCall = trailer_service.getUser(BuildConfig.OPEN_MOVIE_DB_API_KEY);

                    Response<TrailerFetch> trailer_fetch=trailerFetchCall.execute();

                    trailer_l= "https://www.youtube.com/watch?v="+trailer_fetch.body().getResults().get(0).getKey();

                    user_rating = user_rat;

                    release_date = response.body().getResults().get(i).getReleaseDate();

                    icon = response.body().getResults().get(i).getBackdropPath();


                    break;
                }

            }
                    return null;

                } catch (Exception e){
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
            detailActivityFragment.trailer_lin.setText(trailer_l);
            detailActivityFragment.original_t.setText(original_title);
            detailActivityFragment.plot_s.setText(plot_synopsis);
            detailActivityFragment.user_r.setText(user_rating);
            detailActivityFragment.release_d.setText(release_date);


           // detailActivityFragment.trailer_vid.setVideoPath(trailer_l);
            Picasso.with(detailActivityFragment.getContext())
                    .load("http://image.tmdb.org/t/p/w92/" + icon)
                    .into(detailActivityFragment.movie_p);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error on post execute", e);

        }
    }
}
