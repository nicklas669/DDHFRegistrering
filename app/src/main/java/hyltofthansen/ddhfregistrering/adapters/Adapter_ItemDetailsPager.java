package hyltofthansen.ddhfregistrering.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import hyltofthansen.ddhfregistrering.fragments.itemdetailsfragments.Frag_ItemDetailInfo;
import hyltofthansen.ddhfregistrering.fragments.itemdetailsfragments.Frag_ItemDetailPicture;
import hyltofthansen.ddhfregistrering.fragments.itemdetailsfragments.Frag_ItemDetailSound;

public class Adapter_ItemDetailsPager extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public Adapter_ItemDetailsPager(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Frag_ItemDetailInfo tab1 = new Frag_ItemDetailInfo();
                return tab1;
            case 1:
                Frag_ItemDetailPicture tab2 = new Frag_ItemDetailPicture();
                return tab2;
            case 2:
                Frag_ItemDetailSound tab3 = new Frag_ItemDetailSound();
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