package de.cokuss.chhe.pinmoney;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipient);
        init();
    }

    private void init () {
        daoImplSQLight = new DAOImplSQLight(this);
        setViewVars();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                createNewKonto();
            }
        });
    }

    private void setViewVars () {
        button = (Button) findViewById(R.id.button_save_new_child);
        nameFeld = (EditText) findViewById(R.id.input_name);
        gebDatFeld = (EditText) findViewById(R.id.input_birthday);
        turnusChecker = (RadioGroup) findViewById(R.id.turnus);
        betragFeld = (EditText) findViewById(R.id.betrag);
        startBetragFeld = (EditText) findViewById(R.id.startBetrag);
        startDateFeld = (EditText) findViewById(R.id.input_startDate);
    }

    private void createNewKonto () {
        kontoname = checkEditText(nameFeld);
        gebDatum = string2Date(checkEditText(gebDatFeld));
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
        betrag = Float.parseFloat(checkEditText(betragFeld));
        startBetrag = Float.parseFloat(checkEditText(startBetragFeld));
        startDatum = string2Date(checkEditText(startDateFeld));
        if (isValid) {
            if (daoImplSQLight.isValidKontoName(kontoname)) {
                if (!daoImplSQLight.kontoExists(kontoname)) {
                    //Zahlung erstellen
                    zahlungen = new Zahlungen(startDatum,turnus,betrag);
                    //Konto erstellen
                    konto = new Konto(kontoname,startBetrag);
                    daoImplSQLight.createKonto(konto);
                    daoImplSQLight.setPinMoney(konto,zahlungen);
                } else kontoExists();
            } else kontoNameUngueltig();
        }
    }

    private String checkEditText (EditText nameFeld) {
        String string = nameFeld.getText().toString();
        if (string.length() == 0) {
            nameFeld.setError("Bitte einen Wert eingeben!");
            isValid = false;
        }
        return string;
    }

    private void kontoNameUngueltig () {
        nameFeld.setError("Der Kontoname ist ungültig!");
    }

    private void kontoExists () {
        nameFeld.setError("Das Konto Existiert bereits!");
    }

    public Date string2Date (String string) {
        Date date = null;
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", new Locale("de"));
        try {
            date = formatter.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
