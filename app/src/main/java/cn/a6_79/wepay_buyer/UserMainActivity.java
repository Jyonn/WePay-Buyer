package cn.a6_79.wepay_buyer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

import static android.nfc.NdefRecord.createMime;

public class UserMainActivity extends Activity implements RadioGroup.OnCheckedChangeListener,NfcAdapter.CreateNdefMessageCallback {
    private FragmentManager fragmentManager;
    NfcAdapter mNfcAdapter;
    Context context;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNFC();
        initInterface();
    }

    private void initInterface () {
        setContentView(R.layout.activity_usermain);
        fragmentManager = getFragmentManager();
        RadioGroup radioGroup = findViewById(R.id.bottom_tab);
        radioGroup.setOnCheckedChangeListener(this);

        findViewById(R.id.button_list).performClick();

    }

    private void initNFC(){
        context = this;
        try {
            //get the position of the file
            File filePath;
            filePath = new File(context.getExternalFilesDir(null), "info.json");
            //if file exists
            if(filePath.exists()){
                // Check for available NFC Adapter
                mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
                if (mNfcAdapter == null) {
                    Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
                //read it out in contnet
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(filePath));
                JSONObject jsonObj = (JSONObject) obj;
                content = jsonObj.toString();
                // Register callback
                mNfcAdapter.setNdefPushMessageCallback(this, this);
            }
            else{
                Toast.makeText(this, "请注册登录并初始化网络信息", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { createMime(
                        "application/vnd.com.example.android.beam", content.getBytes())
                });
        return msg;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        android.app.Fragment fragment = FragmentFactory.getBottomTabInstanceByIndex(checkedId);
        transaction.replace(R.id.bottom_content, fragment);
        transaction.commit();
    }
}