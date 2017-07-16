package cn.a6_79.wepay_buyer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class BuyerOrder extends Activity implements RadioGroup.OnCheckedChangeListener {
    private FragmentManager fragmentManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_order_view);

        ImageView mReturnImage = (ImageView) findViewById(R.id.return_personal_info);
        mReturnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fragmentManager = getFragmentManager();
        RadioGroup radioGroup = findViewById(R.id.order_tab);
        radioGroup.setOnCheckedChangeListener(this);

        findViewById(R.id.have_delivered).performClick();
        RadioButton mHaveDelivered = findViewById(R.id.have_delivered);
        RadioButton mHaveOrdered = findViewById(R.id.have_ordered);
        mHaveDelivered.setTextColor(getResources().getColor(R.color.colorPrimary, getTheme()));
        mHaveOrdered.setTextColor(getResources().getColor(R.color.menu, getTheme()));

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
        RadioButton mHaveDelivered = findViewById(R.id.have_delivered);
        RadioButton mHaveOrdered = findViewById(R.id.have_ordered);
        if (mHaveDelivered.isChecked()) {
            mHaveDelivered.setTextColor(getResources().getColor(R.color.colorPrimary, getTheme()));
            mHaveOrdered.setTextColor(getResources().getColor(R.color.menu, getTheme()));
        }
        else {
            mHaveDelivered.setTextColor(getResources().getColor(R.color.menu, getTheme()));
            mHaveOrdered.setTextColor(getResources().getColor(R.color.colorPrimary, getTheme()));
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        android.app.Fragment fragment = FragmentFactory.getOrderTabInstanceByIndex(checkedId);
        transaction.replace(R.id.order_content, fragment);
        transaction.commit();
    }

}
