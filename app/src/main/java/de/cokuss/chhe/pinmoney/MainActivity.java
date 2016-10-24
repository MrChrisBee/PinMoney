package de.cokuss.chhe.pinmoney;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    ArrayList<Konto> kontenListe;
    //DAO
    DAOImplSQLight daoImplSQLight;
    //Alert 4 Delete
    AlertDialog.Builder alertBuilder;
    //Buttons bekannt machen
    private Button kontoauszug;
    private Button auszahlung;
    private Button einzahlung;
    private Spinner accountName;
    private ArrayList<String> array4Adapter = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    //Konto
    private Konto selectedKonto;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher_money);
    }

    private void init() {
        daoImplSQLight = DAOImplSQLight.getInstance(getApplicationContext());
        accountName = (Spinner) findViewById(R.id.spinner4Child);
        kontoauszug = (Button) findViewById(R.id.button_konto);
        auszahlung = (Button) findViewById(R.id.button_auszahlung);
        einzahlung = (Button) findViewById(R.id.button_einzahlung);
        fillKontoSpinner();
        setListeners();
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
                        daoImplSQLight.createBuchung(daoImplSQLight.getKonto(konto),buchung);
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
        log("fillArray4Adapter array 4 adapter size: " + array4Adapter.size());
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
        kontoauszug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedKonto != null) {
                    //in case of all Existing Entrys have been deleted
                    if (daoImplSQLight.kontoExists(selectedKonto.getInhaber())) {
                        Intent intent = new Intent(v.getContext(), ShowAuszugActivity.class);
                        intent.putExtra("KontoName", selectedKonto.getInhaber());
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Bitte erst einen \nTaschengeldempfänger anlegen! \nÜber Settings \n  -> Empfänger \n  -> Neuer Empfänger", Toast.LENGTH_SHORT).show();
                }
            }
        });

        einzahlung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedKonto != null) {
                    Intent intent = new Intent(v.getContext(), BuchenActivity.class);
                    intent.putExtra("KontoName", selectedKonto.getInhaber());
                    intent.putExtra("InOut", "In");
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Bitte erst einen \nTaschengeldempfänger anlegen! \nÜber Settings \n  -> Empfänger \n  -> Neuer Empfänger", Toast.LENGTH_SHORT).show();
                }
            }
        });
        auszahlung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedKonto != null) {
                    Intent intent = new Intent(v.getContext(), BuchenActivity.class);
                    intent.putExtra("KontoName", selectedKonto.getInhaber());
                    intent.putExtra("InOut", "Out");
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Bitte erst einen \nTaschengeldempfänger anlegen! \nÜber Settings \n  -> Empfänger \n  -> Neuer Empfänger", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                Intent intent1 = new Intent(this, HelpStartActivity.class);
                startActivity(intent1);
                return true;
            case R.id.action_new:
                //Intend für NewReciverActivity
                Intent intent2 = new Intent(this, NewRecipientActivity.class);
                startActivity(intent2);
                return true;
            case R.id.action_change:
                Intent intent3 = new Intent(this, ChangePinInfoActivity.class);
                intent3.putExtra("inhaber", selectedKonto.getInhaber());
                startActivity(intent3);
                return true;
            case R.id.action_delete:
                //Wurde etwas ausgewählt?
                accountName.getSelectedItem();
                if (accountName.getSelectedItem() == null) {
                    Toast.makeText(this, R.string.kontoWaehlen, Toast.LENGTH_LONG).show();
                    return false;
                }
                String loescheMich = accountName.getSelectedItem().toString();
                alertDialogDelete(loescheMich);
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();
                return true;
            case R.id.action_settings:
                //leider keine Zeit für Extras
                return true;
            case R.id.action_close_app:
                finish();
                return true;
            case R.id.action_show_history:
                //todo Zeige alle History Einträge an -> Auswahlmöglichkeit ?
                Intent intent4 = new Intent(this, ShowHistoryActivity.class);
                startActivity(intent4);
                return true;
            case R.id.action_test_the_booker:
                //todo Zeige alle History Einträge an -> Auswahlmöglichkeit ?
                accountName.getSelectedItem();
                if (accountName.getSelectedItem() == null) {
                    Toast.makeText(this, R.string.kontoWaehlen, Toast.LENGTH_LONG).show();
                    return false;
                }
                String testeMich = accountName.getSelectedItem().toString();
                //errechne das fällige Taschengeld
                Buchung buchung = daoImplSQLight.calcSavings(this,testeMich);
                if(buchung != null) {
                    log(buchung.toString());
                }else log("Leere Buchung! ");
                alertDialogDoIt(testeMich,buchung);
                alertDialog = alertBuilder.create();
                alertDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
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
// gut ArrayAdapter https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
// später: Bluetooth https://github.com/SoftdeveloperNeumann/Bluetooth.git funzt nicht sicher

//Links zu JUnit
//http://www.torsten-horn.de/techdocs/java-junit.htm
//http://www.tutego.de/blog/javainsel/2010/04/junit-4-tutorial-java-tests-mit-junit

// NewR\w+|Check\w+|DAO\w+|Pin\w+|MyDate\w+|MainActivity|Show\w+|Konto\w+
