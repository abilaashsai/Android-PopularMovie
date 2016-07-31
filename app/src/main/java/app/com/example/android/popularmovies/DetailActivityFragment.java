package app.com.example.android.popularmovies;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    String url_string;
    String img_string;
    TextView original_t;
    TextView user_r;
    TextView release_d;
    TextView plot_s;
    ImageView movie_p;
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_detail, container, false);
        original_t=((TextView) rootView.findViewById(R.id.original_tit));
        user_r=(TextView) rootView.findViewById(R.id.user_rat);
        release_d=(TextView) rootView.findViewById(R.id.release_dat);
        plot_s=(TextView) rootView.findViewById(R.id.plot_syn);
        movie_p=(ImageView) rootView.findViewById(R.id.movie_poster);


        Intent intent=getActivity().getIntent();
        Bundle extras = intent.getExtras();
         url_string = extras.getString("EXTRA_URL");
        img_string = extras.getString("EXTRA_IMG");

        // Instance of ImageAdapter Class
        FetchData fetchData=new FetchData();
        fetchData.execute(url_string);
        if(intent!=null){


        }
        return rootView;
    }
    public class FetchData extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchData.class.getSimpleName();
        String original_title;
        String plot_synopsis;
        String user_rating;
        String release_date;
        String icon;
        public String jsonconverter(String forecastJsonStr){

            String resultStrs="";
            try {
                JSONObject forecastJson = new JSONObject(forecastJsonStr);
                JSONArray weatherArray = forecastJson.getJSONArray("results");
                for (int i = 0; i < weatherArray.length(); i++) {
                    JSONObject dayForecast = weatherArray.getJSONObject(i);
                    String temperatureObject = dayForecast.getString("poster_path");
                    if(img_string.contains(temperatureObject)){
                         original_title=dayForecast.getString("original_title");
                         plot_synopsis=dayForecast.getString("overview");
                         user_rating=dayForecast.getString("vote_average");
                        release_date=dayForecast.getString("release_date");

                        icon=dayForecast.getString("backdrop_path");




                        break;

                    }


                }
return resultStrs;

            }
            catch (JSONException e){
                Log.e(LOG_TAG, "Error ", e);


            }
            return null;



        }
        @Override
        protected Void doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJson = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                String baseUrl = "http://api.themoviedb.org/3/movie/"+params[0];

                String apiKey = "?api_key=" + BuildConfig.OPEN_MOVIE_DB_API_KEY;
                URL url = new URL(baseUrl.concat(apiKey));


                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJson = buffer.toString();
                jsonconverter(forecastJson);


            }
            catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            }

            finally {


                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                original_t.setText(original_title);
                plot_s.setText(plot_synopsis);
                user_r.setText(user_rating);
                release_d.setText(release_date);
                Picasso.with(getContext())
                        .load("http://image.tmdb.org/t/p/w92/" + icon)
                        .into(movie_p);
            }catch (Exception e){
                Log.e(LOG_TAG,"Error on post execute",e);

            }
        }
    }
    }



