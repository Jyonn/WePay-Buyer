package cn.a6_79.wepay_buyer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cn.a6_79.wepay_buyer.NetPack.HttpThreadTask;

public class RegisterActivity extends AppCompatActivity {
    private SharedPreferences account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView mGoLogin = (TextView) findViewById(R.id.go_to_login);
        mGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                HttpThreadTask task = API.captcha(mPhone.getText().toString(), captchaListener);
                if (task != null) {
                    task.execute();
                }
            }
        });
        Button mRegisterBtn = (Button) findViewById(R.id.register_button);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpThreadTask task = API.register(
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
        private User user;
        @Override
        public void callback(String response) throws JSONException {
            JSONObject jsonObject = API.ResponseShow(getApplicationContext(), response);
            JSONObject body = jsonObject.getJSONObject("body");
            if (body != null) {
                EditText usernameText = (EditText) findViewById(R.id.login_username);
                EditText passwordText = (EditText) findViewById(R.id.register_password);
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();
                String userID = body.getString("user_id");
                String avatar = body.getString("avatar");

                User.userID = userID;
                User.username = username;
                User.avatar = avatar;

                account = getSharedPreferences("account", 0);
                SharedPreferences.Editor editor = account.edit();
                editor.putString("username", username);
                editor.putString("password", password);
                editor.commit();
                finish();

                Intent intent = new Intent(RegisterActivity.this, UserMainActivity.class);
                startActivity(intent);
            }
        }
    };
}
