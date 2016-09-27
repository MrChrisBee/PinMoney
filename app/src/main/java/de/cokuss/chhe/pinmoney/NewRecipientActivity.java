package de.cokuss.chhe.pinmoney;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;

public class NewRecipientActivity extends AppCompatActivity {

    //alle Felder
    Date startDatum;
    Date gebDatum;
    String kontoname;
    float betrag;
    Turnus turnus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipient);
    }
}
