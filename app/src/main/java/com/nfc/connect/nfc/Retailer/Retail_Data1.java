package com.nfc.connect.nfc.Retailer;

public class Retail_Data1 {
    int farm_id;
    String rejected;



    public int getFarm_id() {
        return farm_id;
    }

    public void setFarm_id(int farm_id) {
        this.farm_id = farm_id;
    }

    public String getRejected() {
        return rejected;
    }

    public void setRejected(String rejected) {
        this.rejected = rejected;
    }

    public Retail_Data1(){   }
    public Retail_Data1(int farm_id, String rejected){
        this.farm_id = farm_id;
        this.rejected = rejected;



    }

    public Retail_Data1(String rejected){
        this.rejected = rejected;

    }

}
