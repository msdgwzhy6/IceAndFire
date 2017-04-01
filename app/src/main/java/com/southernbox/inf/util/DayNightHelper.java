package com.southernbox.inf.util;

import android.content.Context;
import android.content.SharedPreferences;

public class DayNightHelper {

    private final static String FILE_NAME = "ice_and_fire";
    private final static String MODE = "day_night_mode";

    private SharedPreferences mSharedPreferences;

    public DayNightHelper(Context context) {
        this.mSharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 保存模式设置
     *
     * @param mode 模式
     */
    public void setMode(DayNight mode) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(MODE, mode.getName()).apply();
    }

    /**
     * 夜间模式
     *
     * @return 是否夜间模式
     */
    public boolean isNight() {
        String mode = mSharedPreferences.getString(MODE, DayNight.DAY.getName());
        return DayNight.NIGHT.getName().equals(mode);
    }

    /**
     * 日间模式
     *
     * @return 是否白天模式
     */
    public boolean isDay() {
        String mode = mSharedPreferences.getString(MODE, DayNight.DAY.getName());
        return DayNight.DAY.getName().equals(mode);
    }

    public enum DayNight {

        DAY("DAY"),
        NIGHT("NIGHT");

        private String name;

        DayNight(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
