package hyltofthansen.ddhfregistrering.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.activities.ItemDetailsActivity;

/**
 * Created by hylle on 13-01-2016.
 */
public class ImageAdapter extends BaseAdapter {
    private static final String TAG = "ImageAdapter";
    private Context mContext;
    private int imgWidth;
    private ArrayList<Bitmap> pictures;

    public ImageAdapter(Context c, ArrayList<Bitmap> pictures) {
        //Get itemid from extra data which was set when user clicked in SearchItemFragment
//        itemId = getItemIdFromExtra();
//        Log.d(TAG, "item id er " + itemId);
        this.pictures = pictures;
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView");
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            int imgWidth = getImgWidthFromDisplaySize();
            imageView.setLayoutParams(new GridView.LayoutParams(imgWidth, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

//        imageView.setImageResource(mThumbIds[position]);

        //Check if picture actually contains data before displaying it
        if((pictures.get(position).getHeight() + pictures.get(position).getWidth()) > 0) {
            imageView.setImageBitmap(pictures.get(position));
        } else {
            imageView.setImageResource(R.drawable.noimage);
        }

        //Get scaled picture array from itemid
        return imageView;
    }

    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    // references to our images
    private Integer[] mThumbIds = {
//            R.drawable.sample_0, R.drawable.sample_1,
//            R.drawable.sample_2, R.drawable.sample_3,
//            R.drawable.sample_4, R.drawable.sample_5,
//            R.drawable.sample_6, R.drawable.sample_7,
    };

    /**
     * Sets the imgwidth to half of screensize - so two pictures for each row on gridview.
     * @return int half of screen size
     */
    public int getImgWidthFromDisplaySize() {
        WindowManager wm  = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        imgWidth = size.x / 2;
        return imgWidth;
    }

    public int getItemIdFromExtra() {
        Intent intent = ((Activity) mContext).getIntent();
        int itemid;
        Bundle extras = intent.getExtras();
        itemid= extras.getInt("itemid");
        return itemid;
    }
}