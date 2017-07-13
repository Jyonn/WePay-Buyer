package cn.a6_79.wepay_buyer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileInputStream;
import java.util.Scanner;

public class ConnectButtonInfo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_connect_info);
        TextView mWifiAccount = (TextView)findViewById(R.id.wifi_account);
        TextView mWifiPassword = (TextView)findViewById(R.id.wifi_password);

        StringBuffer getFileContent = read("connectInfo.txt");
        if (getFileContent == null) {
            mWifiAccount.setText("请设置wifi账号");
            mWifiPassword.setText("请设置wifi密码");
        } else {
//            TODO:处理StringBuffer

        }

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

    private StringBuffer read(String filename) {
        FileInputStream in;
        Scanner s;
        StringBuffer sb = new StringBuffer();
        try {
            in = super.openFileInput(filename);
            s = new Scanner(in);
            while (s.hasNext()) {
                sb.append(s.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }
}
