package de.cokuss.chhe.pinmoney;

import android.text.InputType;
import android.text.method.DigitsKeyListener;

import java.text.DecimalFormatSymbols;

public class DecimalKeyListener extends DigitsKeyListener {
    private final char[] acceptedCharacters =
            new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-',
                    new DecimalFormatSymbols().getDecimalSeparator()};

    @Override
    protected char[] getAcceptedChars() {
        return acceptedCharacters;
    }

    public int getInputType() {
        return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_TEXT;
    }

}