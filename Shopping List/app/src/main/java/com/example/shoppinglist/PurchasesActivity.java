package com.example.shoppinglist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PurchasesActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton B_NewPurchase;
    private LinearLayout LL_get;
    private Button B_get;
    private Button B_delete;
    private EditText ET_get;
    private RecyclerView RV;
    private DataAdapter adapter;
    private LinearLayout LL_edit;
    private EditText ET_edit;
    private Button B_edit;
    private Button B_back;

    private int position;

    private ArrayList<Item> purchases = new ArrayList<>();
    private ArrayList<Integer> CheckPurchases = new ArrayList<>();
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, CheckBox> checker = new HashMap<>();

    private static class asyncTask extends AsyncTask<URL, Void, String > {
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String lines = "";
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                DataInputStream in = new DataInputStream(connection.getInputStream());
                BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
                String s;
                while((s = buffer.readLine())!= null){
                    lines+=s;
                    lines+="\n";
                };
            } catch (IOException e) {
                lines+=e.toString();
            }finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return lines;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.purchases_tool_bar);

        setContentView(R.layout.activity_purchases);

        findByID();
        buildRecyclerView();
        setClickListener();
        //LoadCache();
    }
    private void findByID() {
        B_get = findViewById(R.id.B_get);
        B_delete = findViewById(R.id.B_delete);
        B_NewPurchase = findViewById(R.id.B_NewPurchase);
        LL_get = findViewById(R.id.LL_get);
        ET_get = findViewById(R.id.ET_get);
        RV = findViewById(R.id.RV);
        LL_edit = findViewById(R.id.LL_edit);
        B_edit=findViewById(R.id.B_edit);
        ET_edit = findViewById(R.id.ET_edit);
        B_back = findViewById(R.id.B_back);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setClickListener() {
        B_NewPurchase.setOnClickListener(this);
        B_get.setOnClickListener(this);
        B_delete.setOnClickListener(this);
        B_edit.setOnClickListener(this);
        B_back.setOnClickListener(this);
    }

    private void buildRecyclerView() {
        adapter = new DataAdapter(purchases);
        RV.setLayoutManager(new LinearLayoutManager(this));
        RV.setAdapter(adapter);
        adapter.setOnClickListener(new ItemClickListener() {
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
            @SuppressLint("RestrictedApi")
            @Override
            public void CardListener(int position, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LL_get.setVisibility(View.GONE);
                    LL_edit.setVisibility(View.GONE);
                    B_NewPurchase.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void EditButtonClick(int position) {
                startEditItem(position);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (((ViewHolder)viewHolder).getCheck()) {
                    removeItem(viewHolder.getAdapterPosition());
                    Toast.makeText(PurchasesActivity.this, "Товар удален", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(PurchasesActivity.this, "Товар не помечен галочкой", Toast.LENGTH_SHORT).show();
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            }
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                final View foregroundView = ((ViewHolder) viewHolder).foreground;

                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
            }
            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                        RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                        int actionState, boolean isCurrentlyActive) {
                final View foregroundView = ((ViewHolder) viewHolder).foreground;
                getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
            }
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final View foregroundView = ((ViewHolder) viewHolder).foreground;
                getDefaultUIUtil().clearView(foregroundView);
            }
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    final View foregroundView = ((ViewHolder) viewHolder).foreground;
                    getDefaultUIUtil().onSelected(foregroundView);
                }
            }
        }).attachToRecyclerView(RV);
    }

    private void LoadCache(){
        File cache = new File(getCacheDir(), "purchases.txt");
        try {
            FileReader in = new FileReader(cache);
            BufferedReader buffer = new BufferedReader(in);
            String line = buffer.readLine();
            while (line != null) {
                insertItem(purchases.size(), line);
                line = buffer.readLine();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Не удалось открыть файл кэша", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Возникла ошибка при загрузки из кэша", Toast.LENGTH_LONG).show();
        }
    }

    private void SaveCache(){
        File cache = new File(getCacheDir(), "purchases.txt");
        try {
            FileWriter out = new FileWriter(cache, false);
            for (Item s:purchases){
                out.write(s.getPurchase());
                out.append('\n');
            }
            out.flush();
        } catch (FileNotFoundException  e) {
            Toast.makeText(this, "Не удалось получить доступ к файлам кэша для перезаписи", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Не удалось записать в кэш изменения", Toast.LENGTH_LONG).show();
        }
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
                    Toast.makeText(PurchasesActivity.this, "Введите покупку", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.B_delete:
                if (CheckPurchases.size()!=0){
                    AlertDialog.Builder DeleteAll = new AlertDialog.Builder(this);
                    DeleteAll.setTitle("Delete")
                            .setIcon(R.drawable.ic_delete_black_24dp)
                            .setMessage("Вы действительно хотите удалить все отмеченные покупки?")
                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for (int i = CheckPurchases.size()-1; i >= 0; i--){
                                        removeItem(CheckPurchases.get(i));
                                    }
                                    CheckPurchases.removeAll(CheckPurchases);
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    DeleteAll.create().show();
                }
                else
                    Toast.makeText(PurchasesActivity.this, "Не стоит ни одной галочки", Toast.LENGTH_SHORT).show();
                break;
            case R.id.B_edit:
                if (!ET_edit.getText().toString().equals("")){
                    editItem(position, new Item(ET_edit.getText().toString()));
                    LL_get.setVisibility(View.GONE);
                    LL_edit.setVisibility(View.GONE);
                    B_NewPurchase.setVisibility(View.VISIBLE);
                    View view = this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }else
                    Toast.makeText(this, "Введите текст", Toast.LENGTH_SHORT).show();
            case R.id.B_back:
                finish();
                break;

        }
    }

    private void insertItem(int position, String purchase){
        if (purchase != null){
            purchases.add(position, new Item(purchase));
            adapter.notifyItemInserted(position);
            SaveCache();
        }
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
        SaveCache();
    }

    @SuppressLint("RestrictedApi")
    private void startEditItem(int position){
        LL_get.setVisibility(View.GONE);
        LL_edit.setVisibility(View.VISIBLE);
        B_NewPurchase.setVisibility(View.GONE);
        ET_edit.setText(purchases.get(position).getPurchase());
        this.position = position;
    }

    private void editItem(int position, Item i){
        purchases.set(position, i);
        adapter.notifyItemChanged(position);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if (B_NewPurchase.getVisibility() == View.VISIBLE) {
            finish();
        }else{
            LL_get.setVisibility(View.GONE);
            LL_edit.setVisibility(View.GONE);
            B_NewPurchase.setVisibility(View.VISIBLE);

        }
    }

    void connect() throws MalformedURLException {
        asyncTask task = new asyncTask();
        task.execute(new URL("https://flask-shoplist.herokuapp.com/api/users/1"));
    }
}
