package hyltofthansen.ddhfregistrering.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.Singleton;
import hyltofthansen.ddhfregistrering.dto.ItemDTO;

/**
 * Custom Adapter for at muliggøre søgningen med filter() i ArrayAdapter.
 * Fra: http://stackoverflow.com/questions/14118309/how-to-use-search-functionality-in-custom-list-view-in-android/14119383#14119383
 *
 **/
public class CustomArrayAdapter extends ArrayAdapter<ItemDTO> implements Filterable {

    private ArrayList<ItemDTO> mOriginalValues;
    private ArrayList<ItemDTO> mDisplayedValues;
    private static final String TAG = "CustomArrayAdapter";
    private Context context;


    public CustomArrayAdapter(Context context, int layout, int layout_textview, ArrayList<ItemDTO> mItemArrayList) {
        super(context, layout, layout_textview, mItemArrayList);
        this.context = context;
        this.mDisplayedValues = mItemArrayList;
        this.mOriginalValues = mItemArrayList;
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public ItemDTO getItem(int position) {
        return mDisplayedValues.get(position);
    }

    @Override
    public long getItemId(int position) { return mDisplayedValues.get(position).getItemid(); }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.d(TAG, "getView pos: " + position + ", genstandid: " + mDisplayedValues.get(position).getItemid());
        View view = super.getView(position, convertView, parent);
        TextView itemHeadline = (TextView) view.findViewById(R.id.search_tvheadline);
        itemHeadline.setText(mDisplayedValues.get(position).getItemheadline());

        TextView itemID = (TextView) view.findViewById(R.id.search_itemid);
        itemID.setText(String.valueOf(mDisplayedValues.get(position).getItemid()));

        ImageView itemImage = (ImageView) view.findViewById(R.id.search_iv);
//        if (mDisplayedValues.get(position).getImageArraySize() == 0) {

        ItemDTO itemShown = mDisplayedValues.get(position);

        if(!(itemShown.getDefaultImageURL().equals("null") && itemShown.gettingPicture() == false)) {
            if(itemShown.getDefaultImage() == null && itemShown.gettingPicture() == false) {
                itemShown.setGettingPicture(true);
                Log.d(TAG, itemShown.getDefaultImageURL());
                Log.d(TAG, "Henter billed");
                Singleton.getInstance().fetchDefaultImage(itemShown, this);
                itemImage.setImageResource(R.drawable.default_image_wait);
            }
            if(itemShown.isDefaultImageDownloaded()) {
                Log.d(TAG, "Sætter billed på " + itemShown.toString());
                itemImage.setImageBitmap(itemShown.getDefaultImage());
            }
        } else {
            itemImage.setImageResource(R.drawable.noimage);
        }
        return view;
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<ItemDTO>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<ItemDTO> FilteredArrList = new ArrayList<ItemDTO>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<ItemDTO>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    Log.d(TAG, "Constraint:" + constraint.toString().toLowerCase());
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).getItemheadline();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(mOriginalValues.get(i));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }

    public void updateItemsList(ArrayList<ItemDTO> items) {
        //Log.d(TAG, "itemlist bliver opdateret!");
        this.mDisplayedValues = items;
        this.mOriginalValues = items;
        //notifyDataSetChanged();
        this.notifyDataSetChanged();
    }
}
