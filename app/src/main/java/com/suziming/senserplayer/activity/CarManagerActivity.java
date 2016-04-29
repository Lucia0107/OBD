package com.suziming.senserplayer.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.friendlyarm.AndroidSDK.HardwareControler;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by little grey on 2016/4/24.
 */
public abstract class CarManagerActivity extends Activity{


    public int fd;//文件描述符

    private Thread listen;//接收数据的线程
    public byte[] buf = new byte[32];//数据缓存区

    private static int RECEIVE_DATA_TIME = 4000;//线程等待时间

    public HashMap<String, HashMap<String, String>> infoMap;//存放解析完成的数据


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fd = HardwareControler.openSerialPort("/dev/s3c2410_serial0", 115200, 8, 1);

    }

    @SuppressLint("HandlerLeak")
    private Handler revHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            String type = (String) msg.obj;
            if (isSynchronize(buf)) {
                parseData(buf,type);
            }

            showResult(infoMap);
            super.handleMessage(msg);
        }
    };

    public void read(final String type) {

        listen = new Thread(new Runnable() {
            @Override
            public void run() {
                int m = HardwareControler.select(fd, 1, 0);
                if (m == 1) {
                    while ((HardwareControler.read(fd, buf,
                            buf.length)) > 0) {
                        try {
                            Thread.sleep(RECEIVE_DATA_TIME);                // 睡眠等待数据完全接收
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Message message = Message.obtain();
                        message.obj = type;
                        revHandler.sendMessage(message);
                    }
                }
            }
        });
        listen.start();
    }

    private void parseData(byte [] Re_buf, String type) {

        HashMap<String, String> map = new HashMap<>();
        switch (type){
            case "data_stream":

                double engine_load = Re_buf[4];
                double coolant_temperature = Re_buf[5] - 40;
                double engine_speed = Re_buf[6]*256 +Re_buf[7];
                double mileage = Re_buf[8]*16777216 + Re_buf[9]*65536 + Re_buf[10]*256 + Re_buf[11];
                double speed = Re_buf[12];
                double air_temperature = Re_buf[13] - 40;
                double air_flow = (Re_buf[14]*256 + Re_buf[15]) / 100;
                double posion = Re_buf[16];
                double volbage = Re_buf[17];
                double fault_condition = Re_buf[18];
                double fuel_trim = ((Re_buf[19] - 128)*100/128);
                double angle = Re_buf[20] - 64;
                double pressure = Re_buf[21];
                double OBD_type = Re_buf[22];
                double fuel_time = Re_buf[23];
                double fuel_distance = Re_buf[24];

                map.put("engine_load", String.valueOf(engine_load));
                map.put("coolant_temperature", String.valueOf(coolant_temperature));
                map.put("engine_speed", String.valueOf(engine_speed));
                map.put("mileage", String.valueOf(mileage));
                map.put("speed", String.valueOf(speed));
                map.put("air_temperature", String.valueOf(air_temperature));
                map.put("air_flow", String.valueOf(air_flow));
                map.put("position", String.valueOf(posion));
                map.put("voltage", String.valueOf(volbage));
                map.put("fault_condition", String.valueOf(fault_condition));
                map.put("fuel_trim", String.valueOf(fuel_trim));
                map.put("angle", String.valueOf(angle));
                map.put("pressure", String.valueOf(pressure));
                map.put("OBD_type", String.valueOf(OBD_type));
                map.put("fuel_time", String.valueOf(fuel_time));
                map.put("fuel_distance", String.valueOf(fuel_distance));
                infoMap.put("data_stream",map);

                break;
            case "health" :
                int sysID = Re_buf[4];
                int state = Re_buf[5];
                map.put("sysID", String.valueOf(sysID));
                map.put("state", String.valueOf(state));
                infoMap.put("health",map);
                break;
            case "vin" :
                byte[] temp = new byte[17];
                for(int i = 0; i < 17; i++){
                    temp[i] = Re_buf[i+4];
                }
                try {
                    String vinCode = new String(temp,"utf-8");
                    map.put("vinCode",vinCode);
                    infoMap.put("vin",map);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                break;
            case "fuel" :
                double initial_oil = Re_buf[4]*16777216 + Re_buf[5]*65536 + Re_buf[6]*256 + Re_buf[7];
                double final_oil;
                if(initial_oil >= 32768){
                    final_oil = (initial_oil - 32768) / 10;
                    map.put("final_oil",String.valueOf(final_oil) + "%");
                }
                else {
                    final_oil = initial_oil / 10;
                    map.put("final_oil",String.valueOf(final_oil) + "L");
                }

                infoMap.put("fuel",map);
                break;
            default:
                break;
        }

    }

    protected abstract  void showResult(HashMap<String, HashMap<String, String>> infoMap);
    private boolean isSynchronize(byte [] buf) {
        if ((buf[0] == 69) && (buf[1] == 76)) {
            return true;
        }
        return false;
    }
}
