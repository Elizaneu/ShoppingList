package com.example.shoppinglist;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

interface ListItemClickListener{
    void DeleteButtonClick(int position);
    void listClick(int position);
    void EditButtonClick(int position);
}

public class ListHolder extends RecyclerView.ViewHolder {
    private Button B_delete;
    private Button B_edit;
    TextView list;

    public ListHolder(@NonNull View itemView, final ListItemClickListener listener) {
        super(itemView);
        findByID();
        setListener(listener);
    }
    void findByID(){
        B_delete = itemView.findViewById(R.id.B_list_delete);
        B_edit = itemView.findViewById(R.id.B_list_edit);
        list = itemView.findViewById(R.id.TV_list);
    }
    void setListener(final ListItemClickListener listener){
        B_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.DeleteButtonClick(position);
                    }
                }
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.listClick(position);
                    }
                }
            }
        });
        B_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.EditButtonClick(position);
                    }
                }
            }
        });
    }
}
