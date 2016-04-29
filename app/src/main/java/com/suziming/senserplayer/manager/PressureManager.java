package com.suziming.senserplayer.manager;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.friendlyarm.AndroidSDK.HardwareControler;

import java.util.HashMap;

/**
 * Created by Su Ziming on 2016/3/30.
 */
public class PressureManager implements PressureSerial {

    public HashMap<String, HashMap<String, String>> infoMap;

    private static int FRAME_LENGTH = 8;
    private static int REFRESH_TIME = 1000; //ms
    private static int RECEIVE_DATA_TIME = 4000;

    private Thread listen;
    private int fd;
    private Boolean openFlag = false;
    public byte[] buf = new byte[FRAME_LENGTH * 4];


    private final static byte LEAKAGE_STATE = 0b00001000; // 1为漏气报警
    private final static byte BATTERY_STATE = 0b00010000; // 1为电池电压低
    private final static byte  SIGNAL_STATE = 0b00100000; // 1为信号丢失



    @SuppressLint("HandlerLeak")
    private Handler revHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (isSynchronize(buf)) {
                parseData(buf);
            }
            super.handleMessage(msg);
        }
    };


    public PressureManager() {
        infoMap = new HashMap<>();
    }

    @Override
    public void startReading() {
        fd = HardwareControler.openSerialPort("/dev/s3c2410_serial3", 19200, 8, 1);
        openFlag = true;
        /**
         * 启动线程监听数据
         */
        listen = new Thread(new Runnable() {
            @Override
            public void run() {
                while (openFlag) {
                    int m = HardwareControler.select(fd, 1, 0);
                    String text = "";
                    if (m == 1) {
                        while ((HardwareControler.read(fd, buf,
                                buf.length)) > 0) {
                            try {
                                Thread.sleep(RECEIVE_DATA_TIME);                // 睡眠等待数据完全接收
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            Message message = Message.obtain();
                            message.obj = text;
                            revHandler.sendMessage(message);
                        }
                    }
                    try {
                        Thread.sleep(REFRESH_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        listen.start();
    }

    @Override
    public void stopReading() {

    }

    private void parseData(byte [] Re_buf) {

        for (int i = 0; i < Re_buf.length ; i += FRAME_LENGTH) {
            HashMap<String, String> map = new HashMap<>();

            double pressure = Re_buf[i + 4] * 3.44; // KPA
            double temperature = Re_buf[i + 5] - 50;
            map.put(TIRE_PRESSURE_KEY, String.valueOf(pressure));
            map.put(TIRE_TEMPERATURE_KEY, String.valueOf(temperature));

            byte state = Re_buf[i + 6];
            map.put(TIRE_LEAKAGE_STATE_KEY, String.valueOf((state & LEAKAGE_STATE)  == LEAKAGE_STATE));
            map.put(TIRE_BATTERY_STATE_KEY, String.valueOf((state & BATTERY_STATE) == BATTERY_STATE));
            map.put(TIRE_SIGNAL_STATE_KEY, String.valueOf((state & SIGNAL_STATE) == SIGNAL_STATE));

            // 车轮号
            switch (Re_buf[i+3]) {
                // 左前FL
                case 0x00:
                    infoMap.put(TIRE_POSITION_FL_KEY, map);
                    break;
                // 右前FR
                case 0x01:
                    infoMap.put(TIRE_POSITION_FR_KEY, map);
                    break;
                // 左后RL
                case 0x10:
                    infoMap.put(TIRE_POSITION_RL_KEY, map);
                    break;
                // 右后RR
                case 0x11:
                    infoMap.put(TIRE_POSITION_RR_KEY, map);
                    break;
            }

        }
    }

    private boolean isSynchronize(byte [] buf) {
        if ((buf[0] == 85) && (buf[1] == -86) && (buf[2] == 8)) {
            return true;
        }
        return false;
    }

}
