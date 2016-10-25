package de.cokuss.chhe.pinmoney;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class DateHelper {
    private final static String LOG_TAG = DateHelper.class.getSimpleName();
    final DateFormat sdfShort = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
    final DateFormat sdfLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
    private Date today = new Date();

    //private DateHelper dateHelper = new DateHelper();
    private void log(String string) {
        Log.d(LOG_TAG, string);
    }

    Date string2Date(Check4EditText c4Thing) {
        Date date = null;
        if (c4Thing.isValid()) {
            try {
                //date = dateHelper.sdfShort.parse(c4Thing.getString()); //auch wenn vorhanden bringt die App zum Absturz ???
                date = sdfShort.parse(c4Thing.getString());
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
            date = sdfShort.parse(string);
        } catch (ParseException e) {
        }
        return date;
    }

    String setNowDate() {
        return sdfLong.format(today);
    }
}
