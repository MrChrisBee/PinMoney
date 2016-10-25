package de.cokuss.chhe.pinmoney;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class DateHelper {
    private final static String LOG_TAG = DateHelper.class.getSimpleName();
    private void log(String string) {
        Log.d(LOG_TAG, string);
    }
    private DateHelper dateHelper;

    private Date today = new Date();
    final DateFormat sdfShort = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
    final DateFormat sdfLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

    Date string2Date(Check4EditText c4Thing) {
        Date date = null;
        if (c4Thing.isValid()) {
            try {
                date = dateHelper.sdfShort.parse(c4Thing.getString());
                log("string2Date Datum: " + date.toString());
            } catch (ParseException e) {
                c4Thing.getEditText().setError("Datum ist ungültig!");
                log("string2Date Ungültig");
            }
        }
        return date;
    }
    Date string2Date(String string) {
        Date date = null;
            try {
                date = dateHelper.sdfShort.parse(string);
            } catch (ParseException e) {
                log("string2Date Ungültig");
            }
        return date;
    }

    String setNowDate() {
        return sdfLong.format(today);
    }
}
