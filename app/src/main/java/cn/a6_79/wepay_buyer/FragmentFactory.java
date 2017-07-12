package cn.a6_79.wepay_buyer;

import android.app.Fragment;


public class FragmentFactory {
    public static Fragment getBottomTabInstanceByIndex(int index) {
        Fragment fragment = null;
        switch (index) {
            case R.id.good_category:
                fragment = new BuyerButtonFragment();
                break;
            case R.id.personal_info:
                fragment = new PersonalInfoFragment();
                break;
        }
        return fragment;
    }

    public static Fragment getOrderTabInstanceByIndex(int index) {
        Fragment fragment = null;
        switch (index) {
            case R.id.have_delivered:
                fragment = new HaveDeliveredOrder();
                break;
            case R.id.have_ordered:
                fragment = new HaveOrderedOrder();
                break;
        }
        return fragment;
    }
}
