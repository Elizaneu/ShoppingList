package com.example.shoppinglist;

import android.graphics.Color;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

interface ItemClickListener{
    void onDeleteClick(int position, CheckBox checkBox);
    void CheckBoxClick(int position, CheckBox checkBox);
}


class ViewHolder extends RecyclerView.ViewHolder  {
    TextView purchase;
    private Button B_delete;
    private CheckBox checkBox;

    ViewHolder(@NonNull View itemView, final ItemClickListener listener) {
        super(itemView);
        findByID();
        setListener(listener);
    }


    private void findByID() {
        purchase = itemView.findViewById(R.id.purchase);
        B_delete = itemView.findViewById(R.id.B_DeleteThis);
        checkBox = itemView.findViewById(R.id.checkBox);
    }

    private void setListener(final ItemClickListener listener) {
        B_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onDeleteClick(position, checkBox);
                    }
                }
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        if (isChecked)
                            purchase.setTextColor(Color.GREEN);
                        else
                            purchase.setTextColor(Color.GRAY);
                        listener.CheckBoxClick(position, checkBox);
                    }
                }
            }
        });
    }

}
