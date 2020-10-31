package com.nfc.connect.nfc.Retailer;

public class Retail_Data {
    int farm_id;
    String Farmer_id;
    String type;

    public String getFarmer_id() {
        return Farmer_id;
    }

    public void setFarmer_id(String farmer_id) {
        Farmer_id = farmer_id;
    }

    String quality;
    String loading_date;
    String truck_id;
    String location;
    String box_id;
    String quantity;
    String retiler_id;
    String discharge_date;
    String retailer_location;

    public String getRetiler_id() {
        return retiler_id;
    }

    public void setRetiler_id(String retiler_id) {
        this.retiler_id = retiler_id;
    }

    public String getDischarge_date() {
        return discharge_date;
    }

    public void setDischarge_date(String discharge_date) {
        this.discharge_date = discharge_date;
    }

    public String getRetailer_location() {
        return retailer_location;
    }

    public void setRetailer_location(String retailer_location) {
        this.retailer_location = retailer_location;
    }

    public int getFarm_id() {
        return farm_id;
    }

    public void setFarm_id(int farm_id) {
        this.farm_id = farm_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getLoading_date() {
        return loading_date;
    }

    public void setLoading_date(String loading_date) {
        this.loading_date = loading_date;
    }

    public String getTruck_id() {
        return truck_id;
    }

    public void setTruck_id(String truck_id) {
        this.truck_id = truck_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBox_id() {
        return box_id;
    }

    public void setBox_id(String box_id) {
        this.box_id = box_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    public Retail_Data(){   }
    public Retail_Data(int farm_id,String Farmer_id, String type, String quantity, String loading_date, String truck_id, String location, String box_id, String quality, String retiler_id, String discharge_date, String retailer_location){
        this.farm_id = farm_id;
        this.Farmer_id=Farmer_id;
        this.type = type;
        this.quality = quality;
        this.loading_date = loading_date;
        this.truck_id = truck_id;
        this.location = location;
        this.box_id = box_id;
        this.quantity = quantity;
        this.retiler_id=retiler_id;
        this.discharge_date=discharge_date;
        this.retailer_location=retailer_location;
    }

    public Retail_Data(String Farmer_id,String type, String quantity, String loading_date, String truck_id, String location, String box_id, String quality, String retiler_id, String discharge_date, String retailer_location){
        this.Farmer_id=Farmer_id;
        this.type = type;
        this.quality = quality;
        this.loading_date = loading_date;
        this.truck_id = truck_id;
        this.location = location;
        this.box_id = box_id;
        this.quantity = quantity;
        this.retiler_id=retiler_id;
        this.discharge_date=discharge_date;
        this.retailer_location=retailer_location;
    }

}
