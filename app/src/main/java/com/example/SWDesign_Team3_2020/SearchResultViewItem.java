package com.example.SWDesign_Team3_2020;

public class SearchResultViewItem {
    private String StoreName;
    private String StoreOwner;
    private String Address;

    public SearchResultViewItem(String StoreName, String StoreOwner, String Address){
        this.StoreName = StoreName;
        this.StoreOwner = StoreOwner;
        this.Address = Address;
    }
    public String getStoreName() { return StoreName; }

    public void setStoreName(String StoreName) {
        this.StoreName = StoreName;
    }

    public String getStoreOwner() { return StoreOwner; }

    public void setStoreOwner(String StoreOwner) {
        this.StoreOwner = StoreOwner;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getAddress() { return Address; }


}
