package com.suziming.senserplayer.activity;

import android.annotation.SuppressLint;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.suziming.senserplayer.R;
import com.suziming.senserplayer.manager.AccelerationManager;

import java.util.Timer;
import java.util.TimerTask;

public class AccelerationActivity extends AppCompatActivity {

    Button btnBack;
    Button btnStart;
    Button btnStop;

    TextView tvA;
    TextView tvW;
    TextView tvAngle;

    Timer timer;
    TimerTask task;

    @SuppressLint("HandlerLeak")
    private Handler revHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            displayData(tvA, manager.accelerometer);
            displayData(tvW, manager.angularVelocity);
            displayData(tvAngle, manager.angle);
            super.handleMessage(msg);
        }
    };

    private AccelerationManager manager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceleration);
        timer = new Timer(true);
        initComponent();
    }

    @Override
    protected void onDestroy() {
        manager.stopReading();
        System.out.println("destroy");
        super.onDestroy();
    }

    private void initComponent() {
        manager = new AccelerationManager();
        btnBack = (Button)findViewById(R.id.btnBack);
        btnStart = (Button)findViewById(R.id.btnStart);
        btnStop = (Button)findViewById(R.id.btnStop);
        tvA = (TextView)findViewById(R.id.tvA);
        tvW = (TextView)findViewById(R.id.tvW);
        tvAngle = (TextView)findViewById(R.id.tvAngle);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccelerationActivity.this.finish();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                task.cancel();
                task = null;
                manager.stopReading();
            }
        });
    }

    private void displayData(TextView view, double[] data) {
        view.setText(String.format("%.3f %.3f %.3f", data[0], data[1], data[2]));
    }

}
