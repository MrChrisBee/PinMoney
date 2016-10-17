package de.cokuss.chhe.pinmoney;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;


public class BuchenActivity extends AppCompatActivity {
    private static final String LOG_TAG = BuchenActivity.class.getSimpleName();

    private void log (String string) {
        Log.d(LOG_TAG, string);
    }

    Konto empfaenger;
    String empfaengerStr, buchungstext;
    Boolean isEinzahlung;
    DAOImplSQLight daoImplSQLight;
    Buchung buchung;
    TextView header, kontoname, kontostand;
    EditText betrag, text;
    Button button;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buchen);
        header = (TextView) findViewById(R.id.textView);
        kontoname = (TextView) findViewById(R.id.empfaenger);
        kontostand = (TextView) findViewById(R.id.kontostand);
        betrag = (EditText) findViewById(R.id.betragBuchen);
        button = (Button) findViewById(R.id.button);
        text = (EditText) findViewById(R.id.eTextBuchen);

        daoImplSQLight = DAOImplSQLight.getInstance(getApplicationContext());
        empfaengerStr = getIntent().getStringExtra("KontoName");
        //über den Intend Parameter InOut wird entschieden ob hier ein oder ausgezahlt wird
        isEinzahlung = (getIntent().getStringExtra("InOut").equals("In"));
        if ((empfaenger = daoImplSQLight.getKonto(empfaengerStr)) == null) {
            Log.e(LOG_TAG, "onCreate: Das konto Existiert nicht!!!");
            finish();
        }
        if (isEinzahlung) {
            header.setText(getResources().getString(R.string.einzahlung_auf));
        } else {
            header.setText(getResources().getString(R.string.ausahlung_von));
        }
        kontoname.setText(empfaengerStr);
        //einen Kontostand eintragen
        kontostand.setText(String.format(Locale.getDefault(), "%.2f", empfaenger.getKontostand()));
        //betrag.setText(String.format(Locale.ENGLISH, "%.2f", 0f));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (betrag.getTextSize() < 1) {
                    Toast.makeText(v.getContext(), "Bitte einen gültigen Wert eingeben. \nDas Format ist €.Cent (7.50)", Toast.LENGTH_LONG).show();
                    return;
                }
                float wieviel = Float.valueOf(betrag.getText().toString());
                String wievielTxt = String.format(Locale.getDefault(), "%.2f", wieviel);
                if (isEinzahlung) {
                    Toast.makeText(v.getContext(), "Einzahlung " + wievielTxt + " €", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "Auszahlung " + wievielTxt + " €", Toast.LENGTH_SHORT).show();
                    wieviel = wieviel * -1;
                }
                if ((text.getText() == null) || (buchungstext = text.getText().toString()).length() < 1) {
                    if (isEinzahlung) {
                        buchungstext = "Einzahlung";
                    } else {
                        buchungstext = "Auszahlung";
                    }
                }
                //Achtung aktualisierung des Kontostandes findet nur hier statt, stelle sicher das dass Vorzeichen Stimmt
                buchung = new Buchung(null, null , wieviel, buchungstext, null, null, empfaenger.getKontostand() + wieviel);
                daoImplSQLight.createBuchung(empfaenger, buchung);

                //Von hier sollte es immer wieder zurück zu Main gehen.
                //finish macht nicht ganz das was der Nutzer erwarten würde, es ist immer der erste Eintrag der Liste ausgewählt

//                Intent intent = new Intent(v.getContext(), MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("kunde",empfaengerStr);
//                startActivity(intent);
                finish();
            }
        });
    }
}
