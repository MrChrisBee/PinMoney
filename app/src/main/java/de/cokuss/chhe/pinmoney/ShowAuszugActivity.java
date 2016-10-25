package de.cokuss.chhe.pinmoney;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class ShowAuszugActivity extends AppCompatActivity {

    private static final String LOG_TAG = ShowAuszugActivity.class.getSimpleName();
    DAOImplSQLight daoImplSQLight;
    String empfaengerStr;
    Spinner spinner;
    ArrayList<Konto> kontoList;
    ArrayList<String> nameList;
    ArrayList<Buchung> buchungsListe;
    TextView aktuell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_auszug);
        daoImplSQLight = DAOImplSQLight.getInstance(getApplicationContext());
        init();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher_account);
    }

    private void init() {
        if ((empfaengerStr = getIntent().getStringExtra("KontoName")).length() < 1) {
            Toaster(getResources().getString(R.string.kontoWaehlen));
            return;
        }
        spinner = (Spinner) findViewById(R.id.spinnerEmpfaenger);
        kontoList = daoImplSQLight.getAllKonten();
        aktuell = (TextView) findViewById(R.id.show_actual_count);
        nameList = new ArrayList<>();
        for (Konto konto : kontoList) {
            nameList.add(konto.getInhaber());
        }
        makeSpinnerAdapter();
        showIt();
        setListeners();


    }

    private void showIt() {
        // make a change via Spinner (see setListeners) possible
        spinner.setSelection(nameList.indexOf(empfaengerStr));
        buchungsListe = daoImplSQLight.getAllBuchungen(empfaengerStr);
        aktuell.setText(String.format(Locale.getDefault(), "%.2f", daoImplSQLight.getKontostand(empfaengerStr)));
        makeListViewAdapter();
    }

    private void makeSpinnerAdapter() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, nameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    private void makeListViewAdapter() {
        BuchungssatzAdapter buchungssatzAdapter = new BuchungssatzAdapter(this, buchungsListe);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.listViewKontoAuszug);
        listView.setAdapter(buchungssatzAdapter);
    }

    private void setListeners() {
        //make choosing possible
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.setSelection(position);
                String kontoinhaber = parent.getSelectedItem().toString();
                //selection changed?
                if (!kontoinhaber.equals(empfaengerStr)) {
                    empfaengerStr = kontoinhaber;
                    showIt();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                log(parent.getSelectedItem().toString());
            }
        });
    }

    private void log(String string) {
        Log.d(LOG_TAG, string);
    }

    private void Toaster(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }
}
//help from http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView