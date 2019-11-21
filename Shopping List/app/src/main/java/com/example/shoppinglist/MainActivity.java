package com.example.shoppinglist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private FloatingActionButton B_NewPurchase;
    private LinearLayout LL_get;
    private Button B_get;
    private EditText ET_get;
    private RecyclerView RV;
    private DataAdapter dataAdapter;

    private ArrayList<String> purchases = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        B_NewPurchase = findViewById(R.id.B_NewPurchase);
        B_NewPurchase.setOnClickListener(this);
        B_get = findViewById(R.id.B_get);
        B_get.setOnClickListener(this);
        LL_get = findViewById(R.id.LL_get);
        ET_get = findViewById(R.id.ET_get);

        RV = findViewById(R.id.RV);
        RV.setLayoutManager(new LinearLayoutManager(this));
        dataAdapter = new DataAdapter(this, purchases);
        RV.setAdapter(dataAdapter);
        RV.setOnTouchListener(this);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.B_NewPurchase:
                LL_get.setVisibility(View.VISIBLE);
                B_NewPurchase.setVisibility(View.GONE);
                break;
            case R.id.B_get:
                String purchase  = ET_get.getText().toString();
                ET_get.setText("");
                if (!purchase.equals("")) {
                    purchases.add(purchase);
                    dataAdapter.notifyDataSetChanged();
                    RV.smoothScrollToPosition(purchases.size());
                }
//                LL_get.setVisibility(View.GONE);
//                B_NewPurchase.setVisibility(View.VISIBLE);
                break;
            default:
                LL_get.setVisibility(View.GONE);
                B_NewPurchase.setVisibility(View.VISIBLE);
                break;

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
}



