package de.cokuss.chhe.pinmoney;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.util.Calendar;

import java.util.Locale;


public class BuchenActivity extends AppCompatActivity {
    private static final String LOG_TAG = BuchenActivity.class.getSimpleName();

    private void log(String string) {Log.d(LOG_TAG, string);}

    Konto empfaenger;
    Buchung buchung;
    String empfaengerStr, buchungstext;
    Boolean isEinzahlung;
    DAOImplSQLight daoImplSQLight;

    TextView header, kontoname, kontostand;
    EditText betrag, text;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buchen);
        header = (TextView) findViewById(R.id.textView);
        kontoname = (TextView) findViewById(R.id.empfaenger);
        kontostand = (TextView) findViewById(R.id.kontostand);
        DecimalKeyListener decimalKeyListener = new DecimalKeyListener();
        betrag = (EditText) findViewById(R.id.betragBuchen);
        //betrag.setKeyListener(decimalKeyListener);
        text = (EditText) findViewById(R.id.eTextBuchen);
        button = (Button) findViewById(R.id.button);

        daoImplSQLight = DAOImplSQLight.getInstance(getApplicationContext());
        empfaengerStr = getIntent().getStringExtra("KontoName");
        isEinzahlung = (getIntent().getStringExtra("InOut").equals("In"));
        if ((empfaenger = daoImplSQLight.getKonto(empfaengerStr)) == null) {
            Log.e(LOG_TAG,"onCreate: Das konto Existiert nicht!!!");
            finish();
        }
        if (isEinzahlung) {
            header.setText(getResources().getString(R.string.einzahlung_auf));
        } else {
            header.setText(getResources().getString(R.string.ausahlung_von));
        }
        kontoname.setText(empfaengerStr);
        //einen Kontostand eintragen
        kontostand.setText(String.format(Locale.getDefault(),"%.2f", empfaenger.getKontostand()));
        betrag.setText(String.format(Locale.ENGLISH,"%.2f", 0f));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(betrag.getTextSize() < 1) {
                    Toast.makeText(v.getContext(),"Bitte einen gültigen Wert eingeben. Das Format ist €.Cent (7.50)",Toast.LENGTH_LONG).show();
                    return;
                }
                float wieviel = Float.valueOf(betrag.getText().toString());
                String wievielTxt = String.format(Locale.getDefault(),"%.2f", wieviel);
                if (isEinzahlung) {
                    Toast.makeText(v.getContext(),"Das ist ja Super "+ wievielTxt + " € die siehst Du nie wieder !",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(v.getContext(),"Das währe ja noch schöner Du willst "+ wievielTxt + " €. Keine Chance ich bin Pleite",Toast.LENGTH_LONG).show();
                }

                //Buchung erzeugen ich brauche Buchung(Long id,Konto konto, Date date, float value, String text, Long verifi_id, Integer veri_type)
                //Fehlen noch : Text, aktueller Kontostand(alter + wieviel),
                //verify_typ und verify_id werden erstmal getürkt
                if((text.getText() == null) || (buchungstext = text.getText().toString()).length() <1){
                    if(isEinzahlung) {
                        buchungstext = "Einzahlung";
                    } else {
                        buchungstext = "Auszahlung";
                    }
                }
                buchung = new Buchung(null,empfaenger,Calendar.getInstance().getTime(),wieviel,buchungstext,null,0);
                daoImplSQLight.createBuchung(buchung);
            }
        });
    }
}
