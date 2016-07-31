package app.com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    List<String> movieUpdate;
   ImageAdapter imageAdapter;
    TextView sort_order_det;
    String join;
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
    private void updateMovie(){

        FetchRelease fetchRelease = new FetchRelease();
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
         join=prefs.getString(getString(R.string.pref_sort_order_key),
                getString(R.string.pref_sort_order_default));
        if(join.equals("popular")){
       sort_order_det.setText("Sorted by Most Popular Movies");}else{
            sort_order_det.setText("Sorted by Highest Rated Movies");

        }
        fetchRelease.execute(join);


    }

    @Override
    public void onStart() {

        super.onStart();
updateMovie();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);

        sort_order_det=(TextView)rootview.findViewById(R.id.sort_order_detail);

         movieUpdate=new ArrayList<String>(new ArrayList<String>());
        imageAdapter=new ImageAdapter(getContext(),(ArrayList<String>) movieUpdate);
        // Instance of ImageAdapter Class
        GridView gridView = (GridView) rootview.findViewById(R.id.grid_view);

        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pass= (String) imageAdapter.getItem(position);

                Intent intent=new Intent(getActivity(), DetailActivity.class);
                Bundle extras=new Bundle();
                extras.putString("EXTRA_IMG", pass);
                extras.putString("EXTRA_URL",join);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        return rootview;
    }


    public class FetchRelease extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchRelease.class.getSimpleName();
        public String[] jsonconverter(String forecastJsonStr){
            String[] resultStrs;
            try {
                JSONObject forecastJson = new JSONObject(forecastJsonStr);
                JSONArray weatherArray = forecastJson.getJSONArray("results");
                resultStrs = new String[weatherArray.length() + 1];
                for (int i = 0; i < weatherArray.length(); i++) {
                    JSONObject dayForecast = weatherArray.getJSONObject(i);
                    String temperatureObject = dayForecast.getString("poster_path");
                    resultStrs[i] = "http://image.tmdb.org/t/p/w500/"+temperatureObject;
                }
                return resultStrs;

            }
            catch (JSONException e){
                Log.e(LOG_TAG, "Error ", e);


            }

            return null;



        }
        @Override
        protected String[] doInBackground(String... params) {
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

                    return jsonconverter(forecastJson);
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


        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            try {
                if (result != null) {
                    movieUpdate.clear();
                }

                for (String strs : result) {

                    movieUpdate.add(strs);


                }
                imageAdapter.notifyDataSetChanged();
            }catch (Exception e){
                Log.e(LOG_TAG, "Error on post execute",e);

            }


        }
    }

}