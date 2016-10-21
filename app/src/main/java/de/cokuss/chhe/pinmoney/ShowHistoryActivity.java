package de.cokuss.chhe.pinmoney;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowHistoryActivity extends AppCompatActivity {
    private static final String LOG_TAG = ShowHistoryActivity.class.getSimpleName();
    DAOImplSQLight daoImplSQLight;
    ArrayList<PinMoneyEnrty> historyList;

    //todo Zeige alle History Einträge an -> Auswahlmöglichkeit ?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);
        daoImplSQLight = DAOImplSQLight.getInstance(getApplicationContext());
        init();
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
