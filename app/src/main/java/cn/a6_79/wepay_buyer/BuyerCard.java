package cn.a6_79.wepay_buyer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.a6_79.wepay_buyer.NetPack.ThreadTask;

public class BuyerCard extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_card);

        API.init();

        ThreadTask task = API.getCard(getCardListener);
        if (task != null)
            task.execute();


        ImageView mReturnImage = (ImageView)findViewById(R.id.return_personal_info);
        mReturnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                intent.putExtra("default_id", R.id.personal_info);
                startActivity(intent);
            }
        });

        ImageView mAddCard = (ImageView)findViewById(R.id.add_card);
        mAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddBuyerCard.class));
            }
        });


    }


    ResponseListener getCardListener = new ResponseListener() {
        @Override
        public void callback(String response) throws JSONException {
            JSONObject jsonObject = API.ResponseShow(getApplicationContext(), response);
            JSONArray body = jsonObject.getJSONArray("body");
            if (body != null) {
//                for (int i = 0; i < body.length(); i++) {
//                    JSONObject cardInfo = body.getJSONObject(i);
//                    String card = cardInfo.getString("card");
//                    String cardID = cardInfo.getString("card_id");
//                    Boolean isDefault = cardInfo.getBoolean("is_default");
//
//
//
//                    LinearLayout mCardList = (LinearLayout)findViewById(R.id.card_list);
//
//                    LinearLayout.LayoutParams linearLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, );
//                    mCardList.setOrientation(LinearLayout.VERTICAL);
//                    mCardList.addView();
//
//
//                    CardView cardCard = new CardView(getApplicationContext());
//                    CardView.LayoutParams cardCardParam = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
//
//
//                    TableLayout aCardInfo = new TableLayout(getApplicationContext());
//                    TableRow firstLine = new TableRow(getApplicationContext());
//
//
//
//
//
//                }
            }
        }
    };
}
