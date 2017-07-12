package cn.a6_79.wepay_buyer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import cn.a6_79.wepay_buyer.NetPack.ThreadTask;

import static cn.a6_79.wepay_buyer.API.cookie;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        API.init();

        final EditText
                mUsername = (EditText) findViewById(R.id.login_username),
                mPassword = (EditText) findViewById(R.id.login_password);

        Button mLoginBtn = (Button) findViewById(R.id.login_button);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThreadTask task = API.login(mUsername.getText().toString(), mPassword.getText().toString(), loginListener);
                if (task != null)
                    task.execute();
            }
        });

        TextView mGoRegister = (TextView) findViewById(R.id.go_to_register);
        mGoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    ResponseListener loginListener = new ResponseListener() {
        @Override
        public void callback(String response) throws JSONException {
            JSONObject jsonObject = API.ResponseShow(getApplicationContext(), response);
            JSONObject body = jsonObject.getJSONObject("body");
            if (body != null) {
                EditText usernameText = (EditText) findViewById(R.id.login_username);
                String username = usernameText.getText().toString();
                String userID = body.getString("user_id");
                String avatar = body.getString("avatar");

                User.userID = userID;
                User.username = username;
                User.avatar = avatar;

                Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                startActivity(intent);
            }
        }
    };
}
