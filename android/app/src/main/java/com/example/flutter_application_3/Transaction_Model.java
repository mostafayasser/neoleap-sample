package com.example.flutter_application_3;

/**
 * Created by m.alibraheem on 07/01/2018.
 */

public class Transaction_Model {
    private String Amount ,RRN , Date,iDate;

    int Id;

    public Transaction_Model(int Id,String Amount ,String RRN ,String Date,String iDate)
    {
        this.Amount = Amount;
        this.Id=Id;
        this.RRN = RRN;
        this.Date = Date;
        this.iDate = iDate;
    }
    public Transaction_Model(){};
    public String getiDate() {
        return iDate;
    }

    public void setiDate(String iDate) {
        this.iDate = iDate;
    }
    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getRRN() {
        return RRN;
    }

    public void setRRN(String RRN) {
        this.RRN = RRN;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}

