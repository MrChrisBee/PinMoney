package de.cokuss.chhe.pinmoney.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.AfterViews;

import java.util.Locale;

import de.cokuss.chhe.pinmoney.fundamentals.Buchung;
import de.cokuss.chhe.pinmoney.Check4EditText;
import de.cokuss.chhe.pinmoney.DAOImplSQLight;
import de.cokuss.chhe.pinmoney.fundamentals.Konto;
import de.cokuss.chhe.pinmoney.R;
import de.cokuss.chhe.pinmoney.help.HelpBookingActivity;
import de.cokuss.chhe.pinmoney.help.HelpBookingActivity_;

@EActivity(R.layout.activity_buchen)
public class BuchenActivity extends AppCompatActivity {
    private static final String LOG_TAG = BuchenActivity.class.getSimpleName();
    Konto empfaenger;
    String empfaengerStr, buchungstext;
    Boolean isEinzahlung;
    DAOImplSQLight daoImplSQLight;
    Buchung buchung;
    @ViewById
    TextView kontoname, kontostand, header;
    @ViewById
    EditText betrag, text;

    private void log(String string) {
        Log.d(LOG_TAG, string);
    }

    @Click
    protected void button(View v) {
        if (betrag.getTextSize() < 1) {
            Toast.makeText(v.getContext(), "Bitte einen gültigen Wert eingeben. \nDas Format ist €.Cent (7.50)", Toast.LENGTH_LONG).show();
            return;
        }
        Check4EditText tmpC4;
        float wieviel;
        tmpC4 = Check4EditText.checkEditText(betrag, "currency");
        if (tmpC4.isValid()) {
            wieviel = Float.valueOf(tmpC4.getString());
        } else return;
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
        //Achtung aktualisierung des Kontostandes findet nur hier statt, stelle sicher dass das Vorzeichen Stimmt
        buchung = new Buchung(null, null, wieviel, buchungstext, null, null, empfaenger.getKontostand() + wieviel);
        daoImplSQLight.createBuchung(empfaenger, buchung);
        ShowAuszugActivity_.intent(this).extra("KontoName", empfaengerStr).start();
    }


    @AfterViews
    protected void init() {
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


        initToolbar();
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher_booking);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu_booking, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help_booking:
                Intent intent1 = new Intent(this, HelpBookingActivity_.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
