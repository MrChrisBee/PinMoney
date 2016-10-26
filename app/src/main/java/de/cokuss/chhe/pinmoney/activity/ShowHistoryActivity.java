package de.cokuss.chhe.pinmoney.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import de.cokuss.chhe.pinmoney.DAOImplSQLight;
import de.cokuss.chhe.pinmoney.HistoryAdapter;
import de.cokuss.chhe.pinmoney.PinMoneyEntry;
import de.cokuss.chhe.pinmoney.R;

public class ShowHistoryActivity extends AppCompatActivity {
    private static final String LOG_TAG = ShowHistoryActivity.class.getSimpleName();
    DAOImplSQLight daoImplSQLight;
    ArrayList<PinMoneyEntry> historyList;

    //todo Zeige alle History Einträge an -> Auswahlmöglichkeit ?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);
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
        //befülle die anzuzeigende Liste
        historyList = daoImplSQLight.getEntryListFromPinMoney(getApplicationContext());
        log("HistoryList mit " + historyList.size() + " bekommen.");
        //nutze den History Adapter für die Anzeige
        makeListViewAdapter();
    }

    private void makeListViewAdapter () {
        HistoryAdapter historyAdapter = new HistoryAdapter(this, historyList);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.listViewHistoryEntrys);
        listView.setAdapter(historyAdapter);
    }

    private void log (String string) {
        Log.d(LOG_TAG, string);
    }

    private void Toaster (String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

}
