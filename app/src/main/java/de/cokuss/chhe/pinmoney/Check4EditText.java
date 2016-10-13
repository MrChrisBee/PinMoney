package de.cokuss.chhe.pinmoney;

import android.widget.EditText;

class Check4EditText {
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

    boolean isValid() {
        return valid;
    }

    public String getString() {
        return string;
    }
}