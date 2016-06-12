package app.com.example.android.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GSA on 11-04-2016.
 */
public class ImageAdapter extends BaseAdapter{
    private Context mContext;
    private List aa=new ArrayList();
    // Keep all Images in array


    // Constructor
    public ImageAdapter(Context c, ArrayList<String> mTum){
        mContext = c;
        aa=mTum;

    }





    @Override
    public int getCount() {

        return aa.size();
    }
   @Override
   public Object getItem(int position) {
       return aa.get(position);
   }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
       if(convertView==null){

            view = new ImageView(mContext);
        }
            else {

        view= convertView;
        }

        Picasso.with(mContext)
                .load((String) aa.get(position))
                .placeholder(R.drawable.loading)
                .error(R.drawable.no_data)
                .into((ImageView) view);

        return view;



    }

}



