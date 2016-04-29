package com.suziming.senserplayer.manager;

import com.friendlyarm.AndroidSDK.HardwareControler;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Su Ziming on 2016/3/30.
 */
public class AccelerationManager implements Serial {

    public double[] accelerometer = new double[3];
    public double[] angularVelocity = new double[3];
    public double[] angle = new double[3];
    public short temperature;
    public int counter = 0;

    private static int FRAME_LENGTH = 11;
    private static int REFRESH_TIME = 1000; //ms
    private static int RECEIVE_DATA_TIME = 10;

    private Thread listen;
    private int fd;
    private Boolean openFlag = false;
    private byte[] buf = new byte[48]; //2048

    @SuppressLint("HandlerLeak")
    private Handler revHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            counter ++;
            System.out.println(counter);
            parseData(synchronizing(buf));
            super.handleMessage(msg);
        }
    };

    @Override
    public void startReading() {
        fd = HardwareControler.openSerialPort("/dev/s3c2410_serial0", 115200, 8, 1);
        openFlag = true;
        /**
         * 启动线程监听数据
         */
        listen = new Thread(new Runnable() {
            @Override
            public void run() {
                while (openFlag) {
                    int m = HardwareControler.select(fd, 0, 10);
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
        closeSerial();
    }

    private void closeSerial() {
        HardwareControler.close(fd);
        openFlag = false;
    }

    private void parseData(byte [] Re_buf) {

        for (int i = 0; i < Re_buf.length ; i += FRAME_LENGTH) {
            if (Re_buf[i] == 0x55)      //检查帧头
            {
                switch (Re_buf[1+i]) {
                    case 81:
                        accelerometer[0] = (short) ((Re_buf[3 + i] << 8 | Re_buf[2 + i])) / 32768.0 * 16;
                        accelerometer[1] = (short) ((Re_buf[5 + i] << 8 | Re_buf[4 + i])) / 32768.0 * 16;
                        accelerometer[2] = (short) ((Re_buf[7 + i] << 8 | Re_buf[6 + i])) / 32768.0 * 16;
                        temperature = (short) (((Re_buf[9 + i] << 8 | Re_buf[8 + i])) / 340.0 + 36.25);
                        break;
                    case 82:
                        angularVelocity[0] = (short) ((Re_buf[3 + i] << 8 | Re_buf[2 + i])) / 32768.0 * 2000;
                        angularVelocity[1] = (short) ((Re_buf[5 + i] << 8 | Re_buf[4 + i])) / 32768.0 * 2000;
                        angularVelocity[2] = (short) ((Re_buf[7 + i] << 8 | Re_buf[6 + i])) / 32768.0 * 2000;
                        temperature = (short) (((Re_buf[9 + i] << 8 | Re_buf[8 + i])) / 340.0 + 36.25);
                        break;
                    case 83:
                        angle[0] = (short) ((Re_buf[3 + i] << 8 | Re_buf[2 + i])) / 32768.0 * 180;
                        angle[1] = (short) ((Re_buf[5 + i] << 8 | Re_buf[4 + i])) / 32768.0 * 180;
                        angle[2] = (short) ((Re_buf[7 + i] << 8 | Re_buf[6 + i])) / 32768.0 * 180;
                        temperature = (short) (((Re_buf[9 + i] << 8 | Re_buf[8 + i])) / 340.0 + 36.25);
                        break;
                }
            }
        }
    }

    private byte [] synchronizing(byte [] buf) {
        byte [] datas = new byte[FRAME_LENGTH * 3];
        int index = 0;
        while (buf[index] != 85) {
            index++;
        }
        for (int i = 0; i < datas.length; i++) {
            datas[i] = buf[index + i];
        }
        return datas;
    }


    public double[] getAccelerometer() {
        return accelerometer;
    }

    public double[] getAngularVelocity() {
        return angularVelocity;
    }

    public double[] getAngle() {
        return angle;
    }

    public short getTemperature() {
        return temperature;
    }

    public int getCounter() {
        return counter;
    }
}
