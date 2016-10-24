package de.cokuss.chhe.pinmoney;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Locale;

public class ChangePinInfoActivity extends AppCompatActivity {
    private static final String LOG_TAG = ChangePinInfoActivity.class.getSimpleName();
    DateHelper dateHelper = new DateHelper();
    private String inhaber;
    private TextView tvInhaber;
    private PinMoneyEntry pinMoneyEntry;
    private DAOImplSQLight daoImplSQLight = DAOImplSQLight.getInstance(ChangePinInfoActivity.this);
    private Payments payments;
    private EditText betrag, changeDate;
    private RadioButton radioDay;
    private RadioButton radioWeek;
    private RadioButton radioMonth;

    private void log(String string) {
        Log.d(LOG_TAG, string);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin_info);
        betrag = (EditText) findViewById(R.id.betragEdit);
        changeDate = (EditText) findViewById(R.id.input_changeDate);
        radioDay = (RadioButton) findViewById(R.id.rbDay);
        radioWeek = (RadioButton) findViewById(R.id.rbWeek);
        radioMonth = (RadioButton) findViewById(R.id.rbMonth);
        init();
    }

    private void init() {
        inhaber = getIntent().getStringExtra("inhaber");
        tvInhaber = (TextView) findViewById(R.id.inhaber);
        tvInhaber.setText(inhaber);
        showState();
        //todo änderungen erkennen
        //todo den Ändern Button freischalten und den passenden Listener setzen
    }

    private void showState() {
        pinMoneyEntry = daoImplSQLight.getEntryFromPinMoney(getApplicationContext(), inhaber);
        payments = pinMoneyEntry.getPayments();
        betrag.setText(String.format(Locale.GERMAN, "%.2f", payments.getBetrag()));
        log("ShowState " + payments.getDate());
        changeDate.setText(dateHelper.sdfShort.format(payments.getDate()));
        switch (payments.getTurnusStrShort()) {
            case "t":
                radioDay.setChecked(true);
                break;
            case "w":
                radioWeek.setChecked(true);
                break;
            case "m":
                radioMonth.setChecked(true);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu_change_pin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help_change_pin:
                Intent intent1 = new Intent(this, HelpChangePinActivity.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
