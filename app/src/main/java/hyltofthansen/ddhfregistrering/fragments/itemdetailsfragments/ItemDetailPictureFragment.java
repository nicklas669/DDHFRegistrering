package hyltofthansen.ddhfregistrering.fragments.itemdetailsfragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.Singleton;
import hyltofthansen.ddhfregistrering.activities.ShowImageActivity;
import hyltofthansen.ddhfregistrering.adapters.ItemDetailsImageAdapter;
import hyltofthansen.ddhfregistrering.dao.GetItemPicturesForGridViewTask;

/**
 * ItemDetailPictureFragment shows gridview of an item's pictures
 */
public class ItemDetailPictureFragment extends Fragment {

    private static final String TAG ="ItemDetailsPicture" ;
    private ArrayList<Bitmap> pictures;
    private View root;
    private ProgressBar pb;
    private ItemDetailsImageAdapter itemDetailsImageAdapter;
    private int itemid;

    @Override
    public void onResume() {
        Singleton.getInstance().fetchItemGridPictures(getContext(),
                itemid, pictures, itemDetailsImageAdapter, pb);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fr_itemdetails_picture, container, false);
        pb = (ProgressBar) root.findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);
        super.onCreate(savedInstanceState);

        GridView gridview = (GridView) root.findViewById(R.id.gridview);
        Log.d(TAG, "createView");
        pictures = new ArrayList<Bitmap>();

        itemDetailsImageAdapter = new ItemDetailsImageAdapter(getActivity(),pictures);

        itemid = Singleton.getInstance().getItemDetailsID();
        Log.d(TAG, String.valueOf(itemid) );

        //Fetch pictures for itemid

        Singleton.getInstance().fetchItemGridPictures(getContext(),
                itemid,pictures, itemDetailsImageAdapter, pb);

        //Set adapter
        gridview.setAdapter(itemDetailsImageAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
//                Toast.makeText(getActivity(), "Position " + position, Toast.LENGTH_SHORT).show();
                Intent showImageAct  = new Intent(getActivity(), ShowImageActivity.class);
                Log.d(TAG, String.valueOf(itemid) + " img" + String.valueOf(position));
                showImageAct.putExtra("clickedimage", position);
                showImageAct.putExtra("itemid", itemid);
                startActivity(showImageAct);
            }
        });

        return root;


    }

//    public int getItemIdFromExtra() {
//        Bundle bundle = getActivity().getIntent().getExtras();
//        return bundle.getInt("itemid");
//    }
}
