package com.suziming.senserplayer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.suziming.senserplayer.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button accelerationButton;// = (Button)findViewById(R.id.accelerationButton);
    private Button preasureButton;// = (Button)findViewById(R.id.preasureButton);
    private Button dataButton;
    private Button healthButton;
    private Button vinButton;
    private Button fuelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        accelerationButton = (Button)findViewById(R.id.accelerationButton);
        preasureButton = (Button)findViewById(R.id.pressureButton);
        dataButton = (Button) findViewById(R.id.dataButton);
        healthButton = (Button) findViewById(R.id.healthButton);
        vinButton = (Button) findViewById(R.id.vinButton);
        fuelButton = (Button) findViewById(R.id.fuelButton);

        accelerationButton.setOnClickListener(this);
        preasureButton.setOnClickListener(this);
        dataButton.setOnClickListener(this);
        healthButton.setOnClickListener(this);
        vinButton.setOnClickListener(this);
        fuelButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accelerationButton:
                Intent intent1 = new Intent(MainActivity.this, AccelerationActivity.class);
                startActivity(intent1);
                break;
            case R.id.pressureButton:
                Intent intent2 = new Intent(MainActivity.this, PressureActivity.class);
                startActivity(intent2);
                break;
            case R.id.dataButton:
                Intent intent3 = new Intent(MainActivity.this, DataStreamActivity.class);
                startActivity(intent3);
                break;
            case R.id.healthButton:
                Intent intent4 = new Intent(MainActivity.this, HealthActivity.class);
                startActivity(intent4);
                break;
            case R.id.vinButton:
                Intent intent5 = new Intent(MainActivity.this, VINActivity.class);
                startActivity(intent5);
                break;
            case R.id.fuelButton:
                Intent intent6 = new Intent(MainActivity.this, FuelActivity.class);
                startActivity(intent6);
                break;
        }

    }
}
