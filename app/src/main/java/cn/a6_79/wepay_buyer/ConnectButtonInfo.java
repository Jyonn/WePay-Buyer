package cn.a6_79.wepay_buyer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ConnectButtonInfo extends AppCompatActivity {

    private Context context;
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

    private void initInterface () {
        setContentView(R.layout.button_connect_info);
        TextView mWifiAccount = (TextView)findViewById(R.id.wifi_account);
        TextView mWifiPassword = (TextView)findViewById(R.id.wifi_password);

        JSONObject getFileContent;
        try {
            getFileContent = readInfo();
            if (getFileContent == null || !getFileContent.get("UserID").equals(User.userID)) {
                mWifiAccount.setText("请设置wifi账号");
                mWifiPassword.setText("请设置wifi密码");
            } else {
                String wifiAccount = (String)getFileContent.get("SSID");
                String wifiPassword = (String)getFileContent.get("PWD");
                mWifiAccount.setText(wifiAccount);
                mWifiPassword.setText(wifiPassword);
            }
        } catch (ParseException | IOException | JSONException e) {
            e.printStackTrace();
        }

        ImageView mReturnImage = (ImageView)findViewById(R.id.return_personal_info);
        mReturnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button mEditConnectInfo = (Button)findViewById(R.id.edit_connect_info);
        mEditConnectInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView mWifiAccountText = (TextView)findViewById(R.id.wifi_account);
                TextView mWifiPasswordText = (TextView)findViewById(R.id.wifi_password);
                String wifiAccountText = mWifiAccountText.getText().toString();
                String wifiPasswordText = mWifiPasswordText.getText().toString();

                Intent intent = new Intent(getApplicationContext(), EditConnectButtonInfo.class);
                intent.putExtra("wifi_account", wifiAccountText);
                intent.putExtra("wifi_password", wifiPasswordText);
                startActivity(intent);
            }
        });
    }

    private JSONObject readInfo() throws IOException, ParseException, JSONException {
        File filePath;
        this.context = this;
        filePath = new File(context.getExternalFilesDir(null), "info.json");
        try {
            if(filePath.exists()) {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(filePath));
                JSONObject jsonObj = (JSONObject) obj;
                return jsonObj;
            }
            else{
                return null;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
