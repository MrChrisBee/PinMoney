package de.cokuss.chhe.pinmoney;

import java.util.Date;

public class Buchung {

    //private long id; AUTOINCREMENT
    private Date date;
    private double value;
    private String text;
    private Long veri_id;
    private Integer veri_type;
    private double balance;


    public Buchung () {

    }

    public Buchung (Date date, double value, String text, Long verifi_id, Integer veritype, double balance) {
        this.date = date;
        this.value = value;
        this.text = text;
        this.veri_id = verifi_id;
        this.veri_type = veritype;
        this.balance = balance;
    }


    @Override
    public String toString () {
        return date + " " + value + " € " + text + " " + veri_type + " " + veri_id + " " + balance + " €";
    }
}