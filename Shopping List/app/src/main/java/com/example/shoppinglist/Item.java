package com.example.shoppinglist;

import android.widget.TextView;

class Item {
    private String Text;
    Item(String purchase){Text = purchase;}
    String getPurchase(){return Text;}
    void setPurchase(String Text){this.Text = Text;}
}
