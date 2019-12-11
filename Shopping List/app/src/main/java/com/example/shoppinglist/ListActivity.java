package com.example.shoppinglist;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements View.OnClickListener{

    private FloatingActionButton B_NewList;
    private EditText ET_get;
    private Button B_get;
    private LinearLayout LL_get;
    private RecyclerView RV;
    private Button B_exit;
    private ListAdapter adapter;
    private String id;


    private ArrayList<String> lists = new ArrayList<>();
    private ArrayList<Integer> ID = new ArrayList<>();

    private class List{
        String listname;
        List(String s){
           listname = s;
        }
    }


    private class connection extends AsyncTask<String, Void, String>{

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            String url = "";
            switch (strings[0]){
                case "GET":
                    url = "https://flask-shoplist.herokuapp.com/api/users/"+id+"/lists";
                    try{
                        //подключение
                        URL obj = new URL(url);
                        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                        con.setRequestMethod("GET");
                        con.connect();
                        //получение ответа
                        int responseCode  = con.getResponseCode();
                        if (responseCode != 200){
                            return "f";
                        }
                        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = reader.readLine())!=null){
                            response.append(inputLine);
                        }
                        reader.close();
                        //парсинг ответа
                        JSONObject jsonObject = new JSONObject(response.toString());
                        return jsonObject.getString("items");
                    } catch (Exception e) {
                        return e.toString();
                    }
                case "POST":
                    url = "https://flask-shoplist.herokuapp.com/api/users/"+id+"/lists";
                    try {
                        //подключение
                        URL obj = new URL(url);
                        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                        con.setRequestProperty("Accept-Language", "en-US,en,q=0,5");
                        con.setRequestProperty("Content-Type", "application/json");
                        con.setRequestMethod("POST");
                        con.setDoOutput(true);
                        //парсинг
                        Gson g = new Gson();
                        ListActivity.List list = new ListActivity.List(strings[1]);
                        String urlParameters = g.toJson(list);
                        //запись в поток
                        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
                        writer.write(urlParameters);
                        wr.flush();
                        writer.close();
                        wr.close();
                        //получение ответа
                        int responseCode  = con.getResponseCode();
                        if (responseCode != 201)
                            return "f";
                        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = reader.readLine())!=null){
                            response.append(inputLine);
                        }
                        reader.close();
                        //парсинг ответа
                        JSONObject jsonObject = new JSONObject(response.toString());
                        int id = jsonObject.getInt("id");
                        //результат
                        ID.add(id);
                        lists.add(strings[1]);
                        adapter.notifyItemInserted(lists.size()-1);

                        return response.toString();

                    } catch (Exception e){
                        return e.toString();
                    }
                case "PUT":

                    break;
                case "DELETE":

                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("f")){
                Toast.makeText(ListActivity.this, "error", Toast.LENGTH_SHORT).show();
            }else {
                if (s.charAt(0) == '['){
                    try {
                        JSONArray jsonArray = new JSONArray(s);
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id");
                            String list = object.getString("listname");
                            ID.add(id);
                            lists.add(list);
                            adapter.notifyItemInserted(lists.size()-1);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(ListActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.list_tool_bar);

        setContentView(R.layout.activity_list);

        getID();
        findByID();
        setClickListener();
        buildRV();
        Load();
        
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
                Intent intent = new Intent(ListActivity.this, PurchasesActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getID(){
        File cache = new File(getCacheDir(), "purchases.txt");
        try {
            FileReader in = new FileReader(cache);
            BufferedReader buffer = new BufferedReader(in);
            String line = buffer.readLine();
            id = line;
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Не удалось открыть файл кэша", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Возникла ошибка при загрузки из кэша", Toast.LENGTH_LONG).show();
        }
    }
    private void Load(){
        new connection().execute("GET");
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
                                File cache = new File(getCacheDir(), "purchases.txt");
                                try {
                                    FileWriter out = new FileWriter(cache, false);
                                    out.flush();
                                    out.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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

    void insertItem(@NonNull String list){
        if (!list.equals("")){
            new connection().execute("POST", list);
        }else{
            Toast.makeText(this, "Строка пуста", Toast.LENGTH_SHORT).show();
        }
    }
    void removeItem(int position){
        lists.remove(position);
        adapter.notifyItemRemoved(position);
    }

}
