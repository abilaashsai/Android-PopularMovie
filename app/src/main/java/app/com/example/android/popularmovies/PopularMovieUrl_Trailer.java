package app.com.example.android.popularmovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by GSA on 17-04-2016.
 */
public interface PopularMovieUrl_Trailer {
        @GET("videos?")
        Call<TrailerFetch> getUser(

                @Query("api_key") String apikey);
}
