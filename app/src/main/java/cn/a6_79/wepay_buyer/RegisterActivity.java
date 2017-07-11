package cn.a6_79.wepay_buyer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import cn.a6_79.wepay_buyer.LoginActivity;
import cn.a6_79.wepay_buyer.NetPack.ThreadTask;
import cn.a6_79.wepay_buyer.R;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView mGoLogin = (TextView) findViewById(R.id.go_to_login);
        mGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        final EditText
                mPhone = (EditText) findViewById(R.id.register_username),
                mCaptcha = (EditText) findViewById(R.id.register_captcha),
                mPassword = (EditText) findViewById(R.id.register_password);
        Button mSendCaptchaBtn = (Button) findViewById(R.id.send_captcha_button);
        mSendCaptchaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThreadTask task = API.captcha(mPhone.getText().toString(), captchaListener);
                if (task != null) {
                    task.execute();
                }
            }
        });
        Button mRegisterBtn = (Button) findViewById(R.id.register_button);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThreadTask task = API.register(
                        mPassword.getText().toString(),
                        mCaptcha.getText().toString(),
                        registerListener
                );
                if (task != null) {
                    task.execute();
                }
            }
        });
    }

    ResponseListener captchaListener = new ResponseListener() {
        @Override
        public void callback(String response) {
            JSONObject jsonObject = API.ResponseShow(getApplicationContext(), response);
            if (jsonObject != null)
                Toast.makeText(getApplicationContext(), "验证码发送成功", Toast.LENGTH_SHORT).show();
        }
    };

    ResponseListener registerListener = new ResponseListener() {
        @Override
        public void callback(String response) {
            JSONObject jsonObject = API.ResponseShow(getApplicationContext(), response);
//            if (jsonObject != null) {
//
//            }
        }
    };
}
