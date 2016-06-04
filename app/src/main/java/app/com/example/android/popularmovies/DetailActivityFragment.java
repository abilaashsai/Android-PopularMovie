package app.com.example.android.popularmovies;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;




public class DetailActivityFragment extends Fragment {
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    String url_string;
    String img_string;
    @InjectView(R.id.original_tit)
    TextView original_t;
    @InjectView(R.id.user_rat)
    TextView user_r;
    @InjectView(R.id.release_dat)
    TextView release_d;
    @InjectView(R.id.plot_syn)
    TextView plot_s;
    @InjectView(R.id.movie_poster)
    ImageView movie_p;
    @InjectView(R.id.trailer_link)
    TextView trailer_lin;
    //@InjectView(R.id.youtube_view) YouTubePlayerView youTubePlayerView;
    //@InjectView(R.id.videoView1) VideoView videoView;
    //@InjectView(R.id.trailer_video) VideoView trailer_vid;
    YouTubePlayerSupportFragment youTubePlayerView;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//trailer_lin.setOnClickListener(new View.OnClickListener() {
        //  @Override
        //public void onClick(View v) {
//
        //      Intent intent=new Intent(getActivity(), YouTubePlayerActivity.class);
        //    startActivity(intent);

//    }
//});
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.inject(this, rootView);

        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();
        YouTubePlayerSupportFragment youTubePlayerSupportFragment = YouTubePlayerSupportFragment.newInstance();
        url_string = extras.getString("EXTRA_URL");
        img_string = extras.getString("EXTRA_IMG");
        //    youTubePlayerView = (YouTubePlayerSupportFragment) getActivity().getSupportFragmentManager()
        //          .findFragmentById(R.id.youtube_view);

//
        final Intent intent1=new Intent(getActivity(),YouTubePlayerActivity.class);

        release_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent1);
            }
        });
        // youTubePlayerSupportFragment.initialize(Config.DEVELOPER_KEY,this);
        //  String uriPath2 = "https://www.youtube.com/watch?v=chvki68McG0";
        //  MediaController mediaController=new MediaController(this);

        //  Uri uri2 = Uri.parse(uriPath2);
        //  videoView.setVideoURI(uri2);
        //  videoView.requestFocus();
        //videoView.start();
//
        // Instance of ImageAdapter Class
        DFetchData fetchData = new DFetchData(this);
        fetchData.execute(url_string);
        if (intent != null) {


        }
//        trailer_vid.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v=Hxy8BZGQ5Jo")));
//            }
//        });
        return rootView;
    }


   /* @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
if(!b){

    youTubePlayer.loadVideo(Config.YOUTUBE_VIDEO_CODE);
    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
}
    }*//*

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
if(youTubeInitializationResult.isUserRecoverableError()){

    youTubeInitializationResult.getErrorDialog(this,RECOVERY_DIALOG_REQUEST).show();
*/

//}else
//{
  //  String errorMessage=String.format(getString(R.string.error_player),youTubeInitializationResult.toString());
    //Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();

//}


  //  }

 /*   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
        }
    }
    private YouTubePlayer.Provider getYouTubePlayerProvider(){

        return youTubePlayerView;

    }*/


}



