package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.media.midi.MidiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.aidl.Request;
import com.example.aidl.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ClientConnectHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        bindClick();
    }

    private void init() {
        helper = ClientConnectHelper.getmInstance();
        helper.init(this);
    }

    private void bindClick() {

        findViewById(R.id.btnBind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.bindService();
                Toast.makeText(MainActivity.this, "bind service", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != helper) {
                    Response response = helper.sendRequest(new Request("0", "hello aidl", true));
                    Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                    Log.i(TAG, response.toString());
                }
            }
        });
        findViewById(R.id.btnUnbind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.unBindSevice();
                Toast.makeText(MainActivity.this, "unBind service", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (helper != null) {
            helper.unBindSevice();
        }
    }
}
