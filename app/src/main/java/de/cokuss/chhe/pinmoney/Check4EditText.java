package de.cokuss.chhe.pinmoney;

import android.util.Log;
import android.widget.EditText;

import de.cokuss.chhe.pinmoney.activity.NewRecipientActivity;

public class Check4EditText {
    private static final String LOG_TAG = NewRecipientActivity.class.getSimpleName();
    private String string;
    private boolean valid;
    private EditText editText;

    Check4EditText(EditText editText, String string, boolean aBoolean) {
        this.editText = editText;
        this.string = string;
        this.valid = aBoolean;
    }

    //Check4Edit enthält deswegen ein EditText um in string2Date
    //und checkEditText möglicherweise darauf einen Fehlertext zu legen
    EditText getEditText() {
        return editText;
    }

    public boolean isValid() {
        return valid;
    }

    public String getString() {
        return string;
    }

    public static Check4EditText checkEditText(EditText nameFeld, String kind) {
        String string = nameFeld.getText().toString();
        Check4EditText c4 = new Check4EditText(nameFeld, "", false);
        switch (kind.toLowerCase()) {
            //create in every case a c4 containing the EditText, the Content and bool arg as result of testing
            case "name":
                log("In Name");
                if (string.length() == 0 || !string.matches("\\w+")) {
                    nameFeld.setError("Bitte einen Namen eingeben! Für Namen nur (A-Za-z0-9_) nuzten!");
                    c4 = new Check4EditText(nameFeld, "", false);
                    log("name Feld leer aus C4ET für " + nameFeld.getId());
                } else {
                    //erster Test bestanden
                    c4 = new Check4EditText(nameFeld, string, true);
                    log("name Test Bestanden aus C4ET " + nameFeld.getId() + " enthält " + c4.getString());
                }
                break;
            case "date":
                log("In Date");
                if (string.length() == 0 || !string.matches("^(0?[1-9]|[12][0-9]|3[01])\\.(0?[1-9]|1[012])\\.(19|20)\\d\\d$")) {
                    nameFeld.setError("Bitte ein gültiges Datum eingeben! Z.B. 31.12.1999");
                    c4 = new Check4EditText(nameFeld, "", false);
                    log("date Feld leer aus C4ET für " + nameFeld.getId());
                } else {
                    //erster Test bestanden
                    c4 = new Check4EditText(nameFeld, string, true);
                    log("date Test Bestanden aus C4ET " + nameFeld.getId() + " enthält " + c4.getString());
                }
                break;
            case "currency":
                log("In Currency");
                if (string.length() == 0 || !string.matches("^[+-]?[0-9]{1,3}(?:[0-9]*(?:[.,][0-9]{2})?|(?:,[0-9]{3})*(?:\\.[0-9]{2})?|(?:\\.[0-9]{3})*(?:,[0-9]{2})?)$")) {
                    nameFeld.setError("Bitte einen gültigen Betrag eingeben! 17.50 (Max. 2 Nachkommastellen und mit . getrennt!)");
                    c4 = new Check4EditText(nameFeld, "", false);
                    log("currency Feld leer aus C4ET für " + nameFeld.getId());
                } else {
                    //erster Test bestanden
                    c4 = new Check4EditText(nameFeld, string, true);
                    log("currency Test Bestanden aus C4ET " + nameFeld.getId() + " enthält " + c4.getString());
                }
                break;
        }
        return c4;
    }
    private static void log(String string) {
        Log.d(LOG_TAG, string);
    }
}