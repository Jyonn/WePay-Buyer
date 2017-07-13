package cn.a6_79.wepay_buyer;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.a6_79.wepay_buyer.NetPack.ThreadTask;

import static cn.a6_79.wepay_buyer.API.cookie;

public class PersonalInfoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_info_fragment, null);

        API.init();

        TextView usernameView = view.findViewById(R.id.username);
        usernameView.setText(User.username);

//        TODO:根据url获取网络图片，需要封装getBitmap
//        ImageView avatarView = view.findViewById(R.id.buyer_avatar);
//        Uri uri = Uri.parse(User.avatar);
//        avatarView.setImageURI(uri);

        TextView mLogout = view.findViewById(R.id.logout_button);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThreadTask task = API.logout(logoutListener);
                if (task != null)
                    task.execute();
            }
        });

        TableRow mBuyerAddress = view.findViewById(R.id.buyer_address);
        mBuyerAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), BuyerAddress.class));
            }
        });

        TableRow mBuyerCard = view.findViewById(R.id.buyer_card);
        mBuyerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), BuyerCard.class));
            }
        });

        TableRow mBuyerOrder = view.findViewById(R.id.buyer_order);
        mBuyerOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), BuyerOrder.class));
            }
        });

        TableRow mConnectButtonInfo = view.findViewById(R.id.connect_button_info);
        mConnectButtonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), ConnectButtonInfo.class));
            }
        });
        return view;
    }

    ResponseListener logoutListener = new ResponseListener() {
        @Override
        public void callback(String response) throws JSONException{
            API.ResponseShow(getActivity().getApplicationContext(), response);
            getActivity().finish();
        }
    };

}