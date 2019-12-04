package com.example.shoppinglist;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<Item> purchases = new ArrayList<>();
    private ItemClickListener listener;

    DataAdapter(ArrayList<Item> purchases) {
        this.purchases = purchases;
    }

    void setOnClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder v = new ViewHolder(view, listener);
        return v;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item purchase = purchases.get(position);
        holder.purchase.setText(purchase.getPurchase());
    }

    @Override
    public int getItemCount() {
        return purchases.size();
    }

    Item getItem(int position){
        return purchases.get(position);
    }
}
