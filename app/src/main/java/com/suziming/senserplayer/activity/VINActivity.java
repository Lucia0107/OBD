package com.suziming.senserplayer.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.friendlyarm.AndroidSDK.HardwareControler;
import com.suziming.senserplayer.R;

import java.util.HashMap;

/**
 * Created by little grey on 2016/4/24.
 */
public class VINActivity extends CarManagerActivity {

    private Button vinStart;
    private Button vinStop;
    private Button vinReturn;
    private TextView vin;

    private HashMap<String,String> hashMap =null;
    private byte[] bytes = new byte[]{0X56,0x4b,0x07,0x00, (byte) 0x26, (byte) 0x69,0x0d,0x0a};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vin);

        vinStart = (Button) findViewById(R.id.vinStart);
        vinStop = (Button) findViewById(R.id.vinStop);
        vinReturn = (Button) findViewById(R.id.vinReturn);
        vin = (TextView) findViewById(R.id.vin);

        vinStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HardwareControler.write(fd, bytes) != -1){
                    read("vin");
                }
                else {
                    Toast.makeText(VINActivity.this, "读取失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        vinStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VINActivity.this, "结束读取", Toast.LENGTH_SHORT).show();
            }
        });

        vinReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VINActivity.this.finish();
            }
        });
    }

    @Override
    protected void showResult(HashMap<String, HashMap<String, String>> infoMap) {
        hashMap = infoMap.get("vin");
        vin.setText(hashMap.get("vinCode"));
    }
}
