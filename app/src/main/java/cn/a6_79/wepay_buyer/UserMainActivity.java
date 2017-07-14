package cn.a6_79.wepay_buyer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;

public class UserMainActivity extends Activity implements RadioGroup.OnCheckedChangeListener {
    private FragmentManager fragmentManager;
//    @Override
//    protected void onResume() {
//        super.onResume();
//        initInterface();
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInterface();
    }

    private void initInterface () {
        setContentView(R.layout.activity_usermain);
        fragmentManager = getFragmentManager();
        RadioGroup radioGroup = findViewById(R.id.bottom_tab);
        radioGroup.setOnCheckedChangeListener(this);

        Intent intent = getIntent();
        int defaultID = intent.getIntExtra("default_id", 1);
        if (defaultID != 1) {
            findViewById(defaultID).performClick();
        }
        else {
            findViewById(R.id.button_list).performClick();
        }
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        android.app.Fragment fragment = FragmentFactory.getBottomTabInstanceByIndex(checkedId);
        transaction.replace(R.id.bottom_content, fragment);
        transaction.commit();
    }
}