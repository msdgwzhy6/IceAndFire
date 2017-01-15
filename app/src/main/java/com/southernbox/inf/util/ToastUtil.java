package com.southernbox.inf.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	private static Toast toast;

	public static void toastShow(Context context, String text) {
		if (toast == null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		} else {
			toast.setText(text);
		}
		toast.show();
	}
	
	public static void toastCancel(){
		toast.cancel();
	}
}
