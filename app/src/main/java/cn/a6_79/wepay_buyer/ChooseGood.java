package cn.a6_79.wepay_buyer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.a6_79.wepay_buyer.NetPack.ThreadTask;

public class ChooseGood extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInterface();
    }

    private void initInterface() {
        setContentView(R.layout.choose_good);
        API.init();

        ThreadTask task = API.getCategory(getCategoryListener);
        if (task != null)
            task.execute();
    }

    ResponseListener getCategoryListener = new ResponseListener() {
        @Override
        public void callback(String response) throws JSONException {
            JSONObject jsonObject = API.ResponseShow(getApplicationContext(), response);
            JSONArray body = jsonObject.getJSONArray("body");
            RadioGroup orderRadios = (RadioGroup) findViewById(R.id.order_radio);
            orderRadios.removeAllViews();
            if (body != null) {
                for (int i = 0; i < body.length(); i++) {
                    JSONObject categoryInfo = body.getJSONObject(i);
                    String categoryName = categoryInfo.getString("category_name");
                    final int categoryID = categoryInfo.getInt("category_id");
                    RadioButton categoryButton = (RadioButton) LayoutInflater.from(getApplicationContext()).inflate(R.layout.order_radio, null);
                    categoryButton.setId(categoryID);
                    categoryButton.setText(categoryName);
                    orderRadios.addView(categoryButton);

                    categoryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ThreadTask task = API.getGoodInCategory(categoryID, getGoodInCategoryListener);
                            if (task != null)
                                task.execute();
                        }
                    });
                }
            }
        }
    };

    ResponseListener getGoodInCategoryListener = new ResponseListener() {
        @Override
        public void callback(String response) throws JSONException {
            JSONObject jsonObject = API.ResponseShow(getApplicationContext(), response);
            JSONArray body = jsonObject.getJSONArray("body");
            LinearLayout goodCardList = (LinearLayout)findViewById(R.id.good_card);
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

                    GoodCard goodCard = new GoodCard(goodID, brand, goodName, store, price, pic, description, categoryName, categoryID, getApplicationContext(), ChooseGood.this);
                    goodCardList.addView(goodCard);
                }
            }
        }
    };

}


class GoodCard extends CardView {
    private ImageView mGoodImg;
    private TextView mGoodName;
    private TextView mGoodPrice;

    private LinearLayout mGoodListCard;
    public GoodCard(final int goodID, final String brand, final String goodName, final int store, final double price,
                    final String pic, final String description, final String categoryName, final int categoryID,
                    Context context, final Activity activity) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.good_card, this);

        mGoodImg.findViewById(R.id.good_img);
        mGoodName.findViewById(R.id.good_name);
        mGoodName.setText(goodName);
        mGoodPrice.findViewById(R.id.good_price);
        mGoodPrice.setText("¥" + price);

        mGoodListCard = findViewById(R.id.good_list_card);
        mGoodListCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity.getApplicationContext(), GoodDetail.class);
                intent.putExtra("good_id", goodID);
                intent.putExtra("brand", brand);
                intent.putExtra("good_name", goodName);
                intent.putExtra("store", store);
                intent.putExtra("price", price);
                intent.putExtra("pic", pic);
                intent.putExtra("description", description);
                intent.putExtra("category_name", categoryName);
                intent.putExtra("category_id", categoryID);
                activity.startActivity(intent);
            }
        });


    }
}