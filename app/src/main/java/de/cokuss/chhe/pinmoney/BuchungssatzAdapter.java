package de.cokuss.chhe.pinmoney;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BuchungssatzAdapter extends ArrayAdapter<Buchung> {
    public BuchungssatzAdapter (Context context, ArrayList<Buchung> buchungen) {
        super(context, 0, (List<Buchung>) buchungen);
    }

    @NonNull
    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        Buchung buchung = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_buchungssatz, parent, false);
        }
        // Lookup view for data population
        TextView tvDate = (TextView) convertView.findViewById(R.id.idate);
        TextView tvBuchungstext = (TextView) convertView.findViewById(R.id.ibuchungstext);
        TextView tvVeryfikation = (TextView) convertView.findViewById(R.id.iveryfikation);
        TextView tvBetrag = (TextView) convertView.findViewById(R.id.ibetrag);
        TextView tvSumme = (TextView) convertView.findViewById(R.id.isumme);
        // Populate the data into the template view using the data object

        if (buchung != null) {
            tvDate.setText(buchung.getDate().toString());
            tvBuchungstext.setText(buchung.getText());
            tvVeryfikation.setText(buchung.getVeri_type());
            tvBetrag.setText(String.format(Locale.getDefault(), "%.2f", buchung.getValue()));
            tvSumme.setText(String.format(Locale.getDefault(), "%.2f", buchung.getValue()));
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
