package com.example.shoppinglist;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ET_username;
    private EditText ET_password;
    private Button B_auth;
    private Button B_reg;

    private class auth extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String serverAnswer;
            String url = "https://flask-shoplist.herokuapp.com/api/login";
            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection)obj.openConnection();
                con.setRequestProperty("Accept-Language", "en-US,en,q=0,5");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                //парсинг
                Gson g = new Gson();
                AuthActivity.User user = new AuthActivity.User(strings[0], strings[1]);
                String urlParameters = g.toJson(user);
                //Запись в поток
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
                writer.write(urlParameters);
                wr.flush();
                writer.close();
                wr.close();
                //получение ответа
                con.connect();
                int responseCode  = con.getResponseCode();
                if (responseCode != 200)
                    return "f";
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = reader.readLine())!=null){
                    response.append(inputLine);
                }
                reader.close();

                serverAnswer = response.toString();
                return serverAnswer;

            } catch (Exception e) {
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            TextView TV = findViewById(R.id.TV);
            if (s.equals("f")){
                TV.setText("Авторизация не удалась");
            }else{
                try {
                    JSONObject user = new JSONObject(s);
                    int id = user.getInt("id");
                    File cache = new File(getCacheDir(), "purchases.txt");
                    try {
                        FileWriter out = new FileWriter(cache, false);
                        out.write(Integer.toString(id));
                        out.flush();
                        TV.setText(Integer.toString(id));
                        startActivity(new Intent(AuthActivity.this, ListActivity.class));
                        finish();
                    } catch (IOException e) {
                        TV.setText(e.toString());
                    }
                } catch (JSONException e) {
                    TV.setText(e.toString());
                }
               // TV.setText(s);
            }
        }
    }

    private class User {
        String username;
        String password;

        User(String u, String p){
            username = u;
            password = p;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        findByID();
        setListener();
    }

    private void findByID() {
        ET_username = findViewById(R.id.ET_username);
        ET_password = findViewById(R.id.ET_password);
        B_auth = findViewById(R.id.B_auth);
        B_reg = findViewById(R.id.B_reg);
    }
    private void setListener(){
        B_auth.setOnClickListener(this);
        B_reg.setOnClickListener(this);
    }

    private void connect(String username, String password){
        if(!username.equals("") && !password.equals("")){
            auth a = new auth();
            a.execute(username, password);
        }else{
            Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.B_auth:
                connect(ET_username.getText().toString(), ET_password.getText().toString());
                break;
            case R.id.B_reg:
                startActivity(new Intent(AuthActivity.this, RegistrationActivity.class));
                finish();
                break;
        }
    }
}
