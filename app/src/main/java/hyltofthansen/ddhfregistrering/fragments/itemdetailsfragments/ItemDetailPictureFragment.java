package hyltofthansen.ddhfregistrering.fragments.itemdetailsfragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.adapters.ImageAdapter;
import hyltofthansen.ddhfregistrering.dao.GetItemPicturesForGridViewTask;
import hyltofthansen.ddhfregistrering.dto.ItemDTO;

/**
 * ItemDetailPictureFragment shows gridview of an item's pictures
 */
public class ItemDetailPictureFragment extends Fragment {

    private static final String TAG ="ItemDetailsPicture" ;
    private ArrayList<Bitmap> pictures;
    private View root;
    private ArrayList<ItemDTO> items;
    private int itemIdFromExtra;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.itemdetails_picture, container, false);

        super.onCreate(savedInstanceState);

        GridView gridview = (GridView) root.findViewById(R.id.gridview);
        Log.d(TAG, "createView");
        pictures = new ArrayList<Bitmap>();

        ImageAdapter imageAdapter = new ImageAdapter(getActivity(),pictures);

        int itemid = getItemIdFromExtra();

        //Fetch pictures for itemid
        GetItemPicturesForGridViewTask getItemPictures =
                new GetItemPicturesForGridViewTask(getContext(),
                        itemid,pictures, imageAdapter);
        getItemPictures.execute();

        //Set adapter
        gridview.setAdapter(imageAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(), "Position " + position, Toast.LENGTH_SHORT).show();
            }
        });

        return root;


    }

    public int getItemIdFromExtra() {
        Bundle bundle = getActivity().getIntent().getExtras();
        return bundle.getInt("itemid");
    }
}
