package cn.a6_79.wepay_buyer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import cn.a6_79.wepay_buyer.NetPack.HttpThreadTask;
import cn.a6_79.wepay_buyer.NetPack.ImageTaskResponse;
import cn.a6_79.wepay_buyer.NetPack.ImageThreadTask;
import cn.a6_79.wepay_buyer.NetPack.OnAsyncImageTaskListener;

public class GoodDetail extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_detail);
        initInterface();
    }
    private void initInterface() {
        Intent intent = getIntent();
        final int goodID = intent.getIntExtra("good_id", 0);
        String brand = intent.getStringExtra("brand");
        String goodName = intent.getStringExtra("good_name");
        final int store = intent.getIntExtra("store", 0);
        double price = intent.getDoubleExtra("price", 0.0);
        String pic = intent.getStringExtra("pic");
        String description = intent.getStringExtra("description");
        String categoryName = intent.getStringExtra("category_name");
        int categoryID = intent.getIntExtra("category_id", 0);

        final ImageView mGoodDetailImg = (ImageView) findViewById(R.id.good_detail_img);
        new ImageThreadTask(pic, new OnAsyncImageTaskListener() {
            @Override
            public void callback(ImageTaskResponse imageTaskResponse) {
                Bitmap bitmap = imageTaskResponse.getBitmap();
                mGoodDetailImg.setImageBitmap(bitmap);
            }
        }).execute();

        TextView mGoodDetailName = (TextView) findViewById(R.id.good_detail_name);
        mGoodDetailName.setText(goodName);
        TextView mGoodDetailDescription = (TextView) findViewById(R.id.good_detail_description);
        mGoodDetailDescription.setText(description);
        TextView mGoodDetailPrice = (TextView) findViewById(R.id.good_detail_price);
        mGoodDetailPrice.setText("¥" + price);
        TextView mGoodDetailStore = (TextView) findViewById(R.id.good_detail_store);
        mGoodDetailStore.setText("库存：" + store + "件");
        TextView mGoodDetailNumber = (TextView) findViewById(R.id.good_detail_number);
        String number = String.valueOf(API.number);
        mGoodDetailNumber.setText(number);

        ImageView mReturnImage = (ImageView)findViewById(R.id.return_last);
        mReturnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView mSubtractionNumber = (TextView) findViewById(R.id.subtraction_number);
        mSubtractionNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView mGoodDetailNumberAfter = (TextView) findViewById(R.id.good_detail_number);
                String numberText = mGoodDetailNumberAfter.getText().toString();
                int number = Integer.parseInt(numberText);
                if (number > 1){
                    number -= 1;
                    numberText = String.valueOf(number);
                    mGoodDetailNumberAfter.setText(numberText);
                }
            }
        });

        TextView mAddNumber = (TextView) findViewById(R.id.add_number);
        mAddNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView mGoodDetailNumberAfter = (TextView) findViewById(R.id.good_detail_number);
                String numberText = mGoodDetailNumberAfter.getText().toString();
                int number = Integer.parseInt(numberText);
                number += 1;
                numberText = String.valueOf(number);
                mGoodDetailNumberAfter.setText(numberText);
            }
        });

        Button mEditOrAddButton = (Button) findViewById(R.id.edit_or_add_button);
        mEditOrAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView mGoodDetailNumberAfter = (TextView) findViewById(R.id.good_detail_number);
                String numberText = mGoodDetailNumberAfter.getText().toString();
                int number = Integer.parseInt(numberText);
                API.number = number;
                if (API.buttonID == 0) {
                    HttpThreadTask task = API.addButton(goodID, number, addButtonListener);
                    if (task != null)
                        task.execute();
                }
                else {
                    HttpThreadTask task = API.editButton(API.buttonID, goodID, number, editButtonListener);
                    if (task != null)
                        task.execute();
                }
            }
        });

    }
    ResponseListener addButtonListener = new ResponseListener() {
        @Override
        public void callback(String response) throws JSONException {
            JSONObject jsonObject = API.ResponseShow(getApplicationContext(), response);
            int newButtonID = jsonObject.getInt("body");
            API.buttonID = newButtonID;
            Toast.makeText(getApplicationContext(), "添加按钮成功", Toast.LENGTH_SHORT).show();
        }
    };

    ResponseListener editButtonListener = new ResponseListener() {
        @Override
        public void callback(String response) throws JSONException {
            JSONObject jsonObject = API.ResponseShow(getApplicationContext(), response);
            Toast.makeText(getApplicationContext(), "编辑按钮成功", Toast.LENGTH_SHORT).show();
        }
    };

}

