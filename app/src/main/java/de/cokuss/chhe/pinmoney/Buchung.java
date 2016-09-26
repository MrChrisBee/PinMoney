package de.cokuss.chhe.pinmoney;

import java.util.Date;

public class Buchung {

    private Long id;
    private Konto konto;
    private Date date;
    private float value;
    private String text;
    private Long veri_id;
    private Integer veri_type;
    private float balance;



    public Buchung(){
        //keine Ahnung ob eim Parameterloser Konstruktor hier sinn macht
    }

    public Buchung(Long id,Konto konto, Date date, float value, String text, Long verifi_id, Integer veri_type) {
        this.id = id;
        this.konto = konto;
        this.date = date;
        this.value = value;
        this.text = text;
        this.veri_id = verifi_id;
        this.veri_type = veri_type;
        this.balance = konto.getKontostand();
    }

    public Konto getKonto () {
        return konto;
    }

    public float getBalance () {
        return balance;
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

    @Override
   public String toString() {
        return  konto.getInhaber() + " " + date + " " + value + " € " +
                text + " " + veri_type + " " + veri_id + " " +
                konto.getKontostand() + " €";
    }

}
