package hyltofthansen.ddhfregistrering.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.R;

/**
 * Created by hylle on 13-01-2016.
 */
public class Adapter_ItemDetailsImage extends BaseAdapter {
    private static final String TAG = "Adapter_ItemDetailsImage";
    private final int layoutResourceId;
    private Context mContext;
    private int imgWidth;
    private ArrayList<Bitmap> pictures;

    public Adapter_ItemDetailsImage(Context c, ArrayList<Bitmap> pictures, int layoutResourceId) {
        this.pictures = pictures;
        mContext = c;
        this.layoutResourceId = layoutResourceId;
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

    static class ViewHolder {
        ProgressBar progressBar;
        ImageView imageView;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            // if it's not recycled, initialize some attributes
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.progressBar = (ProgressBar) row.findViewById(R.id.progressBar_grid);
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_imageview);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        if (pictures != null && (pictures.size() > 0)) {
            try {
                if ((pictures.get(position).getHeight() + pictures.get(position).getWidth()) > 0) {
                    holder.imageView.setImageBitmap(pictures.get(position));
                    holder.progressBar.setVisibility(View.INVISIBLE);
                } else {
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        //Get scaled picture array from itemid
        return row;
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