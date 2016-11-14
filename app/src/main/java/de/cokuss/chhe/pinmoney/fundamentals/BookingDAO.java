package de.cokuss.chhe.pinmoney.fundamentals;

import android.content.Context;

import java.util.ArrayList;

public interface BookingDAO extends KontoDAO {
    ArrayList<Booking> getAllBuchungen (String name);

    void createBuchung (Account konto, Booking booking);
    Booking calcSavings(Context context, String owner);
}

