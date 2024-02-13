package com.bannet.skils.Model;

public class ModelCauralView {

    String amount;
    String title1;
    String title2;

    public ModelCauralView(String amount, String title1, String title2) {
        this.amount = amount;
        this.title1 = title1;
        this.title2 = title2;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }
}
