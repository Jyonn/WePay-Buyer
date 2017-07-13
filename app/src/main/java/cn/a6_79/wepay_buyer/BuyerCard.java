package cn.a6_79.wepay_buyer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.a6_79.wepay_buyer.NetPack.ThreadTask;

public class BuyerCard extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        initInterface();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInterface();
    }

    private void initInterface() {
        setContentView(R.layout.buyer_card);
        API.init();
        ThreadTask task = API.getCard(getCardListener);
        if (task != null)
            task.execute();


        ImageView mReturnImage = (ImageView)findViewById(R.id.return_personal_info);
        mReturnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
            LinearLayout cardList= (LinearLayout) findViewById(R.id.card_list);
            cardList.removeAllViews();
            if (body != null) {
                for (int i = 0; i < body.length(); i++) {
                    JSONObject cardInfo = body.getJSONObject(i);
                    String card = cardInfo.getString("card");
                    String cardID = cardInfo.getString("card_id");
                    Boolean isDefault = cardInfo.getBoolean("is_default");

                    CardCard aCard = new CardCard(card, cardID, isDefault, getApplicationContext(), BuyerCard.this, deleteCardListener, setDefaultCardListener);
                    cardList.addView(aCard);
                }
            }
        }
    };

    ResponseListener deleteCardListener = new ResponseListener() {
        @Override
        public void callback(String response) throws JSONException {
            JSONObject jsonObject = API.ResponseShow(getApplicationContext(), response);
            setContentView(R.layout.buyer_card);
            initInterface();
        }
    };

    ResponseListener setDefaultCardListener = new ResponseListener() {
        @Override
        public void callback(String response) throws JSONException {
            JSONObject jsonObject = API.ResponseShow(getApplicationContext(), response);
            setContentView(R.layout.buyer_card);
            initInterface();
        }
    };

}


class CardCard extends CardView {
    private TextView mDeleteButton;
    private CheckBox mSetDefault;

    public CardCard (String card, final String cardID, final Boolean isDefault, final Context context, final Activity activity,
                     final ResponseListener setDefaultCardListener, final ResponseListener deleteCardListener) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.card_card, this);

        TextView mBankName = findViewById(R.id.bank_name);
        mBankName.setText("XX银行");
        TextView mCardNumber = findViewById(R.id.card_number);
        mCardNumber.setText(card);
        mSetDefault = findViewById(R.id.set_default);
        mSetDefault.setChecked(isDefault);


        mSetDefault.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                API.currentDeleteCard = this;
                if (!isDefault) {
                    ThreadTask task = API.setDefaultCard(cardID, setDefaultCardListener);
                    if (task != null)
                        task.execute();
                }
                else {
                    mSetDefault.setChecked(isDefault);
                    Toast.makeText(context, "不能取消默认银行卡", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDeleteButton = findViewById(R.id.delete_card);
        mDeleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity).setMessage("确定删除该银行卡?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ThreadTask task = API.deleteCard(cardID, deleteCardListener);
                                if (task != null)
                                    task.execute();
                            }
                        }).setNegativeButton("取消", null);
                builder.show();
            }
        });
    }
}

