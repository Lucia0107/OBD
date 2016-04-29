package com.suziming.senserplayer.activity;

import android.annotation.SuppressLint;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.suziming.senserplayer.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.suziming.senserplayer.manager.PressureManager;

import org.w3c.dom.Text;

public class PressureActivity extends AppCompatActivity {

    Button btnStart;
    Button btnStop;
    TextView tvState;
    ListView listView;

//    SimpleAdapter adapter;

    private PressureManager manager;

    private TimerTask task;
    private Timer timer;

    @SuppressLint("HandlerLeak")
    private Handler revHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            System.out.println(Arrays.toString(manager.buf));
            System.out.println("received message");

            ArrayList<HashMap<String, String>> list = new ArrayList<>();
            list.add(manager.infoMap.get(PressureManager.TIRE_POSITION_FL_KEY));
            list.add(manager.infoMap.get(PressureManager.TIRE_POSITION_FR_KEY));
            list.add(manager.infoMap.get(PressureManager.TIRE_POSITION_RL_KEY));
            list.add(manager.infoMap.get(PressureManager.TIRE_POSITION_RR_KEY));

            String[] strings = new String[] {

                    PressureManager.TIRE_PRESSURE_KEY,
                    PressureManager.TIRE_TEMPERATURE_KEY,
                    PressureManager.TIRE_LEAKAGE_STATE_KEY,
                    PressureManager.TIRE_BATTERY_STATE_KEY,
                    PressureManager.TIRE_SIGNAL_STATE_KEY
            };

            int[] rids = new int[] {
                    R.id.tvPressure,
                    R.id.tvTemperature,
                    R.id.tvLeakage,
                    R.id.tvBattery,
                    R.id.tvSingal
            };

            SimpleAdapter adapter = new SimpleAdapter(PressureActivity.this,
                    list,
                    R.layout.layout_tire_info_item,
                    strings,
                    rids);

            listView.setAdapter(adapter);

            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preasure);
        timer = new Timer(true);
        manager = new PressureManager();
        initComponent();
    }

    @Override
    protected void onDestroy() {
        manager.stopReading();
        super.onDestroy();
    }

    private void initComponent() {

        btnStart = (Button)findViewById(R.id.btnStart);
        btnStop = (Button)findViewById(R.id.btnStop);
        tvState = (TextView)findViewById(R.id.tvState);
        listView = (ListView)findViewById(R.id.listView);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvState.setText("Start");
                if (task == null) {
                    task = new TimerTask() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            revHandler.sendMessage(message);
                        }
                    };
                }
                timer.schedule(task, 1000, 1000);
                manager.startReading();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvState.setText("Stop");
                task.cancel();
                task = null;
                manager.stopReading();
            }
        });
    }
}
