package app.com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.com.example.android.popularmovies.data.MovieContract;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List aa = new ArrayList();
    private List<Bitmap> dd = new ArrayList<>();

    public ImageAdapter(Context c, ArrayList<String> mTum) {
        mContext = c;
        aa = mTum;
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
        if (convertView == null) {

            view = new ImageView(mContext);
        } else {

            view = convertView;
        }
        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_POSTER},
                MovieContract.MovieEntry.COLUMN_MOVIE_URL + " = ?",
                new String[]{String.valueOf((String) aa.get(position))},
                null);

        Bitmap bitmap;
        Drawable alternative;
        if (cursor.moveToFirst()) {
            byte[] bytes = cursor.getBlob(0);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            alternative = new BitmapDrawable(bitmap);
            Picasso.with(mContext)
                    .load((String) aa.get(position))
                    .placeholder(alternative)
                    .error(R.drawable.no_data)
                    .into((ImageView) view);
        } else {
            Picasso.with(mContext)
                    .load((String) aa.get(position))
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.no_data)
                    .into((ImageView) view);
        }
        cursor.close();
        return view;
    }

}



