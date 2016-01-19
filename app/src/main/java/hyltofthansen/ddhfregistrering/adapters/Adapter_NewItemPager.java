package hyltofthansen.ddhfregistrering.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentManager;
import hyltofthansen.ddhfregistrering.fragments.newitemfragments.Frag_NewItemInfo;
import hyltofthansen.ddhfregistrering.fragments.newitemfragments.Frag_NewItemPictures;
import hyltofthansen.ddhfregistrering.fragments.newitemfragments.Frag_NewItemSound;

public class Adapter_NewItemPager extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public Adapter_NewItemPager(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Frag_NewItemInfo tab1 = new Frag_NewItemInfo();
                return tab1;
            case 1:
                Frag_NewItemPictures tab2 = new Frag_NewItemPictures();
                return tab2;
            case 2:
                Frag_NewItemSound tab3 = new Frag_NewItemSound();
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