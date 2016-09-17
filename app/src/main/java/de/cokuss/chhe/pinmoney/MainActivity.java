package de.cokuss.chhe.pinmoney;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String viewInfo = "first_start";
    SPHelper spHelper;

    //hier wird das Bundle (savedInstanceState) beim
    // verlassen der View aktualisiert
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("onSaveInstanceState");
        outState.putString("inView", viewInfo);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // welche Version der App nutzen wir
        if (savedInstanceState != null) {
            //gesicherte view aus dem Bundle holen
            viewInfo = (String) savedInstanceState.get("inView");
            switch (viewInfo) {
                case "main_eltern":
                    setContentView(R.layout.main_eltern);
                    break;
                case "empfaenger_neu":
                    setContentView(R.layout.empfaenger_neu);
                    break;
                case "main_kinder":
                    setContentView(R.layout.main_kinder);
                    break;
                case "first_start":
                    setContentView(R.layout.first_start);
                    registerFirstStart();
                    break;
            }
        } else {
            setContentView(R.layout.first_start);
            registerFirstStart();
        }
        spHelper = new SPHelper("AppData", "Kein Wert hinterlegt");
    }

    private void registerFirstStart() {
        Button buttonParents = (Button) findViewById(R.id.button_parents);
        buttonParents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // fuer Bundle festhalten wo wir sind
                // elternversion setzen ? Dialog mit OK und zurück Button
                viewInfo = "empfaenger_neu";
                spHelper.safeString("AppVersion", "Zahler");
                setContentView(R.layout.empfaenger_neu);
            }
        });

        Button buttonChild = (Button) findViewById(R.id.button_child);
        buttonChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // fuer Bundle festhalten wo wir sind
                // kinderversion setzen ? Dialog mit OK und zurück Button
                viewInfo = "main_kinder";
                spHelper.safeString("AppVersion", "empfaenger");
                setContentView(R.layout.main_kinder);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    class SPHelper {
        SharedPreferences preferences;
        String noValue;

        public SPHelper(String prefName, String noValue) {
            if (prefName == null | noValue == null) {
                Toast.makeText(getApplicationContext(), "Interner Fehler! SPHelper", Toast.LENGTH_LONG).show();
                finish();
            }
            this.noValue = noValue;
            preferences = getSharedPreferences(prefName, MODE_PRIVATE);
        }

        public boolean safeString(String key, String value) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            return editor.commit();
        }

        public String loadString(String key) {
            return preferences.getString(key, noValue);
        }
    }

}
