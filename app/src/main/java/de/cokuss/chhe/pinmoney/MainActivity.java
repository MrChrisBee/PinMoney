package de.cokuss.chhe.pinmoney;

import android.content.Intent;
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

    //Buttons bekannt machen
    private Button kontoauszug;
    private Button auszahlung;
    private Button einzahlung;
    private Spinner accountName;
    ArrayList<Konto> kontenListe;
    private ArrayList<String> array4Adapter = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    //DAO
    DAOImplSQLight daoImplSQLight;
    //Konto
    private Konto selectedKonto;
    private String selectedName;
    private float kontostand;

    //savedInstanceState verlassen der View aktualisiert
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
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

    private void fillKontoSpinner() {
        if (arrayAdapter != null) arrayAdapter.clear();
        kontenListe = daoImplSQLight.getAllKonten();
        for (Konto konto : kontenListe) {
            array4Adapter.add(konto.getInhaber());
        }
        log("fillArray4Adapter array 4 adapter size: " + array4Adapter.size());
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array4Adapter);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountName.setAdapter(arrayAdapter);
    }


    private void setSelectedKonto() {
        String tmpString;
        tmpString = accountName.getSelectedItem().toString();
        if (daoImplSQLight.isValidKontoName(tmpString)) {
            if (daoImplSQLight.kontoExists(tmpString)) {
                selectedName = tmpString;
                kontostand = daoImplSQLight.getKontostand(tmpString);
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
                    Intent intent = new Intent(v.getContext(), ShowAuszugActivity.class);
                    intent.putExtra("KontoName", selectedKonto.getInhaber());
                    startActivity(intent);
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
                parent.setSelection(position);
                setSelectedKonto();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(0);
                setSelectedKonto();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                Intent intent1 = new Intent(this, HelpActivity.class);
                startActivity(intent1);
                return true;
            case R.id.action_new:
                //Intend für NewReciverActivity
                Intent intent2 = new Intent(this, NewRecipientActivity.class);
                startActivity(intent2);
                return true;
            case R.id.action_change:
                //Todo Check auf Auswahl und Dialog (Fragment) zur Änderung von Zahlungen
                //Taschengeld erhöhung fällt erstmal aus - leider keine Zeit für Extras
                return true;
            case R.id.action_delete:
                //Wurde etwas ausgewählt?
                accountName.getSelectedItem();
                if (accountName.getSelectedItem() == null) {
                    Toast.makeText(this, R.string.kontoWaehlen, Toast.LENGTH_LONG).show();
                    return false;
                }
                String loescheMich = accountName.getSelectedItem().toString();
                //in loescheMich sollte ein gültiger Eintrag sein
                daoImplSQLight.deleteKonto(loescheMich);
                fillKontoSpinner();
                daoImplSQLight.addEntryToPinMoney(loescheMich,"deleted");
                return true;
            case R.id.action_settings:
                //leider keine Zeit für Extras
                return true;
            case R.id.action_close_app:
                finish();
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
// später: Scool Bluetooth https://github.com/SoftdeveloperNeumann/Bluetooth.git funzt nicht sicher
//http://www.journaldev.com/9976/android-date-time-picker-dialog

//Links zu JUnit
//http://www.torsten-horn.de/techdocs/java-junit.htm
//http://www.tutego.de/blog/javainsel/2010/04/junit-4-tutorial-java-tests-mit-junit

