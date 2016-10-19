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

class HistoryAdapter extends ArrayAdapter<PinMoneyEnrty> {
    private DateHelper dateHelper = new DateHelper();


    //PinMoneyEnrty (Zahlungen zahlungen, Date entryDate, String kontoName, String action)

    HistoryAdapter(Context context, ArrayList<PinMoneyEnrty> pinMoneyEnrties) {
        super(context, 0, (List<PinMoneyEnrty>) pinMoneyEnrties);
    }
    //getView wird vom System zum Inflaten eimer View (hier ListView für die History Einträge) aufgerufen
    //Hier wird also jeweils eine Zeile der History ListView (Entspricht einer Zeile in PinInfo) befüllt

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Zahlungen zahlungen;
        PinMoneyEnrty pinMoneyEnrty = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_history_entry, parent, false);
        }
        if(pinMoneyEnrty.getZahlungen() != null) {
            zahlungen = pinMoneyEnrty.getZahlungen();
        } else {
            zahlungen = new Zahlungen(null,null,0);
        }

        // Lookup view for data population
        TextView tvEntryDate = (TextView) convertView.findViewById(R.id.iHiEntryDate);
        TextView tvAccountName = (TextView) convertView.findViewById(R.id.iHiAccountName);
        TextView tvAction = (TextView) convertView.findViewById(R.id.iHiAction);
        TextView tvBetrag = (TextView) convertView.findViewById(R.id.iHiValue);
        TextView tvStartDate = (TextView) convertView.findViewById(R.id.iHiStartDate);
        TextView tvCycle = (TextView) convertView.findViewById(R.id.iHiCycle);
        // Populate the data into the template view using the data object
        if (pinMoneyEnrty.getAction() != null) {
            tvAction.setText(pinMoneyEnrty.getAction());
        } else {
            tvAction.setText(R.string.none);
        }
        tvAccountName.setText(pinMoneyEnrty.getKontoName());
        if (pinMoneyEnrty.getEntryDate() != null) {
            tvEntryDate.setText(dateHelper.sdfShort.format(pinMoneyEnrty.getEntryDate()));
        } else {
            tvEntryDate.setText(R.string.no);
        }
        if (zahlungen.getDate() != null) {
            tvStartDate.setText(dateHelper.sdfShort.format(zahlungen.getDate()));
        } else {
            tvStartDate.setText(R.string.no);
        }
        tvBetrag.setText(String.format(Locale.getDefault(), "%.2f", zahlungen.getBetrag()));
        tvCycle.setText(zahlungen.getTurnusStrShort());
        // Return the completed view to render on screen
        return convertView;
    }
}
