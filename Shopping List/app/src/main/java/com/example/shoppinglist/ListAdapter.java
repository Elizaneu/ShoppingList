package com.example.shoppinglist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListHolder> {

    private ArrayList<String> lists = new ArrayList<>();
    private ListItemClickListener listener;

    ListAdapter(ArrayList<String> lists) {
        this.lists = lists;
    }
    void setOnClickListener(ListItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ListHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {
        String purchase = lists.get(position);
        holder.list.setText(purchase);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
