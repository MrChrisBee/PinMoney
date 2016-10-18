package de.cokuss.chhe.pinmoney;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ChangePinInfoActivity extends AppCompatActivity {
    private String inhaber;
    private TextView tvInhaber;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin_info);
        init();
    }

    private void init () {
        inhaber =  getIntent().getStringExtra("inhaber");
        tvInhaber = (TextView) findViewById(R.id.inhaber);
        tvInhaber.setText(inhaber);
        //todo alles setzen
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
