package com.example.shoppinglist;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

interface ItemClickListener{
    void CheckBoxClick(int position, CheckBox checkBox);
    void CardListener(int position, MotionEvent event);
    void EditButtonClick(int position);
}


class ViewHolder extends RecyclerView.ViewHolder  {
    TextView purchase;
    private CheckBox checkBox;
    RelativeLayout background, foreground;
    private Button B_edit;


    ViewHolder(@NonNull View itemView, final ItemClickListener listener) {
        super(itemView);
        findByID();
        setListener(listener);
    }

    public boolean getCheck(){
        return checkBox.isChecked();
    }

    private void findByID() {
        purchase = itemView.findViewById(R.id.purchase);
        checkBox = itemView.findViewById(R.id.checkBox);
        background = itemView.findViewById(R.id.RL_background);
        foreground = itemView.findViewById(R.id.RL_foreground);
        B_edit = itemView.findViewById(R.id.B_edit);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void setListener(final ItemClickListener listener) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        if (isChecked)
                            purchase.setTextColor(Color.parseColor(new String("#008577")));
                        else
                            purchase.setTextColor(Color.GRAY);
                        listener.CheckBoxClick(position, checkBox);
                    }
                }
            }
        });
        background.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.CardListener(position, event);
                    }
                }
                return true;
            }
        });
        B_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.EditButtonClick(position);
                    }
                }
            }
        });
    }
}
