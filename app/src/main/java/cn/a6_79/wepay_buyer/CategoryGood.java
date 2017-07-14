package cn.a6_79.wepay_buyer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.a6_79.wepay_buyer.NetPack.HttpThreadTask;


public class CategoryGood extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_good);
        initInterface();
    }
    private void initInterface() {
        Intent intent = getIntent();
        int categoryID = intent.getIntExtra("category_id", 0);
        String categoryName = intent.getStringExtra("category_name");
        int buttonID = intent.getIntExtra("button_id", 0);
        API.buttonID = buttonID;
        int number = intent.getIntExtra("number", API.number);
        API.number = number;

        TextView mCategoryTitleName = (TextView) findViewById(R.id.category_title_name);
        mCategoryTitleName.setText(categoryName);

        ImageView mReturnImage = (ImageView)findViewById(R.id.return_buyer_button);
        mReturnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        HttpThreadTask task = API.getGoodInCategory(categoryID, getGoodInCategoryListener);
        if (task != null)
            task.execute();

    }

    ResponseListener getGoodInCategoryListener = new ResponseListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void callback(String response) throws JSONException {
            JSONObject jsonObject = API.ResponseShow(getApplicationContext(), response);
            JSONArray body = jsonObject.getJSONArray("body");
            LinearLayout goodCardList = (LinearLayout)findViewById(R.id.good_list);
            goodCardList.removeAllViews();
            if (body != null) {
                for (int i = 0; i < body.length(); i++) {
                    JSONObject categoryGoods = body.getJSONObject(i);
                    int goodID = categoryGoods.getInt("good_id");
                    String brand = categoryGoods.getString("brand");
                    String goodName = categoryGoods.getString("good_name");
                    int store = categoryGoods.getInt("store");
                    double price = categoryGoods.getDouble("price");
                    String pic = categoryGoods.getString("pic");
                    String description = categoryGoods.getString("description");
                    String categoryName = categoryGoods.getString("category_name");
                    int categoryID = categoryGoods.getInt("category_id");

                    GoodCard goodCard = new GoodCard(goodID, brand, goodName, store, price, pic, description, categoryName, categoryID, getApplicationContext(), CategoryGood.this);
                    goodCard.setCardBackgroundColor(0xffffffff);
                    goodCardList.addView(goodCard);
                }
            }
        }
    };
}



