package de.cokuss.chhe.pinmoney.fundamentals;

import java.util.Date;

import de.cokuss.chhe.pinmoney.DateHelper;

public class Buchung {

    private Long id;
    private Date date;
    private float value;
    private String text;
    private Long veri_id;
    private Integer veri_type;
    private float balance;

    private DateHelper dateHelper;


    public Buchung(Long id, Date date, float value, String text, Long veri_id, Integer veri_type, float balance) {
        this.id = id;
        this.date = date;
        this.value = value;
        this.text = text;
        this.veri_id = veri_id;
        this.veri_type = veri_type;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Buchung{" +
                "id=" + id +
                ", date=" + date +
                ", value=" + value +
                ", text='" + text + '\'' +
                ", veri_id=" + veri_id +
                ", veri_type=" + veri_type +
                ", balance=" + balance +
                '}';
    }

    Long getId() {
        return id;
    }

    void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    void setDate(Date date) {
        this.date = date;
    }

    public float getValue() {
        return value;
    }

    void setValue(float value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    void setText(String text) {
        this.text = text;
    }

    public Long getVeri_id() {
        return veri_id;
    }

    void setVeri_id(Long veri_id) {
        this.veri_id = veri_id;
    }

    public Integer getVeri_type() {
        return veri_type;
    }

    void setVeri_type(Integer veri_type) {
        this.veri_type = veri_type;
    }

    public float getBalance() {
        return balance;
    }

    void setBalance(float balance) {
        this.balance = balance;
    }
}