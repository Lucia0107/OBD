package com.suziming.senserplayer.manager;

/**
 * Created by Su Ziming on 2016/3/31.
 */
public interface PressureSerial extends Serial {
    // 位置
    String TIRE_POSITION_FL_KEY = "TIRE_POSITION_FL_KEY";
    String TIRE_POSITION_FR_KEY = "TIRE_POSITION_FR_KEY";
    String TIRE_POSITION_RL_KEY = "TIRE_POSITION_RL_KEY";
    String TIRE_POSITION_RR_KEY = "TIRE_POSITION_RR_KEY";

    // 状态
    String TIRE_PRESSURE_KEY = "TIRE_PRESSURE_KEY";
    String TIRE_TEMPERATURE_KEY = "TIRE_TEMPERATURE_KEY";
    String TIRE_LEAKAGE_STATE_KEY = "TIRE_LEAKAGE_STATE_KEY";
    String TIRE_BATTERY_STATE_KEY = "TIRE_BATTERY_STATE_KEY";
    String TIRE_SIGNAL_STATE_KEY = "TIRE_SIGNAL_STATE_KEY";
}
