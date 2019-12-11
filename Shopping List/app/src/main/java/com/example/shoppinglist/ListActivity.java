package com.example.shoppinglist;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.*;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements View.OnClickListener{

    private FloatingActionButton B_NewList;
    private EditText ET_get;
    private Button B_get;
    private LinearLayout LL_get;
    private RecyclerView RV;
    private Button B_exit;
    private ListAdapter adapter;

    private ArrayList<String> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.list_tool_bar);

        setContentView(R.layout.activity_list);

        findByID();
        setClickListener();
        buildRV();
        getID();
        
    }

    private void findByID() {
        B_NewList = findViewById(R.id.B_NewList);
        ET_get = findViewById(R.id.ET_get);
        B_get = findViewById(R.id.B_get);
        LL_get = findViewById(R.id.LL_get);
        RV = findViewById(R.id.RV);
        B_exit = findViewById(R.id.B_exit);
    }
    private void setClickListener() {
        B_NewList.setOnClickListener(this);
        B_get.setOnClickListener(this);
        B_exit.setOnClickListener(this);
    }
    private void buildRV(){
        adapter = new ListAdapter(lists);
        RV.setLayoutManager(new LinearLayoutManager(this));
        RV.setAdapter(adapter);
        adapter.setOnClickListener(new ListItemClickListener() {
            @Override
            public void DeleteButtonClick(final int position) {
                AlertDialog.Builder Delete = new AlertDialog.Builder(ListActivity.this);
                Delete.setTitle("Delete")
                        .setIcon(R.drawable.ic_delete_black_24dp)
                        .setMessage("Вы действительно хотите удалить список?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeItem(position);
                            }
                        })
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                Delete.create().show();

            }

            @Override
            public void listClick(int position) {
                startActivity(new Intent(ListActivity.this, PurchasesActivity.class));
                finish();
            }
        });
    }
    private void getID(){
        File cache = new File(getCacheDir(), "purchases.txt");
        try {
            FileReader in = new FileReader(cache);
            BufferedReader buffer = new BufferedReader(in);
            String line = buffer.readLine();
            if (line != null)
                insertItem(line);
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Не удалось открыть файл кэша", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Возникла ошибка при загрузки из кэша", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.B_NewList:
                LL_get.setVisibility(View.VISIBLE);
                B_NewList.setVisibility(View.GONE);
                break;
            case R.id.B_get:
                insertItem(ET_get.getText().toString());
                ET_get.setText("");
                break;
            case R.id.B_exit:
                AlertDialog.Builder Exit = new AlertDialog.Builder(this);
                Exit.setTitle("Log out!")
                        .setIcon(R.drawable.ic_exit)
                        .setMessage("Вы действительно хотите выйти?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(ListActivity.this, AuthActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                Exit.create().show();
                break;
        }
    }
    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if (B_NewList.getVisibility() == View.VISIBLE) {
            finish();
        }else{
            LL_get.setVisibility(View.GONE);
            B_NewList.setVisibility(View.VISIBLE);
        }
    }

    void insertItem(String list){
        if (!list.equals("")){
            lists.add(lists.size(), list);
            adapter.notifyItemInserted(lists.size()-1);
        }else{
            Toast.makeText(this, "Строка пуста", Toast.LENGTH_SHORT).show();
        }
    }
    void removeItem(int position){
        lists.remove(position);
        adapter.notifyItemRemoved(position);
    }

}
