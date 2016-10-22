package de.cokuss.chhe.pinmoney;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class DateHelper {
    private final static String LOG_TAG = DateHelper.class.getSimpleName();
    private void log(String string) {
        Log.d(LOG_TAG, string);
    }

    Date today = new Date();
    final DateFormat sdfShort = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
    final DateFormat sdfLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());


    String setNowDate() {
        return sdfLong.format(today);
    }
}
