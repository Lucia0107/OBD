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
public class DataStreamActivity extends CarManagerActivity {

    private Button dataStreamStart;
    private Button dataStreamStop;
    private Button dataStreamReturn;
    private TextView engine_load;
    private TextView coolant_temperature;
    private TextView engine_speed;
    private TextView mileage;
    private TextView speed;
    private TextView air_temperature;
    private TextView air_flow;
    private TextView position;
    private TextView voltage;
    private TextView fault_condition;
    private TextView fuel_trim;
    private TextView angle;
    private TextView pressure;
    private TextView OBD_type;
    private TextView fuel_time;
    private TextView fuel_distance;


    private HashMap<String,String> hashMap =null;
    private byte[] bytes = new byte[]{0x56,0x4b,0x01,0x00, (byte) 0x8c, (byte) 0xcf,0x0d,0x0a};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_stream);

        dataStreamStart = (Button) findViewById(R.id.dataStreamStart);
        dataStreamStop = (Button) findViewById(R.id.dataStreamStop);
        dataStreamReturn = (Button) findViewById(R.id.dataStreamReturn);

        engine_load = (TextView) findViewById(R.id.engine_load);
        coolant_temperature = (TextView) findViewById(R.id.coolant_temperature);
        engine_speed = (TextView) findViewById(R.id.engine_speed);
        mileage = (TextView) findViewById(R.id.mileage);
        speed = (TextView) findViewById(R.id.speed);
        air_temperature = (TextView) findViewById(R.id.air_temperature);
        air_flow = (TextView) findViewById(R.id.air_flow);
        position = (TextView) findViewById(R.id.position);
        voltage = (TextView) findViewById(R.id.voltage);
        fault_condition = (TextView) findViewById(R.id.fault_condition);
        fuel_trim = (TextView) findViewById(R.id.fuel_trim);
        angle = (TextView) findViewById(R.id.angle);
        pressure = (TextView) findViewById(R.id.pressure);
        OBD_type = (TextView) findViewById(R.id.OBD_type);
        fuel_time = (TextView) findViewById(R.id.fuel_time);
        fuel_distance = (TextView) findViewById(R.id.fuel_distance);

        dataStreamStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HardwareControler.write(fd,bytes) != -1){
                    read("data_stream");
                }
                else {
                    Toast.makeText(DataStreamActivity.this, "测量失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dataStreamStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DataStreamActivity.this, "结束测量GIT",Toast.LENGTH_SHORT).show();
            }
        });

        dataStreamReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataStreamActivity.this.finish();
            }
        });
    }

    @Override
    protected void showResult(HashMap<String, HashMap<String, String>> infoMap) {
        hashMap = infoMap.get("data_stream");
        engine_load.setText(hashMap.get("engine_load"));
        coolant_temperature.setText(hashMap.get("coolant_temperature"));
        engine_speed.setText(hashMap.get("engine_speed"));
        mileage.setText(hashMap.get("mileage"));
        speed.setText(hashMap.get("speed"));
        air_temperature.setText(hashMap.get("air_temperature"));
        air_flow.setText(hashMap.get("air_flow"));
        position.setText(hashMap.get("position"));
        voltage.setText(hashMap.get("voltage"));
        fault_condition.setText(hashMap.get("fault_condition"));
        fuel_trim.setText(hashMap.get("fuel_trim"));
        angle.setText(hashMap.get("angle"));
        pressure.setText(hashMap.get("pressure"));
        OBD_type.setText(hashMap.get("OBD_type"));
        fuel_time.setText(hashMap.get("fuel_time"));
        fuel_distance.setText(hashMap.get("fuel_distance"));

    }


}
