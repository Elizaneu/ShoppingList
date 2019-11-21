package com.example.shoppinglist;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ViewHolder extends RecyclerView.ViewHolder {
    TextView purchase;
    ViewHolder(@NonNull View itemView) {
        super(itemView);
        purchase = itemView.findViewById(R.id.purchase);
    }
}
