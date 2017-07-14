package cn.a6_79.wepay_buyer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import cn.a6_79.wepay_buyer.NetPack.HttpThreadTask;

public class EditBuyerAddress extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_buyer_address);

        Intent intent = getIntent();
        String recipientName = intent.getStringExtra("recipient_name");
        String address = intent.getStringExtra("address");
        EditText mRecipientName = (EditText)findViewById(R.id.edit_recipient_name);
        if (!recipientName.equals("请添加收货人")){
            mRecipientName.setText(recipientName);
        }
        EditText mAddress = (EditText)findViewById(R.id.edit_address);
        if (!address.equals("请添加收货地址")) {
            mAddress.setText(address);
        }


        ImageView mReturnImage = (ImageView)findViewById(R.id.return_buyer_address);
        mReturnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button mSaveBuyerAddress = (Button)findViewById(R.id.save_buyer_address);
        mSaveBuyerAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText mAfterRecipientName = (EditText)findViewById(R.id.edit_recipient_name);
                EditText mAfterAddress = (EditText)findViewById(R.id.edit_address);
                final String afterEditRecipientName = mAfterRecipientName.getText().toString();
                final String afterEditAddress = mAfterAddress.getText().toString();
                HttpThreadTask task = API.editAddress(afterEditRecipientName, afterEditAddress, saveAddressListener);
                if (task != null)
                    task.execute();
            }
        });
    }

    ResponseListener saveAddressListener = new ResponseListener() {
        @Override
        public void callback(String response) throws JSONException {
            JSONObject jsonObject = API.ResponseShow(getApplicationContext(), response);
            finish();
        }
    };
}
