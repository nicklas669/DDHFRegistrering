package hyltofthansen.ddhfregistrering.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
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
public class ItemDetailsImageAdapter extends BaseAdapter {
    private static final String TAG = "ItemDetailsImageAdapter";
    private Context mContext;
    private int imgWidth;
    private ArrayList<Bitmap> pictures;

    public ItemDetailsImageAdapter(Context c, ArrayList<Bitmap> pictures) {
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
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            int imgWidth = getImgWidthFromDisplaySize();
            imageView.setLayoutParams(new GridView.LayoutParams(imgWidth, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setImageResource(R.drawable.ic_autorenew_black);
        } else {
            imageView = (ImageView) convertView;
        }

        // Do animation start (skulle være en spinner for hvert billed men fik det aldrig helt til at virke)
//        imageView.setImageResource(R.drawable.ic_autorenew_black);
//        Animation rotation = AnimationUtils.loadAnimation(mContext, R.anim.searchlist_refresh_rotate);
//        rotation.setRepeatCount(Animation.INFINITE);
//        imageView.startAnimation(rotation);
        //Check if picture actually contains data before displaying it
        if (pictures != null && (pictures.size() > 0)) {
            try {
                if ((pictures.get(position).getHeight() + pictures.get(position).getWidth()) > 0) {
//                    Log.d(TAG, "picture indeholder noget");
                    imageView.setImageBitmap(pictures.get(position));
//                    rotation.cancel(); rotation.reset();
                } else {
//                    imageView.setImageResource(R.drawable.noimage);
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