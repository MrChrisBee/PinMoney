package de.cokuss.chhe.pinmoney;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    //Buttons bekannt machen
    private  Button kontoauszug;
    private  Button auszahlung;
    private  Button einzahlung;
    Spinner accountName;
    ArrayList<Konto> kontenListe;
    private ArrayList<String> array4Adapter = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    //DAO
    DAOImplSQLight daoImplSQLight;
    //Konto
    private Konto selectedKonto;

    //hier wird das Bundle (savedInstanceState) beim
    // verlassen der View aktualisiert
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //vielleicht brauche ich es noch
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
        setListeners();
        fillKontoSpinner();
    }

    private void fillKontoSpinner(){
        kontenListe = daoImplSQLight.getAllKonten();
        for(Konto konto: kontenListe) {
            array4Adapter.add(konto.getInhaber());
        }
        if (array4Adapter != null) {
            arrayAdapter.clear();
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array4Adapter);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            accountName.setAdapter(arrayAdapter);
        }
    }

    private void setListeners() {
        kontoauszug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShowAuszugActivity.class);
                intent.putStringArrayListExtra("apapterArray", array4Adapter);
                startActivity(intent);
            }
        });
        einzahlung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent myintent=new Intent(Info.this, GraphDiag.class).putExtra("<StringName>", value);
                //startActivity(myintent);

                Intent intent = new Intent(v.getContext(), EinzahlenActivity.class);

                //Todo selectedKonto setzen
                intent.putExtra("KontoName", selectedKonto);
                startActivity(intent);
            }
        });
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_new:
                //Intend für NewReciverActivity
                Intent intent = new Intent(this, NewRecipientActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_change:
                //  Todo Check auf Auswahl und Dialog (Fragment) zur Änderung von Zahlungen
                return true;
            case R.id.action_delete:
                //Todo Check auf Auswahl und delete
                return true;
            case R.id.action_settings:
                //leider keine Zeit für Extras
                return true;
            case R.id.action_close_app:
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

//    @Override
//    protected void onStop() {
//        super.onStop();
//        log("Quelle wird via onStop geschlossen.");
//        daoImplSQLight.close();
//    } wird bei jedem Intend in andere App gerufen, möchte ich dann die DB schliessen, sicher nicht

    @Override
    protected void onDestroy() {
        super.onDestroy();
        log("Quelle wird via onDestroy geschlossen.");
        daoImplSQLight.close();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        log("Quelle wird via onPause geschlossen.");
//        daoImplSQLight.close();
//    }  wird bei jedem Intend in andere App gerufen, möchte ich dann die DB schliessen, sicher nicht

    @Override
    protected void onResume() {
        super.onResume();
        log("Quelle wird via onResume geoffnet.");
        daoImplSQLight.open();
        fillKontoSpinner();
    }
}
// ArrayAdapter https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView