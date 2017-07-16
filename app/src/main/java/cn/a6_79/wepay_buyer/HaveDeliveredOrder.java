package cn.a6_79.wepay_buyer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.a6_79.wepay_buyer.NetPack.HttpThreadTask;
import cn.a6_79.wepay_buyer.NetPack.ImageTaskResponse;
import cn.a6_79.wepay_buyer.NetPack.ImageThreadTask;
import cn.a6_79.wepay_buyer.NetPack.OnAsyncImageTaskListener;


public class HaveDeliveredOrder extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.have_delivered_order, null);
        initInterface();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initInterface();
    }

    private void initInterface() {
        API.init();

        HttpThreadTask task = API.getOrder("unreceived",getOrderListener);
        if (task != null)
            task.execute();

    }

    ResponseListener getOrderListener = new ResponseListener() {
        @Override
        public void callback(String response) throws JSONException {
            JSONObject jsonObject = API.ResponseShow(getActivity().getApplicationContext(), response);
            JSONArray body = jsonObject.getJSONArray("body");
            LinearLayout deliveredOrderCardList = getView().findViewById(R.id.delivered_order_list);
            deliveredOrderCardList.removeAllViews();
            if (body != null) {
                for (int i = 0; i < body.length(); i++) {
                    JSONObject orderInfo = body.getJSONObject(i);
                    int orderID = orderInfo.getInt("order_id");
                    String goodName =  orderInfo.getString("good_name");
                    String realName = orderInfo.getString("real_name");
                    String phoneNum = orderInfo.getString("phone");
                    String address = orderInfo.getString("address");
                    int number = orderInfo.getInt("number");
                    Double price = orderInfo.getDouble("price");
                    String picture = orderInfo.getString("pic");
                    String createTime = orderInfo.getString("create_time");
                    String deliverTime = orderInfo.getString("deliver_time");

                    DeliveredOrderCard deliveredOrderCard = new DeliveredOrderCard(orderID,goodName, number, price, picture, getActivity().getApplicationContext(),
                            getActivity(), deleteDeliveredOrderListener);
                    deliveredOrderCard.setCardBackgroundColor(0xffffffff);
                    deliveredOrderCardList.addView(deliveredOrderCard);
                }
            }
        }
    };

    ResponseListener deleteDeliveredOrderListener = new ResponseListener() {
        @Override
        public void callback(String response) throws JSONException {
            JSONObject jsonObject = API.ResponseShow(getActivity().getApplicationContext(), response);
            initInterface();
        }
    };

}

class DeliveredOrderCard extends CardView {
    private int orderID;
    private String goodName;
    private int number;
    private double totalPrice;
    private String picture;
    private double price;
    private Activity activity;

    private ImageView deliveredOrderImage;
    private TextView deliveredOrderGoodName;
    private TextView deliveredOrderGoodPrice;
    private TextView deliveredOrderTotal;
    private Button confirmReceiptButton;


    public DeliveredOrderCard (final int orderID, String goodName, int number, double totalPrice, String picture,
                       Context context, final Activity activity, final ResponseListener deleteDeliveredOrderListener) {
        super(context);
        this.orderID = orderID;
        this.goodName = goodName;
        this.number = number;
        this.totalPrice = totalPrice;
        this.picture = picture;
        this.activity = activity;
        this.price = totalPrice/number;

        LayoutInflater.from(context).inflate(R.layout.button_card, this);
        deliveredOrderImage = findViewById(R.id.delivered_order_img);
        deliveredOrderGoodName = findViewById(R.id.delivered_order_goodName);
        deliveredOrderGoodPrice = findViewById(R.id.delivered_order_goodPrice);
        deliveredOrderTotal = findViewById(R.id.delivered_order_total);
        confirmReceiptButton = findViewById(R.id.confirm_receipt_button);

        deliveredOrderGoodName.setText(goodName);
        deliveredOrderGoodPrice.setText("¥"+price);
        deliveredOrderTotal.setText("共"+number+"件商品，合计¥"+totalPrice+"元");
        new ImageThreadTask(picture, new OnAsyncImageTaskListener() {
            @Override
            public void callback(ImageTaskResponse imageTaskResponse) {
                Bitmap bitmap = imageTaskResponse.getBitmap();
                deliveredOrderImage.setImageBitmap(bitmap);
            }
        }).execute();

        confirmReceiptButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity).setMessage("确认收货嘛?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                HttpThreadTask task = API.receive(orderID, deleteDeliveredOrderListener);
                                if (task != null)
                                    task.execute();
                            }
                        }).setNegativeButton("取消", null);
                builder.show();
            }
        });

    }
}
