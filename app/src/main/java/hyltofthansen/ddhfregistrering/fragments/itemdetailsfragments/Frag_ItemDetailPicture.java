package hyltofthansen.ddhfregistrering.fragments.itemdetailsfragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.singletons.Sing_AsyncTasks;
import hyltofthansen.ddhfregistrering.activities.Act_ShowImage;
import hyltofthansen.ddhfregistrering.adapters.Adapter_ItemDetailsImage;

/**
 * Frag_ItemDetailPicture shows gridview of an item's pictures
 */
public class Frag_ItemDetailPicture extends Fragment {

    private static final String TAG ="ItemDetailsPicture" ;
    private ArrayList<Bitmap> pictures;
    private View root;
    private ProgressBar pb;
    private Adapter_ItemDetailsImage itemDetailsImageAdapter;
    private int itemid;

    @Override
    public void onResume() {
        Sing_AsyncTasks.getInstance().fetchItemGridPictures(getContext(),
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

        itemDetailsImageAdapter = new Adapter_ItemDetailsImage(getActivity(),pictures);

        itemid = Sing_AsyncTasks.getInstance().getItemDetailsID();
        Log.d(TAG, String.valueOf(itemid) );

        //Fetch pictures for itemid

        Sing_AsyncTasks.getInstance().fetchItemGridPictures(getContext(),
                itemid,pictures, itemDetailsImageAdapter, pb);

        //Set adapter
        gridview.setAdapter(itemDetailsImageAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
//                Toast.makeText(getActivity(), "Position " + position, Toast.LENGTH_SHORT).show();
                Intent showImageAct  = new Intent(getActivity(), Act_ShowImage.class);
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
