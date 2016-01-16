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
public class ItemDetailsImageAdapter extends BaseAdapter {
    private static final String TAG = "ItemDetailsImageAdapter";
    private Context mContext;
    private int imgWidth;
    private ArrayList<Bitmap> pictures;

    public ItemDetailsImageAdapter(Context c, ArrayList<Bitmap> pictures) {
        //Get itemid from extra data which was set when user clicked in SearchItemFragment
//        itemId = getItemIdFromExtra();
//        Log.d(TAG, "item id er " + itemId);
        this.pictures = pictures;
        mContext = c;
    }

    public int getCount() {
        return pictures.size();
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

        imageView.setImageBitmap(pictures.get(position));

        //Check if picture actually contains data before displaying it
        if (pictures != null && (pictures.size() > 0)) {
            Log.d(TAG, String.valueOf(pictures.size() + " pictures size"));
            try {
                if ((pictures.get(position).getHeight() + pictures.get(position).getWidth()) > 0) {
                    imageView.setImageBitmap(pictures.get(position));
                } else {
                    imageView.setImageResource(R.drawable.noimage);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        //Get scaled picture array from itemid
        return imageView;
    }

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

}