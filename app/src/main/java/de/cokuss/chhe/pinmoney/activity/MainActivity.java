package de.cokuss.chhe.pinmoney.activity;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

import java.util.ArrayList;

import de.cokuss.chhe.pinmoney.R;
import de.cokuss.chhe.pinmoney.fundamentals.Buchung;
import de.cokuss.chhe.pinmoney.DAOImplSQLight;
import de.cokuss.chhe.pinmoney.fundamentals.Konto;
import de.cokuss.chhe.pinmoney.help.HelpNewActivity_;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main_menu)
public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    ArrayList<Konto> kontenListe;
    //DAO
    DAOImplSQLight daoImplSQLight;
    //Alert 4 Delete
    AlertDialog.Builder alertBuilder;
    String KontoName;
    private Spinner accountName;
    private ArrayList<String> array4Adapter = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    //Konto
    private Konto selectedKonto;


    @AfterViews
    protected void init() {
        daoImplSQLight = DAOImplSQLight.getInstance(getApplicationContext());
        accountName = (Spinner) findViewById(R.id.spinner4Child);
        fillKontoSpinner();
        setListeners();
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher);
        }
    }

    @Click
    protected void button_konto(View v) {
        if (selectedKonto != null) {
            //in case of all Existing Entrys have been deleted
            if (daoImplSQLight.kontoExists(selectedKonto.getInhaber())) {
                //Todo delete this if working
                ShowAuszugActivity_.intent(v.getContext()).extra("KontoName", selectedKonto.getInhaber())
                        .extra("InOut", "In").start();
            }
        } else {
            Toast.makeText(MainActivity.this, "Bitte erst einen \nTaschengeldempfänger anlegen! \nÜber Settings \n  -> Empfänger \n  -> Neuer Empfänger", Toast.LENGTH_SHORT).show();
        }
    }

    @Click
    protected void button_auszahlung(View v) {
        if (selectedKonto != null) {
            BuchenActivity_.intent(v.getContext())
                    .extra("KontoName", selectedKonto.getInhaber()).extra("InOut", "Out").start();
        } else {
            Toast.makeText(MainActivity.this, "Bitte erst einen \nTaschengeldempfänger anlegen! \nÜber Settings \n  -> Empfänger \n  -> Neuer Empfänger", Toast.LENGTH_SHORT).show();
        }
    }


    @Click
    protected void button_einzahlung(View v) {
        if (selectedKonto != null) {
            BuchenActivity_.intent(v.getContext())
                    .extra("KontoName", selectedKonto.getInhaber()).extra("InOut", "In").start();
        } else {
            Toast.makeText(MainActivity.this, "Bitte erst einen \nTaschengeldempfänger anlegen! \nÜber Settings \n  -> Empfänger \n  -> Neuer Empfänger", Toast.LENGTH_SHORT).show();
        }
    }

    private void alertDialogDelete(final String konto) {
        alertBuilder = new AlertDialog.Builder(MainActivity.this);
        alertBuilder.setMessage("Soll das Konto " + konto + " wirklich gelöscht werden ?");
        alertBuilder.setCancelable(true);
        alertBuilder.setPositiveButton(
                "Ja",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        daoImplSQLight.deleteKonto(konto);
                        fillKontoSpinner();
                        daoImplSQLight.addEntryToPinMoney(konto, "deleted");
                        dialog.cancel();
                    }
                });

        alertBuilder.setNegativeButton(
                "Nein",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
    }

    private void alertDialogDoIt(final String konto, final Buchung buchung) {
        alertBuilder = new AlertDialog.Builder(MainActivity.this);
        alertBuilder.setMessage("Soll ich die Buchung für das Konto " + konto + " ausführen? Keine Angst das steht hier nur zu Test zwecken!");
        alertBuilder.setCancelable(true);
        alertBuilder.setPositiveButton(
                "Ja",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        daoImplSQLight.createBuchung(daoImplSQLight.getKonto(konto), buchung);
                        dialog.cancel();
                    }
                });

        alertBuilder.setNegativeButton(
                "Nein",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
    }

    private void fillKontoSpinner() {
        if (arrayAdapter != null) arrayAdapter.clear();
        kontenListe = daoImplSQLight.getAllKonten();
        for (Konto konto : kontenListe) {
            array4Adapter.add(konto.getInhaber());
        }
       //rem log("fillArray4Adapter array 4 adapter size: " + array4Adapter.size());
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array4Adapter);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountName.setAdapter(arrayAdapter);

    }


    private void setSelectedKonto() {
        String selectedName = accountName.getSelectedItem().toString();
        if (daoImplSQLight.isValidKontoName(selectedName)) {
            if (daoImplSQLight.kontoExists(selectedName)) {
                float kontostand = daoImplSQLight.getKontostand(selectedName);
                selectedKonto = new Konto(selectedName, kontostand);
                log("setSelectedKonto: " + selectedKonto.getInhaber() + " " + selectedKonto.getKontostand());
            }
        }
    }

    private void setListeners() {
        accountName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                log("onItemSelected");
                parent.setSelection(position);
                setSelectedKonto();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                log("onNothingSelected");
                parent.setSelection(0);
                setSelectedKonto();
            }
        });
    }


    @OptionsItem(R.id.action_help)
    void help() {
        HelpNewActivity_.intent(this).start();
    }

    @OptionsItem(R.id.action_close_app)
    void close() {
        finish();
    }

    @OptionsItem(R.id.action_new)
    void action_new() {
        NewRecipientActivity_.intent(this).start();
    }

    @OptionsItem(R.id.action_change)
    void action_change() {
        accountName.getSelectedItem();
        if (accountName.getSelectedItem() == null) {
            Toast.makeText(this, R.string.kontoWaehlen, Toast.LENGTH_LONG).show();
        } else {
            String testeMich = accountName.getSelectedItem().toString();
            if (testeMich == null || testeMich.length() < 1)
                log("Achtung : Wert für den Intent nicht gesetzt!");
            else
                ChangePinInfoActivity_.intent(this).extra("Inhaber", testeMich).start();
        }
    }

    @OptionsItem(R.id.action_delete)
    void action_delete() {
        ///Wurde etwas ausgewählt ?
        accountName.getSelectedItem();
        if (accountName.getSelectedItem() == null) {
            Toast.makeText(this, R.string.kontoWaehlen, Toast.LENGTH_LONG).show();
        } else {
            String loescheMich = accountName.getSelectedItem().toString();
            alertDialogDelete(loescheMich);
            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        }
    }

    @OptionsItem(R.id.action_show_history)
    void action_show_history() {
        ShowHistoryActivity_.intent(this).start();
    }

    @OptionsItem(R.id.action_test_the_booker)
    void action_test_the_booker() {
        //todo Zeige alle History Einträge an -> Auswahlmöglichkeit ?
        accountName.getSelectedItem();
        if (accountName.getSelectedItem() == null) {
            Toast.makeText(this, R.string.kontoWaehlen, Toast.LENGTH_LONG).show();
        } else {
            String testeMich = accountName.getSelectedItem().toString();
            //errechne das fällige Taschengeld
            Buchung buchung = daoImplSQLight.calcSavings(this, testeMich);
            if (buchung != null) {
                log(buchung.toString());
                alertDialogDoIt(testeMich, buchung);
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();
            } else log("Leere Buchung! ");
        }
    }

    private void log(String string) {
        Log.d(LOG_TAG, string);
    }

    @Override
    protected void onStart() {
        super.onStart();
        log("Quelle wird via onStart geoffnet.");
        daoImplSQLight.open();
    }

    @Override
    protected void onStop() {
        super.onStop();
        log("MainActivity wird via onStop geschlossen.");
        daoImplSQLight.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        log("Quelle wird via onDestroy geschlossen.");
        daoImplSQLight.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        log("Quelle wird via onPause geschlossen.");
        daoImplSQLight.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        log("Quelle wird via onResume geoffnet.");
        daoImplSQLight.open();
        fillKontoSpinner();
    }

}


// NewR\w+|Check\w+|DAO\w+|Pin\w+|MyDate\w+|MainActivity|Show\w+|Konto\w+
