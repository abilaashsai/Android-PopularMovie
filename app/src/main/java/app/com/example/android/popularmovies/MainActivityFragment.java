package app.com.example.android.popularmovies;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.List;

import app.com.example.android.popularmovies.data.MovieContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
     ImageAdapter imageAdapter;


    private GridView mGridView;
    private int mPosition=11;
    private static final String SELECTED_KEY = "selected_position";

    List<String> movieUpdate;
    TextView sort_order_det;
    String join;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri selected);
    }


    public MainActivityFragment() {
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//
//        if (mPosition != GridView.INVALID_POSITION) {
//            // If we don't need to restart the loader, and there's a desired position to restore
//            // to, do so now.
//            mGridView.smoothScrollToPosition(mPosition);
//        }else{
//            Log.v("hai","else on activity created"+mPosition);
//        }
//        super.onActivityCreated(savedInstanceState);
//    }

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


    private void updateMovie() {
        MFetchFavourite mFetchFavourite = new MFetchFavourite(this);

        MFetchRelease fetchRelease = new MFetchRelease(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        join = prefs.getString(getString(R.string.pref_sort_order_key),
                getString(R.string.pref_sort_order_default));
        if (join.equals("top_rated")) {
            sort_order_det.setText("Sorted by Highest Rated Movies");
            fetchRelease.execute(join);
        } else if (join.equals("popular")) {
            sort_order_det.setText("Sorted by Most Popular Movies");
//            if(!fetchRelease.isCancelled()){
//                fetchRelease.cancel(true);
//            }
            fetchRelease.execute(join);
            mGridView.setSelection(11);
        } else if (join.equals("favourite")) {


//
            sort_order_det.setText("Favourite movies");
            ArrayList<String> image = new ArrayList<>();
            Cursor cursor = getContext().getContentResolver().query(
                    MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                image.add(cursor.getString(1));
                cursor.moveToNext();
            }
            cursor.close();
            mFetchFavourite.execute(image);
            mGridView.setSelection(11);
            mGridView.smoothScrollToPosition(1111);

        }
    }

//    public void executeFetchRelease() {
//        fetchRelease.execute(join);
//    }
//
//    public void executeFetchFavourite() {
//        ArrayList<String> image=new ArrayList<>();
//        Cursor cursor = getContext().getContentResolver().query(
//                MovieContract.MovieEntry.CONTENT_URI,
//                null,
//                null,
//                null,
//                null);
//        cursor.moveToFirst();
//           while (!cursor.isAfterLast()){
//               image.add(cursor.getString(1));
//         cursor.moveToNext();
//         }
//
//        mFetchFavourite.execute(image);
//    }

    @Override
    public void onStart() {

        super.onStart();
        updateMovie();
 }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
//        mPosition=15;
        Log.v("hai","oncreateview"+mPosition);
        sort_order_det = (TextView) rootview.findViewById(R.id.sort_order_detail);

        movieUpdate = new ArrayList<String>(new ArrayList<String>());
        imageAdapter = new ImageAdapter(getContext(), (ArrayList<String>) movieUpdate);
        // Instance of ImageAdapter Class
        mGridView = (GridView) rootview.findViewById(R.id.grid_view);

        mGridView.setAdapter(imageAdapter);

        mGridView.setSelection(15);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pass = (String) imageAdapter.getItem(position);
                // String extra_url = join.equals("favourite") ? "popular" : join;
//                Intent intent = new Intent(getActivity(), DetailActivity.class);
//                Bundle extras = new Bundle();
//                extras.putString("EXTRA_IMG", pass);
//                extras.putString("EXTRA_URL", extra_url);
//                intent.putExtras(extras);
//                startActivity(intent);
    ((Callback) getActivity())
           .onItemSelected(Uri.parse(pass)
            );

              //  mPosition = position;
                //mPosition=mGridView.getFirstVisiblePosition();

                Log.v("hai","position inside"+ mPosition);

            }
        });


//        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
//            // The listview probably hasn't even been populated yet.  Actually perform the
//            // swapout in onLoadFinished.
//            Log.v("hai dude","if");
//            mPosition = savedInstanceState.getInt(SELECTED_KEY);
//            Log.v("hai","savedinstance"+mPosition);
//        }else{
//            Log.v("hai dude","else");
//        }

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            Log.v("hai dude","if");
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
            Log.v("hai","savedinstance"+mPosition);
        }else{
            Log.v("hai dude","else");
        }
       // if (mPosition != GridView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mGridView.smoothScrollToPosition(mPosition);
       //     Log.v("hai","yeah"+mPosition);
       // }else{

         //   Log.v("hai","else on activity created 11"+mPosition);
        //}
        mGridView.setSelection(11);
        Log.v("hai","position inside"+ mPosition);



        return rootview;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != GridView.INVALID_POSITION) {
            Log.v("hai saved","llll"+mPosition);
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

}