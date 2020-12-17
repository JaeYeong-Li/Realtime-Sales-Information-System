package com.example.SWDesign_Team3_2020;

public class SearchResultViewItem {
    private String storeId;
    private String storeName;
    private String category;
    private String lat;
    private String lang;
    private String address;
    private String openTime;
    private String openDate;

    public SearchResultViewItem(){
    }

    public SearchResultViewItem(String storeId, String storeName, String category, String lat, String lang, String address, String openTime, String openDate){
        this.storeId = storeId;
        this.storeName = storeName;
        this.category = category;
        this.lat = lat;
        this.lang = lang;
        this.address = address;
        this.openTime = openTime;
        this.openDate = openDate;
    }

    public SearchResultViewItem(String storeId, String storeName){
        this.storeId = storeId;
        this.storeName = storeName;
    }

    public String getStoreId() { return storeId; }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() { return storeName; }

    public void setStoreName(String StoreName) {
        this.storeName = StoreName;
    }

    public String getCategory() { return category; }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLat() { return lat; }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() { return lang; }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getAddress() { return address; }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getOpenTime() { return openTime; }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getOpenDate() { return openDate; }


}
