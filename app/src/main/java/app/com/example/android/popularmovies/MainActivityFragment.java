package app.com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

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

        MFetchRelease fetchRelease = new MFetchRelease(this);
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
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pass = (String) imageAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle extras = new Bundle();
                extras.putString("EXTRA_IMG", pass);
                extras.putString("EXTRA_URL", join);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        return rootview;
    }


}