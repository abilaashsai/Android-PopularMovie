package app.com.example.android.popularmovies;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    String url_string;
    String img_string;
    @InjectView(R.id.original_tit) TextView original_t;
    @InjectView(R.id.user_rat) TextView user_r;
    @InjectView(R.id.release_dat) TextView release_d;
    @InjectView(R.id.plot_syn) TextView plot_s;
    @InjectView(R.id.movie_poster) ImageView movie_p;
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.inject(this,rootView);

        Intent intent=getActivity().getIntent();
        Bundle extras = intent.getExtras();
         url_string = extras.getString("EXTRA_URL");
        img_string = extras.getString("EXTRA_IMG");

        // Instance of ImageAdapter Class
        DFetchData fetchData=new DFetchData(this);
        fetchData.execute(url_string);
        if(intent!=null){


        }
        return rootView;
    }

}



