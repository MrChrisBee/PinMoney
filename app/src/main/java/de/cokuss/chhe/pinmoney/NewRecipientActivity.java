package de.cokuss.chhe.pinmoney;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewRecipientActivity extends AppCompatActivity {
    private static final String LOG_TAG = NewRecipientActivity.class.getSimpleName();

    DAOImplSQLight daoImplSQLight;
    Boolean isValid = true;
    Konto konto;
    Zahlungen zahlungen;
    //alle Felder
    String kontoname;
    Date gebDatum;
    Turnus turnus;
    float betrag;
    float startBetrag;
    Date startDatum;
    // die Views
    EditText nameFeld;
    EditText gebDatFeld;
    RadioGroup turnusChecker;
    EditText betragFeld;
    EditText startBetragFeld;
    EditText startDateFeld;
    Button button;
    String tmpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipient);
        init();
    }

    private void init() {
        daoImplSQLight = DAOImplSQLight.getInstance(getApplicationContext());
        setViewVars();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewKonto();
            }
        });
    }

    private void setViewVars() {
        button = (Button) findViewById(R.id.button_save_new_child);
        nameFeld = (EditText) findViewById(R.id.input_name);
        gebDatFeld = (EditText) findViewById(R.id.input_birthday);
        turnusChecker = (RadioGroup) findViewById(R.id.turnus);
        betragFeld = (EditText) findViewById(R.id.betrag);
        startBetragFeld = (EditText) findViewById(R.id.startBetrag);
        startDateFeld = (EditText) findViewById(R.id.input_startDate);
    }

    private void createNewKonto() {
        Check4EditText tmpC4;
        //kontoname
        tmpC4 = checkEditText(nameFeld);
        if (tmpC4.isValid()) {
            kontoname = tmpC4.getString();
            log("Kontoname gesetzt aus CreateNewKonto: " + kontoname);
        } else return;
        //gebDatum
        tmpC4 = checkEditText(gebDatFeld);
        if (tmpC4.isValid()) {
            gebDatum = string2Date(tmpC4);
            log("gebDatum gesetzt aus CreateNewKonto: " + gebDatum.toString());
        } else return;
        switch (turnusChecker.getCheckedRadioButtonId()) {
            case R.id.radioButton_dayli:
                turnus = Turnus.TAEGLICH;
                break;
            case R.id.radioButton_weekly:
                turnus = Turnus.WOECHENTLICH;
                break;
            case R.id.radioButton_monthly:
                turnus = Turnus.MONATLICH;
                break;
        }
        //betrag
        tmpC4 = checkEditText(betragFeld);
        if (tmpC4.isValid()) {
            betrag = Float.parseFloat(tmpC4.getString());
            log("betrag gesetzt aus CreateNewKonto: " + betrag);
        } else return;
        //startBetrag
        tmpC4 = checkEditText(startBetragFeld);
        if (tmpC4.isValid()) {
            startBetrag = Float.parseFloat(tmpC4.getString());
            log("startBetrag gesetzt aus CreateNewKonto: " + startBetrag);
        } else return;
        //startDatum
        tmpC4 = checkEditText(startDateFeld);
        if (tmpC4.isValid()) {
            startDatum = string2Date(tmpC4);
            log("startDatum gesetzt aus CreateNewKonto: " + startDatum.toString());
        } else return;
        if (daoImplSQLight.isValidKontoName(kontoname)) {
            if (!daoImplSQLight.kontoExists(kontoname)) {
                //Zahlung erstellen
                zahlungen = new Zahlungen(startDatum, turnus, betrag);
                //Konto erstellen
                konto = new Konto(kontoname, startBetrag);
                daoImplSQLight.createKonto(konto);
                log("createnew Konto erstellt");
                daoImplSQLight.setPinMoney(konto, zahlungen);
                log("createnew setPinmoney erstellt");
                this.finish();
            } else nameFeld.setError("Das Konto Existiert bereits!");
        } else nameFeld.setError("Der Kontoname ist ungültig!");
    }

    private Check4EditText checkEditText(EditText nameFeld) {
        String string = nameFeld.getText().toString();
        Check4EditText c4;
        if (string.length() == 0) {
            nameFeld.setError("Bitte einen Wert eingeben!");
            c4 = new Check4EditText(nameFeld, "", false);
            log("Feld leer aus C4ET für " + nameFeld.toString());
        } else {
            //erster Test bestanden
            c4 = new Check4EditText(nameFeld, string, true);
            log("Test Bestanden aus C4ET " + nameFeld + " enthält " + c4.getString());
        }
        return c4;
    }

    public Date string2Date(Check4EditText c4Thing) {
        Date date = null;
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", new Locale("de"));
        if (c4Thing.isValid()) {
            try {
                date = formatter.parse(c4Thing.getString());
                log("klappte aus string2Date" + date.toString());
            } catch (ParseException e) {
                c4Thing.getEditText().setError("Datum ist ungültig!");
                log("ungültig aus string2Date");
            }
        }
        return date;
    }
    private void log(String string) {
        Log.d(LOG_TAG, string);
    }
}

class Check4EditText {
    private String string;
    private boolean valid;
    private EditText editText;

    Check4EditText(EditText editText, String string, boolean aBoolean) {
        this.editText = editText;
        this.string = string;
        this.valid = aBoolean;
    }

    //Check4Edit enthält deswegen ein EditText um in string2Date möglicherweise darauf einen Fehlertext zu legen
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