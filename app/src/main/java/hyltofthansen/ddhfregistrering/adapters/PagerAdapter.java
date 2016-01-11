package hyltofthansen.ddhfregistrering.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentManager;
import hyltofthansen.ddhfregistrering.fragments.newitemfragments.NewItemInfoFragment;
import hyltofthansen.ddhfregistrering.fragments.newitemfragments.NewItemPicturesFragment;
import hyltofthansen.ddhfregistrering.fragments.newitemfragments.NewItemSoundFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                NewItemInfoFragment tab1 = new NewItemInfoFragment();
                return tab1;
            case 1:
                NewItemPicturesFragment tab2 = new NewItemPicturesFragment();
                return tab2;
            case 2:
                NewItemSoundFragment tab3 = new NewItemSoundFragment();
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