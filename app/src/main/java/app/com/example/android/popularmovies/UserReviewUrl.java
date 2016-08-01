package app.com.example.android.popularmovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserReviewUrl {
    @GET("reviews?")
    Call<UserReview> getUser(
            @Query("api_key") String apikey);
}