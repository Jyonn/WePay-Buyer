package cn.a6_79.wepay_buyer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;


public class EditConnectButtonInfo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_button_connect_info);

        Intent intent = getIntent();
        String wifiAccount = intent.getStringExtra("wifi_account");
        String wifiPassword = intent.getStringExtra("wifi_password");
        EditText mWifiAccount = (EditText)findViewById(R.id.edit_wifi_account);
        if (!wifiAccount.equals("请设置wifi账号")){
            mWifiAccount.setText(wifiAccount);
        }
        EditText mWifiPassword = (EditText)findViewById(R.id.edit_wifi_password);
        if (!wifiPassword.equals("请设置wifi密码")) {
            mWifiPassword.setText(wifiPassword);
        }

        ImageView mReturnImage = (ImageView)findViewById(R.id.return_button_connect_info);
        mReturnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button mSaveConnectInfo = (Button)findViewById(R.id.save_button_connect_info);
        mSaveConnectInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText mWifiAccountText = (EditText)findViewById(R.id.edit_wifi_account);
                EditText mWifiPasswordText = (EditText)findViewById(R.id.edit_wifi_password);
                String wifiAccountText = mWifiAccountText.getText().toString();
                String wifiPasswordText = mWifiPasswordText.getText().toString();

                try {
                    saveInfo(wifiAccountText, wifiPasswordText);
                    finish();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void saveInfo(String wifiAccountText, String wifiPasswordText) throws JSONException, IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("UserID", User.username);
        jsonObject.put("SSID", wifiAccountText);
        jsonObject.put("PWD", wifiPasswordText);

        FileOutputStream fileWirte;
        PrintStream printStream;

        fileWirte = super.openFileOutput("info.json", EditConnectButtonInfo.MODE_PRIVATE);
        printStream = new PrintStream(fileWirte);
        printStream.print(jsonObject.toString());
        fileWirte.close();
        printStream.close();
    }
}