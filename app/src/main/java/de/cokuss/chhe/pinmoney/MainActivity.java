package de.cokuss.chhe.pinmoney;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String viewInfo;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // welche Version der App nutzen wir
        if (savedInstanceState == null) {
            // nur beim ersten Start first_start anzeigen
            savedInstanceState.putString("inView", "first_start");
            setContentView(R.layout.first_start);
        }

        // dann nach
        if ((viewInfo = (String) savedInstanceState.get("inView")) != null) {
            switch (viewInfo) {
                case "main_eltern":
                    setContentView(R.layout.main_eltern);
                    break;
            }

        } else {

        }

        Button b = (Button) findViewById(R.id.button_parents);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // über Bundle festhalten wo wir sind
                savedInstanceState.putString("inView", "main_eltern");
                setContentView(R.layout.main_eltern);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

}
