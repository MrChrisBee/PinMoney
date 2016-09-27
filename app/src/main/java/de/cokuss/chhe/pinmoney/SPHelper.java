package de.cokuss.chhe.pinmoney;

import android.content.Context;
import android.content.SharedPreferences;

//HelperKlasse für den Umgang mit Shared Preferenzes


public class SPHelper {

        public static void safeString(Context context, String key, String value) {
            SharedPreferences sharedPref = context.getSharedPreferences(key,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(key, value);
            editor.commit();
        }

        public static String loadString(Context context, String key,String noValue) {
            SharedPreferences sharedPref = context.getSharedPreferences(key,Context.MODE_PRIVATE);
            return sharedPref.getString(key, noValue);
        }
}
