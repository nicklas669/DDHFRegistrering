package hyltofthansen.ddhfregistrering.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.singletons.Sing_NewItemData;

/**
 * Created by hylle on 20-01-2016.
 */
public class Adapter_NewItemPicture extends BaseAdapter {
    private static final String TAG = "Adapter_NewItemPicture";
    private Context mContext;
    private int imgWidth;
    private ArrayList<String> pictures;
    private BitmapFactory.Options bmOptions;

    public Adapter_NewItemPicture(Context c) {
        this.pictures = Sing_NewItemData.getInstance().getPhotoFileList();
        mContext = c;
         bmOptions = new BitmapFactory.Options();

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
//                File image = new File(pictures.get(position), "hello.jpg");
                Bitmap bitmap = BitmapFactory.decodeFile(pictures.get(position), bmOptions);
                bitmap = Bitmap.createScaledBitmap(bitmap, 300,300,false);
                imageView.setImageBitmap(bitmap);
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