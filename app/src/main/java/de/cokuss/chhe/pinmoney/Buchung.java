package de.cokuss.chhe.pinmoney;

import java.util.Date;

public class Buchung {

    private Long id;
    private Date date;
    private float value;
    private String text;
    private Long veri_id;
    private Integer veri_type;
    private float balance;

    public Buchung (Long id, Date date, float value, String text, Long veri_id, Integer veri_type, float balance) {
        this.id = id;
        this.date = date;
        this.value = value;
        this.text = text;
        this.veri_id = veri_id;
        this.veri_type = veri_type;
        this.balance = balance;
    }

    public Long getId () {
        return id;
    }

    public Date getDate () {
        return date;
    }

    public float getValue () {
        return value;
    }

    public String getText () {
        return text;
    }

    public Long getVeri_id () {
        return veri_id;
    }

    public Integer getVeri_type () {
        return veri_type;
    }

    public float getBalance () {
        return balance;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public void setDate (Date date) {
        this.date = date;
    }

    public void setValue (float value) {
        this.value = value;
    }

    public void setText (String text) {
        this.text = text;
    }

    public void setVeri_id (Long veri_id) {
        this.veri_id = veri_id;
    }

    public void setVeri_type (Integer veri_type) {
        this.veri_type = veri_type;
    }

    public void setBalance (float balance) {
        this.balance = balance;
    }
}