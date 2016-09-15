package de.cokuss.chhe.pinmoney;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    String viewInfo = "first_start";
    SPHelper spHelper = new SPHelper("AppData","Kein Wert hinterlegt");

    //hier wird das Bundle (savedInstanceState) aktualisiert
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
            viewInfo = (String) savedInstanceState.get("inView");
            switch (viewInfo) {
                case "main_eltern":
                    setContentView(R.layout.main_eltern);
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
    }

    private void registerFirstStart() {
        Button buttonParents = (Button) findViewById(R.id.button_parents);
        buttonParents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // fuer Bundle festhalten wo wir sind
                // elternversion setzen ? Dialog mit OK und zurück Button


                viewInfo = "main_eltern";
                spHelper.safeString("AppVersion","Zahler");
                setContentView(R.layout.main_eltern);
            }
        });
        Button buttonChild = (Button) findViewById(R.id.button_child);
        buttonParents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // fuer Bundle festhalten wo wir sind
                // kinderversion setzen ? Dialog mit OK und zurück Button


                viewInfo = "main_eltern";
                spHelper.safeString("AppVersion","Zahler");
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
