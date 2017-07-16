package cn.a6_79.wepay_buyer;

import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;

import static android.nfc.NdefRecord.createMime;

public class ConnectNFC extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback{
    NfcAdapter mNfcAdapter;
    Context context;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_nfc_view);
        initNFC();
        ImageView mReturnImage = (ImageView)findViewById(R.id.return_personal_info);
        mReturnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
                Toast.makeText(this, "请初始化网络信息", Toast.LENGTH_LONG).show();
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

}
