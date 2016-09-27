package de.cokuss.chhe.pinmoney;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class ShowAuszugActivity extends AppCompatActivity {

    private static final String LOG_TAG = ShowAuszugActivity.class.getSimpleName();
    String[] parameter;
    String parameterString;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_auszug);
        init();
    }

    private void init() {
        spinner = (Spinner) findViewById(R.id.spinnerEmpfaenger);
        Bundle extras = getIntent().getExtras();
        Intent intent = getIntent();
        parameter = intent.getStringArrayExtra("apapterArray");
        if (parameter != null) {
            log(parameter[0]);
            makeAdapter();
        }
    }

    private void makeAdapter() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, parameter);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    private void log(String string) {
        Log.d(LOG_TAG, string);
    }
}
