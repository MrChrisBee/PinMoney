package de.cokuss.chhe.pinmoney;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;


public class ShowAuszugActivity extends AppCompatActivity {

    private static final String LOG_TAG = ShowAuszugActivity.class.getSimpleName();
    DAOImplSQLight daoImplSQLight;
    String empfaengerStr;
    Spinner spinner;
    ArrayList<Konto> kontoList;
    ArrayList<String> nameList;
    ArrayList<Buchung> buchungsListe;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_auszug);
        daoImplSQLight = DAOImplSQLight.getInstance(getApplicationContext());
        init();
    }

    private void init () {
        if ((empfaengerStr = getIntent().getStringExtra("KontoName")).length() < 1) {
            Toaster(getResources().getString(R.string.kontoWaehlen));
            return;
        }
        spinner = (Spinner) findViewById(R.id.spinnerEmpfaenger);
        kontoList = daoImplSQLight.getAllKonten();
        nameList = new ArrayList<>();
        for (Konto konto : kontoList) {
            nameList.add(konto.getInhaber());
        }
        makeSpinnerAdapter();
        spinner.setSelection(nameList.indexOf(empfaengerStr));
        buchungsListe = daoImplSQLight.getAllBuchungen(empfaengerStr);
        makeListViewAdapter();
    }

    private void makeSpinnerAdapter () {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, nameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }

    private void makeListViewAdapter () {
        BuchungssatzAdapter buchungssatzAdapter = new BuchungssatzAdapter(this,buchungsListe);
        // Attach the adapter to a ListView

        ListView listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(buchungssatzAdapter);
    }

    private void log (String string) {
        Log.d(LOG_TAG, string);
    }

    private void Toaster (String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }
}


//Todo http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView