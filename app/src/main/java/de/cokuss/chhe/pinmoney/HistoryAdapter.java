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

import de.cokuss.chhe.pinmoney.fundamentals.Payments;

public class HistoryAdapter extends ArrayAdapter<PinMoneyEntry> {
    private DateHelper dateHelper = new DateHelper();


    //PinMoneyEntry (Payments payments, Date entryDate, String kontoName, String action)

    public HistoryAdapter(Context context, ArrayList<PinMoneyEntry> pinMoneyEnrties) {
        super(context, 0, (List<PinMoneyEntry>) pinMoneyEnrties);
    }
    //getView gets called from System to inflate a View (here ListView for history entrys)
    //here every time one row of the History ListView (from one row in PinInfo) gets filled
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Payments payments;
        PinMoneyEntry pinMoneyEntry = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_history_entry, parent, false);
        }
        if(pinMoneyEntry.getPayments() != null) {
            payments = pinMoneyEntry.getPayments();
        } else {
            payments = new Payments(null,null,0);
        }

        // Lookup view for data population
        TextView tvEntryDate = (TextView) convertView.findViewById(R.id.iHiEntryDate);
        TextView tvAccountName = (TextView) convertView.findViewById(R.id.iHiAccountName);
        TextView tvAction = (TextView) convertView.findViewById(R.id.iHiAction);
        TextView tvBetrag = (TextView) convertView.findViewById(R.id.iHiValue);
        TextView tvStartDate = (TextView) convertView.findViewById(R.id.iHiStartDate);
        TextView tvCycle = (TextView) convertView.findViewById(R.id.iHiCycle);
        // Populate the data into the template view using the data object
        if (pinMoneyEntry.getAction() != null) {
            tvAction.setText(pinMoneyEntry.getAction());
        } else {
            tvAction.setText(R.string.none);
        }
        tvAccountName.setText(pinMoneyEntry.getKontoName());
        if (pinMoneyEntry.getEntryDate() != null) {
            tvEntryDate.setText(dateHelper.sdfShort.format(pinMoneyEntry.getEntryDate()));
        } else {
            tvEntryDate.setText(R.string.no);
        }
        if (payments.getDate() != null) {
            tvStartDate.setText(dateHelper.sdfShort.format(payments.getDate()));
        } else {
            tvStartDate.setText(R.string.no);
        }
        tvBetrag.setText(String.format(Locale.getDefault(), "%.2f", payments.getBetrag()));
        tvCycle.setText(payments.getTurnusStrShort());
        // Return the completed view to render on screen
        return convertView;
    }
}
