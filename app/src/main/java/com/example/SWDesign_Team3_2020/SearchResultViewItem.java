package com.example.SWDesign_Team3_2020;

public class SearchResultViewItem {
    private String StoreName;
    private String Address;
    private String OpenTime;

    public SearchResultViewItem(String StoreName, String Address, String OpenTime){
        this.StoreName = StoreName;
        this.Address = Address;
        this.OpenTime = OpenTime;
    }
    public String getStoreName() { return StoreName; }

    public void setStoreName(String StoreName) {
        this.StoreName = StoreName;
    }

    public String getAddress() { return Address; }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public void setOpenTime(String Address) {
        this.OpenTime = OpenTime;
    }

    public String getOpenTime() { return OpenTime; }


}
