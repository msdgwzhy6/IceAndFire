package com.southernbox.inf.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by SouthernBox on 2016/3/28.
 * 吐司工具类
 */

public class ToastUtil {

    private static Toast toast;

    public static void show(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void cancel() {
        toast.cancel();
    }

}
