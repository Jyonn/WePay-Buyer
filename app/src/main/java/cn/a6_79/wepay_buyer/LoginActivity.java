package cn.a6_79.wepay_buyer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import cn.a6_79.wepay_buyer.NetPack.ThreadTask;

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
        public void callback(String response) {
            JSONObject jsonObject = API.ResponseShow(getApplicationContext(), response);
//            if (jsonObject != null) {
//                Intent intent = new Intent(getApplicationContext(), )
//            }
        }
    };
}
