package cn.a6_79.wepay_buyer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.a6_79.wepay_buyer.NetPack.ThreadTask;


public class AddBuyerCard extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_buyer_card);

        ImageView mReturnImage = (ImageView)findViewById(R.id.return_buyer_card);
        mReturnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Button mAddCard = (Button)findViewById(R.id.save_buyer_card);
        mAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText mAddCardNumber = (EditText)findViewById(R.id.add_card_number);
                String addCardNumber = mAddCardNumber.getText().toString();
                CheckBox mIsDefaultCard = (CheckBox)findViewById(R.id.is_default_card);
                ThreadTask task;
                if (mIsDefaultCard.isChecked()) {
                     task = API.addCard(addCardNumber, 1, addCardListener);
                }
                else {
                    task = API.addCard(addCardNumber, 0, addCardListener);
                }
                if (task != null)
                    task.execute();
            }
        });

    }

    ResponseListener addCardListener = new ResponseListener() {
        @Override
        public void callback(String response) throws JSONException {
            JSONObject jsonObject = API.ResponseShow(getApplicationContext(), response);
            finish();
        }
    };
}
