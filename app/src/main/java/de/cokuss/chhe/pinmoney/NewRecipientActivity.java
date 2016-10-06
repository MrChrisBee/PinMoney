package de.cokuss.chhe.pinmoney;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        tmpC4 = checkEditText(nameFeld, "Name");
        if (tmpC4.isValid()) {
            kontoname = tmpC4.getString();
            log("Kontoname gesetzt aus CreateNewKonto: " + kontoname);
        } else return;
        //gebDatum
        tmpC4 = checkEditText(gebDatFeld, "Name");
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
        tmpC4 = checkEditText(betragFeld, "Name");
        if (tmpC4.isValid()) {
            betrag = Float.parseFloat(tmpC4.getString());
            log("betrag gesetzt aus CreateNewKonto: " + betrag);
        } else return;
        //startBetrag
        tmpC4 = checkEditText(startBetragFeld, "Name");
        if (tmpC4.isValid()) {
            startBetrag = Float.parseFloat(tmpC4.getString());
            log("startBetrag gesetzt aus CreateNewKonto: " + startBetrag);
        } else return;
        //startDatum
        tmpC4 = checkEditText(startDateFeld, "Datum");
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
                daoImplSQLight.addEntryToPinMoney(kontoname, zahlungen,"neu");
                log("createnew setPinmoney erstellt");
                this.finish();
            } else nameFeld.setError("Das Konto Existiert bereits!");
        } else nameFeld.setError("Der Kontoname ist ungültig!");
    }

    private Check4EditText checkEditText(EditText nameFeld, String art) {
        String string = nameFeld.getText().toString();
        Check4EditText c4;
        switch (art){
            case "Name":
                break;
            case "Datum":
                break;
        }
        if (string.length() == 0 || !string.matches("\\w+")) {
            nameFeld.setError("Bitte einen Wert eingeben! Für Namen nur (A-Za-z0-9_) nuzten!");
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