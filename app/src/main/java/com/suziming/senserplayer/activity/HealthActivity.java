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
public class HealthActivity extends CarManagerActivity {

    private Button healthStart;
    private Button healthStop;
    private Button healthReturn;
    private TextView engine_system;
    private TextView gear_box_system;
    private TextView brake_system;
    private TextView air_bag_system;

    private HashMap<String,String> hashMap =null;
    private byte[] bytes = new byte[]{0X56,0x4b,0x03,0x00, (byte) 0xea, (byte) 0xad,0x0d,0x0a};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health);

        healthStart = (Button) findViewById(R.id.healthStart);
        healthStop = (Button) findViewById(R.id.healthStop);
        healthReturn = (Button) findViewById(R.id.healthReturn);

        engine_system = (TextView) findViewById(R.id.engine_system);
        gear_box_system = (TextView) findViewById(R.id.gear_box_system);
        brake_system = (TextView) findViewById(R.id.brake_system);
        air_bag_system = (TextView) findViewById(R.id.air_bag_system);

        healthStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HardwareControler.write(fd, bytes) != -1){
                    read("health");
                }
                else {
                    Toast.makeText(HealthActivity.this, "检查失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        healthStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HealthActivity.this, "结束检查", Toast.LENGTH_SHORT).show();
            }
        });

        healthReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HealthActivity.this.finish();
            }
        });
    }

    @Override
    protected void showResult(HashMap<String, HashMap<String, String>> infoMap) {
        hashMap = infoMap.get("health");
        if(hashMap.get("state").equals("00")){
            engine_system.setText("发动机系统故障");
        }
        else if(hashMap.get("state").equals("01")){
            engine_system.setText("发动机系统正常");
        }
    }
}
