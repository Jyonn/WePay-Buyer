package cn.a6_79.wepay_buyer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.a6_79.wepay_buyer.NetPack.HttpThreadTask;
import cn.a6_79.wepay_buyer.NetPack.ImageTaskResponse;
import cn.a6_79.wepay_buyer.NetPack.ImageThreadTask;
import cn.a6_79.wepay_buyer.NetPack.OnAsyncImageTaskListener;

public class ChooseGood extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        initInterface();
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initInterface();
//    }

    private void initInterface() {
        setContentView(R.layout.choose_good);
        API.buttonID = 0;
        API.number = 1;
        ImageView mReturnImage = (ImageView)findViewById(R.id.return_buyer_button);
        mReturnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        HttpThreadTask task = API.getCategory(getCategoryListener);
        if (task != null)
            task.execute();
    }

    ResponseListener getCategoryListener = new ResponseListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void callback(String response) throws JSONException {
            JSONObject jsonObject = API.ResponseShow(getApplicationContext(), response);
            JSONArray body = jsonObject.getJSONArray("body");
            final RadioGroup orderRadios = (RadioGroup) findViewById(R.id.order_radio);
            orderRadios.removeAllViews();
            if (body != null) {
                for (int i = 0; i < body.length(); i++) {
                    JSONObject categoryInfo = body.getJSONObject(i);
                    String categoryName = categoryInfo.getString("category_name");
                    final int categoryID = categoryInfo.getInt("category_id");
                    final RadioButton categoryButton = new RadioButton(getApplicationContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2.0f);
                    categoryButton.setLayoutParams(params);
                    categoryButton.setId(categoryID);
                    categoryButton.setText(categoryName);
                    categoryButton.setGravity(Gravity.CENTER);
                    categoryButton.setButtonDrawable(null);
                    categoryButton.setPadding(60, 30, 60, 30);
                    if (i == 0) {
                        categoryButton.setChecked(true);
                        HttpThreadTask task = API.getGoodInCategory(categoryID, getGoodInCategoryListener);
                        if (task != null)
                            task.execute();
                    }
                    for (int j = 0; j < orderRadios.getChildCount(); j++) {
                        RadioButton tmpButton = (RadioButton)orderRadios.getChildAt(j);
                        if (!tmpButton.isChecked()) {
                            tmpButton.setTextColor(getResources().getColor(R.color.tab_off, getTheme()));
                        }
                        else {
                            tmpButton.setTextColor(getResources().getColor(R.color.colorPrimary, getTheme()));
                        }
                    }
                    orderRadios.addView(categoryButton);

                    categoryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            categoryButton.setChecked(true);
                            for (int j = 0; j < orderRadios.getChildCount(); j++) {
                                RadioButton tmpButton = (RadioButton)orderRadios.getChildAt(j);
                                if (!tmpButton.isChecked()) {
                                    tmpButton.setTextColor(getResources().getColor(R.color.tab_off, getTheme()));
                                }
                                else {
                                    tmpButton.setTextColor(getResources().getColor(R.color.colorPrimary, getTheme()));
                                }
                            }

                            HttpThreadTask task = API.getGoodInCategory(categoryID, getGoodInCategoryListener);
                            if (task != null)
                                task.execute();
                        }
                    });
                }
            }
        }
    };

    ResponseListener getGoodInCategoryListener = new ResponseListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
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

        mGoodImg = findViewById(R.id.good_card_img);
        new ImageThreadTask(pic, new OnAsyncImageTaskListener() {
            @Override
            public void callback(ImageTaskResponse imageTaskResponse) {
                Bitmap bitmap = imageTaskResponse.getBitmap();
                mGoodImg.setImageBitmap(bitmap);
            }
        }).execute();

        mGoodName = findViewById(R.id.good_card_name);
        mGoodName.setText(goodName);
        mGoodPrice = findViewById(R.id.good_card_price);
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