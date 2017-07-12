package cn.a6_79.wepay_buyer;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Window;
import android.widget.RadioGroup;

import cn.a6_79.wepay_buyer.fragment.FragmentFactory;

public class OrderActivity extends Activity implements RadioGroup.OnCheckedChangeListener {
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.buyer_order);

        fragmentManager = getFragmentManager();
        RadioGroup radioGroup = findViewById(R.id.order_tab);
        radioGroup.setOnCheckedChangeListener(this);

        findViewById(R.id.have_delivered).performClick();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        android.app.Fragment fragment = FragmentFactory.getOrderTabInstanceByIndex(checkedId);
        transaction.replace(R.id.order_content, fragment);
        transaction.commit();
    }
}
