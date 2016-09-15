package de.cokuss.chhe.pinmoney;

import android.content.SharedPreferences;


/**
 * getSharedPreferences(...) funktioniert nur wenn es an eine Activitiy
 * gebunden ist
 */
public class SPHelper extends MainActivity{
    SharedPreferences preferences;
    String noValue;

    public SPHelper(String prefName, String noValue) {
        preferences = getSharedPreferences(prefName, MODE_PRIVATE);
        this.noValue = noValue;
    }

    public boolean safeString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,value);
        return editor.commit();
    }

    public String loadString(String key) {
        return preferences.getString(key,noValue);
    }
}
