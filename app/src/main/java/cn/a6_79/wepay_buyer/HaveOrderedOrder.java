package cn.a6_79.wepay_buyer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
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

public class HaveOrderedOrder extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.have_ordered_order, null);
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

        HttpThreadTask task = API.getOrder("unsent",getOrderListener);
        if (task != null)
            task.execute();

    }

    ResponseListener getOrderListener = new ResponseListener() {
        @Override
        public void callback(String response) throws JSONException {
            JSONObject jsonObject = API.ResponseShow(getActivity().getApplicationContext(), response);
            JSONObject body = jsonObject.getJSONObject("body");
            JSONArray order_list = body.getJSONArray("order_list");
            LinearLayout orderedOrderCardList = getView().findViewById(R.id.ordered_order_list);
            orderedOrderCardList.removeAllViews();
            if (order_list != null) {
                for (int i = 0; i < order_list.length(); i++) {
                    JSONObject orderInfo = order_list.getJSONObject(i);
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

                    OrderedOrderCard orderedOrderCard = new OrderedOrderCard(orderID,goodName, number, price, picture, getActivity().getApplicationContext(),
                            getActivity());
                    orderedOrderCard.setCardBackgroundColor(0xffffffff);
                    orderedOrderCardList.addView(orderedOrderCard);
                }
            }
        }
    };
}

class OrderedOrderCard extends CardView {
    private int orderID;
    private String goodName;
    private int number;
    private double totalPrice;
    private String picture;
    private double price;
    private Activity activity;

    private ImageView orderedOrderImage;
    private TextView orderedOrderGoodName;
    private TextView orderedOrderGoodPrice;
    private TextView orderedOrderTotal;


    public OrderedOrderCard (final int orderID, String goodName, int number, double totalPrice, String picture,
                               Context context, final Activity activity) {
        super(context);
        this.orderID = orderID;
        this.goodName = goodName;
        this.number = number;
        this.totalPrice = totalPrice;
        this.picture = picture;
        this.activity = activity;
        this.price = totalPrice/number;

        LayoutInflater.from(context).inflate(R.layout.button_card, this);
        orderedOrderImage = findViewById(R.id.ordered_order_img);
        orderedOrderGoodName = findViewById(R.id.ordered_order_goodName);
        orderedOrderGoodPrice = findViewById(R.id.ordered_order_goodPrice);
        orderedOrderTotal = findViewById(R.id.ordered_order_total);

        orderedOrderGoodName.setText(goodName);
        orderedOrderGoodPrice.setText("¥"+price);
        orderedOrderTotal.setText("共"+number+"件商品，合计¥"+totalPrice+"元");
        new ImageThreadTask(picture, new OnAsyncImageTaskListener() {
            @Override
            public void callback(ImageTaskResponse imageTaskResponse) {
                Bitmap bitmap = imageTaskResponse.getBitmap();
                orderedOrderImage.setImageBitmap(bitmap);
            }
        }).execute();
    }
}
