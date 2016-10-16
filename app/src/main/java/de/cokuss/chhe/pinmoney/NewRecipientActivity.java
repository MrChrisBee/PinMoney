package de.cokuss.chhe.pinmoney;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class NewRecipientActivity extends AppCompatActivity {
    private static final String LOG_TAG = NewRecipientActivity.class.getSimpleName();
    private DateHelper dateHelper = new DateHelper();
    private DAOImplSQLight daoImplSQLight;
    private Boolean isValid = true;
    private Konto konto;
    private Zahlungen zahlungen;
    //alle Felder
    private String kontoname;
    private Date gebDatum;
    private Turnus turnus;
    private float betrag;
    private float startBetrag;
    private Date startDatum;
    // die Views
    private EditText nameFeld;
    private EditText gebDatFeld;
    private RadioGroup turnusChecker;
    private EditText betragFeld;
    private EditText startBetragFeld;
    private EditText startDateFeld;
    private Button button, pickerGeb, pickerStart;
    private String tmpText;
    private final Calendar c = Calendar.getInstance();
    private int mYear = c.get(Calendar.YEAR);
    private int mMonth = c.get(Calendar.MONTH);
    private int mDay = c.get(Calendar.DAY_OF_MONTH);

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
        pickerGeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                gebDatFeld.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        pickerStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                startDateFeld.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

    }

    private void setViewVars() {
        button = (Button) findViewById(R.id.button_save_new_child);
        pickerGeb = (Button) findViewById(R.id.birthdayButton);
        pickerStart = (Button) findViewById(R.id.startButton);
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
        tmpC4 = checkEditText(nameFeld, "name");
        if (tmpC4.isValid()) {
            kontoname = tmpC4.getString();
            log("Kontoname gesetzt aus CreateNewKonto: " + kontoname);
        } else return;
        //gebDatum
        tmpC4 = checkEditText(gebDatFeld, "date");
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
        tmpC4 = checkEditText(betragFeld, "currency");
        if (tmpC4.isValid()) {
            betrag = Float.parseFloat(tmpC4.getString());
            log("betrag gesetzt aus CreateNewKonto: " + betrag);
        } else return;
        //startBetrag
        tmpC4 = checkEditText(startBetragFeld, "currency");
        if (tmpC4.isValid()) {
            startBetrag = Float.parseFloat(tmpC4.getString());
            log("startBetrag gesetzt aus CreateNewKonto: " + startBetrag);
        } else return;
        //startDatum
        tmpC4 = checkEditText(startDateFeld, "date");
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
                //erste Buchung mit Startbetrag
                Buchung buchung = new Buchung(null, null, startBetrag, "Neuanlage", null, null, startBetrag);
                daoImplSQLight.createBuchung(konto, buchung);
                //Eintrag in die History
                daoImplSQLight.addEntryToPinMoney(kontoname, zahlungen,"neu");
                log("createnew setPinmoney erstellt");
                this.finish();
            } else nameFeld.setError("Das Konto Existiert bereits!");
        } else nameFeld.setError("Der Kontoname ist ungültig!");
    }

    private Check4EditText checkEditText (EditText nameFeld, String kind) {
        String string = nameFeld.getText().toString();
        Check4EditText c4 = new Check4EditText(nameFeld,"",false);
        switch (kind.toLowerCase()){
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
                if (string.length() == 0 || !string.matches("^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[012])\\.(19|20)\\d\\d$")) {
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

    public Date string2Date(Check4EditText c4Thing) {
        Date date = null;
        if (c4Thing.isValid()) {
            try {
                date = dateHelper.sdfShort.parse(c4Thing.getString());
                log("string2Date Datum: " + date.toString());
            } catch (ParseException e) {
                c4Thing.getEditText().setError("Datum ist ungültig!");
                log("string2Date Ungültig");
            }
        }
        return date;
    }
    private void log(String string) {
        Log.d(LOG_TAG, string);
    }
}

