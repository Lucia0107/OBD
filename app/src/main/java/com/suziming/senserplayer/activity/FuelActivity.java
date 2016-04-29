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
public class FuelActivity extends CarManagerActivity {

    private Button fuelStart;
    private Button fuelStop;
    private Button fuelReturn;
    private TextView fuel;

    private HashMap<String,String> hashMap =null;
    private byte[] bytes = new byte[]{0X56,0x4b,0x08,0x00, (byte) 0x36, (byte) 0x57,0x0d,0x0a};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fuel);

        fuelStart = (Button) findViewById(R.id.fuelStart);
        fuelStop = (Button) findViewById(R.id.fuelStop);
        fuelReturn = (Button) findViewById(R.id.fuelReturn);

        fuel = (TextView) findViewById(R.id.fuel);

        fuelStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HardwareControler.write(fd, bytes) != -1){
                    read("fuel");
                }
                else {
                    Toast.makeText(FuelActivity.this, "读取失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fuelStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FuelActivity.this, "结束读取", Toast.LENGTH_SHORT).show();
            }
        });

        fuelReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FuelActivity.this.finish();
            }
        });
    }

    @Override
    protected void showResult(HashMap<String, HashMap<String, String>> infoMap) {
        hashMap = infoMap.get("fuel");
        fuel.setText(hashMap.get("final_oil"));
    }
}
