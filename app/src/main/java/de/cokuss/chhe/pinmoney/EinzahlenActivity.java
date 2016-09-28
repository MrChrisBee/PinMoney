package de.cokuss.chhe.pinmoney;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EinzahlenActivity extends AppCompatActivity {
    Konto empfaenger;
    DAOImplSQLight daoImplSQLight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_einzahlen);
        daoImplSQLight = DAOImplSQLight.getInstance(getApplicationContext());
    }

    //Todo den Intend auswerten und entsprechend ...

    //String s= getIntent().getStringExtra(<StringName>);
}
