package de.cokuss.chhe.pinmoney;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PinInfoActivity extends AppCompatActivity {
    private String inhaber;
    private TextView tvInhaber;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_info);
        init();
    }

    private void init () {
        inhaber =  getIntent().getStringExtra("inhaber");
        tvInhaber = (TextView) findViewById(R.id.inhaber);
        tvInhaber.setText(inhaber);
        //todo alles setzen
    }
}
