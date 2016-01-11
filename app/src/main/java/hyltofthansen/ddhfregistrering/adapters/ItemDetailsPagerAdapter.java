package hyltofthansen.ddhfregistrering.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import hyltofthansen.ddhfregistrering.fragments.itemdetailsfragments.ItemDetailInfoFragment;
import hyltofthansen.ddhfregistrering.fragments.itemdetailsfragments.ItemDetailPictureFragment;
import hyltofthansen.ddhfregistrering.fragments.itemdetailsfragments.ItemDetailSoundFragment;
import hyltofthansen.ddhfregistrering.fragments.newitemfragments.NewItemInfoFragment;
import hyltofthansen.ddhfregistrering.fragments.newitemfragments.NewItemPicturesFragment;
import hyltofthansen.ddhfregistrering.fragments.newitemfragments.NewItemSoundFragment;

public class ItemDetailsPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ItemDetailsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ItemDetailInfoFragment tab1 = new ItemDetailInfoFragment();
                return tab1;
            case 1:
                ItemDetailPictureFragment tab2 = new ItemDetailPictureFragment();
                return tab2;
            case 2:
                ItemDetailSoundFragment tab3 = new ItemDetailSoundFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}