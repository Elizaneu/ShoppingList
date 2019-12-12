package com.example.shoppinglist;

import android.widget.TextView;

class Item {
    private String itemname;
    Item(String purchase){itemname = purchase;}
    String getPurchase(){return itemname;}
    void setPurchase(String Text){this.itemname = Text;}
}
