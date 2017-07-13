package cn.a6_79.wepay_buyer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        EditText mWifiPassword = (EditText)findViewById(R.id.edit_wifi_account);
        if (!wifiPassword.equals("请设置wifi密码")) {
            mWifiPassword.setText(wifiPassword);
        }

        Button mSaveConnectInfo = (Button)findViewById(R.id.save_button_connect_info);
        mSaveConnectInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText mWifiAccountText = (EditText)findViewById(R.id.edit_wifi_account);
                EditText mWifiPasswordText = (EditText)findViewById(R.id.edit_wifi_password);
                String wifiAccountText = mWifiAccountText.getText().toString();
                String wifiPasswordText = mWifiPasswordText.getText().toString();

                //TODO:写文件
            }
        });

    }

    private void save(String data, String filename) {
        FileOutputStream out = null;
        PrintStream ps = null;
        try {
            out = super.openFileOutput(filename, EditConnectButtonInfo.MODE_APPEND);
            ps = new PrintStream(out);
            ps.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                    ps.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}