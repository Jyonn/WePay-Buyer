package cn.a6_79.wepay_buyer.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.a6_79.wepay_buyer.R;


public class HaveDeliveredOrder extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.have_delivered_order, null);
        return view;
    }
}