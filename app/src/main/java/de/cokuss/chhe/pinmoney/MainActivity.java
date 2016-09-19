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
            //habe ich schon einen Eintrag in den SharedPreferences
//            if (spHelper != null) {
//                if(spHelper.loadString("AppVersion") == "in" ) {
//
//                } else if(spHelper.loadString("AppVersion") == "out" ) {
//
//                }
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

        //     spHelper = new SPHelper("AppData", "Kein Wert hinterlegt");
    }

    private void registerFirstStart() {
        Button buttonParents = (Button) findViewById(R.id.button_parents);
        buttonParents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // fuer Bundle festhalten wo wir sind
                // elternversion setzen ? Dialog mit OK und zurück Button
                viewInfo = "empfaenger_neu";
                //sind sie sicher?

                SPHelper.safeString(getApplicationContext(), "AppVersion", "out");
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
                SPHelper.safeString(getApplicationContext(), "AppVersion", "in");
                setContentView(R.layout.main_kinder);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


}
