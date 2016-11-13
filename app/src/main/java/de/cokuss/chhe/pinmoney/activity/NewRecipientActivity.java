package de.cokuss.chhe.pinmoney.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;
import java.util.Date;

import de.cokuss.chhe.pinmoney.fundamentals.Buchung;
import de.cokuss.chhe.pinmoney.Check4EditText;
import de.cokuss.chhe.pinmoney.fundamentals.Cycle;
import de.cokuss.chhe.pinmoney.DAOImplSQLight;
import de.cokuss.chhe.pinmoney.DateHelper;
import de.cokuss.chhe.pinmoney.fundamentals.Konto;
import de.cokuss.chhe.pinmoney.fundamentals.Payments;
import de.cokuss.chhe.pinmoney.R;
import de.cokuss.chhe.pinmoney.help.HelpNewActivity_;


@OptionsMenu(R.menu.main_menu_new_recipient)
@EActivity(R.layout.activity_new_recipient)
public class NewRecipientActivity extends AppCompatActivity {
    private static final String LOG_TAG = NewRecipientActivity.class.getSimpleName();
    //private Button button, pickerGeb, pickerStart;
    private final Calendar c = Calendar.getInstance();
    // die Views
    @ViewById(R.id.input_name)
    EditText nameField;
    @ViewById(R.id.input_birthday)
    EditText gebDatField;
    @ViewById(R.id.cycle)
    RadioGroup cycleChecker;
    @ViewById(R.id.betrag)
    EditText valueField;
    @ViewById(R.id.startBetrag)
    EditText startValueField;
    @ViewById(R.id.start_date)
    EditText startDateField;

    private DateHelper dateHelper = new DateHelper();
    private DAOImplSQLight daoImplSQLight;
    private int mYear = c.get(Calendar.YEAR);
    private int mMonth = c.get(Calendar.MONTH);
    private int mDay = c.get(Calendar.DAY_OF_MONTH);

    @AfterViews
    void init() {
        daoImplSQLight = DAOImplSQLight.getInstance(getApplicationContext());
        initToolbar();
    }

    void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher_new);
    }

    @Click(R.id.button_save_new_child)
    void button() {
        createNewKonto();
    }


    @Click(R.id.birthdayButton)
    void pickerGeb(View v) {

    }

    @Click(R.id.showCalendar)
    void pickerStart(View v) {

    }

//    @Click(R.id.birthdayButton)
//    void pickerGeb(View v) {
//        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//                        gebDatField.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
//                    }
//                }, mYear, mMonth, mDay);
//        datePickerDialog.show();
//    }
//
//    @Click(R.id.showCalendar)
//    void pickerStart(View v) {
//        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//                        startDateField.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
//                    }
//                }, mYear, mMonth, mDay);
//        datePickerDialog.show();
//    }

    private void createNewKonto() {
        //Todo Veryfi via Annotation ???
        Check4EditText tmpC4;
        //kontoname
        tmpC4 = Check4EditText.checkEditText(nameField, "name");
        String kontoname;
        if (tmpC4.isValid()) {
            kontoname = tmpC4.getString();
        } else return;
        //gebDatum
        tmpC4 = Check4EditText.checkEditText(gebDatField, "date");
        Date gebDatum;
        if (tmpC4.isValid()) {
            gebDatum = dateHelper.string2Date(tmpC4);
        } else return;
        Cycle cycle;
        switch (cycleChecker.getCheckedRadioButtonId()) {
            case R.id.radioButton_dayli:
                cycle = Cycle.TAEGLICH;
                break;
            case R.id.radioButton_weekly:
                cycle = Cycle.WOECHENTLICH;
                break;
            case R.id.radioButton_monthly:
                cycle = Cycle.MONATLICH;
                break;
            default: //einer der Werte sollte es sein
                return;
        }
        //betrag
        tmpC4 = Check4EditText.checkEditText(valueField, "currency");
        float betrag;
        if (tmpC4.isValid()) {
            betrag = Float.parseFloat(tmpC4.getString());
        } else return;
        //startBetrag
        tmpC4 = Check4EditText.checkEditText(startValueField, "currency");
        float startBetrag;
        if (tmpC4.isValid()) {
            startBetrag = Float.parseFloat(tmpC4.getString());
        } else return;
        //startDatum
        tmpC4 = Check4EditText.checkEditText(startDateField, "date");
        Date startDatum;
        if (tmpC4.isValid()) {
            startDatum = dateHelper.string2Date(tmpC4);
        } else return;
        if (daoImplSQLight.isValidKontoName(kontoname)) {
            if (!daoImplSQLight.kontoExists(kontoname)) {
                //Zahlung erstellen
                Payments payments = new Payments(startDatum, cycle, betrag);
                //Konto erstellen
                Konto konto = new Konto(kontoname, startBetrag);
                daoImplSQLight.createKonto(konto);
                //erste Buchung mit Startbetrag
                Buchung buchung = new Buchung(null, null, startBetrag, "Neuanlage", null, null, startBetrag);
                daoImplSQLight.createBuchung(konto, buchung);
                //Eintrag in die History
                daoImplSQLight.addEntryToPinMoney(kontoname, gebDatum, payments, "neu");
                this.finish();
            } else nameField.setError("Das Konto Existiert bereits!");
        } else nameField.setError("Der Kontoname ist ungültig!");
    }

    @OptionsItem(R.id.action_help_new_recipient)
    void helper() {
        HelpNewActivity_.intent(this).start();
    }

    private void log(String string) {
        Log.d(LOG_TAG, string);
    }
}

