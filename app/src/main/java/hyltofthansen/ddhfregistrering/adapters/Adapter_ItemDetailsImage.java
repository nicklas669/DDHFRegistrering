package hyltofthansen.ddhfregistrering.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import java.util.ArrayList;

/**
 * Created by hylle on 13-01-2016.
 */
public class Adapter_ItemDetailsImage extends BaseAdapter {
    private static final String TAG = "Adpt_ItemDetailsImage";
    private Context mContext;
    private int imgWidth;
    private ArrayList<Bitmap> pictures;

    public Adapter_ItemDetailsImage(Context c, ArrayList<Bitmap> pictures) {
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
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            int imgWidth = getImgWidthFromDisplaySize();
            imageView.setLayoutParams(new GridView.LayoutParams(imgWidth, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        if (pictures != null && (pictures.size() > 0)) {
            try {
                    if ((pictures.get(position).getHeight() + pictures.get(position).getWidth()) > 0) {
                        imageView.setImageBitmap(pictures.get(position));
                        //Log.d(TAG, String.valueOf(position));
                        //Log.d(TAG, String.valueOf(pictures.size()));
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