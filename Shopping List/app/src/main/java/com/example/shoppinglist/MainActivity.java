package com.example.shoppinglist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.*;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private FloatingActionButton B_NewPurchase;
    private LinearLayout LL_get;
    private Button B_get;
    private Button B_delete;
    private EditText ET_get;
    private RecyclerView RV;
    private DataAdapter adapter;
    private LinearLayout LL_toolbar;

    private ArrayList<Item> purchases = new ArrayList<>();
    private ArrayList<Integer> CheckPurchases = new ArrayList<>();
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, CheckBox> checker = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        findByID();
        buildRecyclerView();
        setClickListener();
    }

    private void findByID() {
        B_get = findViewById(R.id.B_get);
        B_delete = findViewById(R.id.B_delete);
        B_NewPurchase = findViewById(R.id.B_NewPurchase);
        LL_get = findViewById(R.id.LL_get);
        ET_get = findViewById(R.id.ET_get);
        RV = findViewById(R.id.RV);
        LL_toolbar = findViewById(R.id.LL_toolbar);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setClickListener() {
        B_NewPurchase.setOnClickListener(this);
        B_get.setOnClickListener(this);
        B_delete.setOnClickListener(this);
        LL_toolbar.setOnTouchListener(this);
    }

    private void buildRecyclerView() {
        adapter = new DataAdapter(purchases);
        RV.setLayoutManager(new LinearLayoutManager(this));
        RV.setAdapter(adapter);
        adapter.setOnClickListener(new ItemClickListener() {
            @Override
            public void onDeleteClick(int position, CheckBox checkBox) {
                if (checkBox.isChecked()) {
                    removeItem(position);
                }else
                    Toast.makeText(MainActivity.this, "Предмет не куплен", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void CheckBoxClick(int position, CheckBox checkBox) {
                if (checkBox.isChecked()){
                    CheckPurchases.add(position);
                    checker.put(position, checkBox);
                    Collections.sort(CheckPurchases);
                }else{
                    for (int i = 0; i < CheckPurchases.size(); i++) {
                        if (CheckPurchases.get(i) == position){
                            CheckPurchases.remove(i);
                            break;
                        }
                        checker.remove(position);
                    }
                }

            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.B_NewPurchase:
                LL_get.setVisibility(View.VISIBLE);
                B_NewPurchase.setVisibility(View.GONE);
                break;
            case R.id.B_get:
                String text = ET_get.getText().toString();
                if (!text.equals("")){
                    ET_get.setText("");
                    insertItem(purchases.size(), text);
                    RV.smoothScrollToPosition(purchases.size());
                }else{
                    Toast.makeText(MainActivity.this, "Введите покупку", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.B_delete:
                if (CheckPurchases.size()!=0){
                    for (int i = CheckPurchases.size()-1; i >= 0; i--){
                        removeItem(CheckPurchases.get(i));
                    }
                    CheckPurchases.removeAll(CheckPurchases);
                }
                else
                    Toast.makeText(MainActivity.this, "Не стоит ни одной галочки", Toast.LENGTH_SHORT).show();

        }
    }

    @SuppressLint({"RestrictedApi", "ClickableViewAccessibility"})
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            LL_get.setVisibility(View.GONE);
            B_NewPurchase.setVisibility(View.VISIBLE);
        }
        return true;
    }

    private void insertItem(int position, String purchase){
        purchases.add(position, new Item(purchase));
        adapter.notifyItemInserted(position);
    }

    private void removeItem(int position){
        purchases.remove(position);
        CheckBox c = checker.remove(position);
        if (c != null)
            c.setChecked(false);
        boolean b = true;
        for (int i = 0; i < CheckPurchases.size(); i++) {
            if (CheckPurchases.get(i) == position) {
                CheckPurchases.remove(i);
                b = false;
            }
            if (CheckPurchases.size()!= i && CheckPurchases.get(i) >position && !b)
                CheckPurchases.set(i, CheckPurchases.get(i)-1);
        }
        adapter.notifyItemRemoved(position);
    }
}