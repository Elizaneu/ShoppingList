package com.example.shoppinglist;

import android.annotation.SuppressLint;
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
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{

    private Button B_registration;
    private Button B_back;
    private EditText ET_username;
    private EditText ET_email;
    private EditText ET_password;

    private class registration extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String serverAnswer;
            String url = "https://flask-shoplist.herokuapp.com/api/users";
            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection)obj.openConnection();
                con.setRequestProperty("Accept-Language", "en-US,en,q=0,5");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                //парсинг
                Gson g = new Gson();
                User user = new User(strings[0], strings[1], strings[2]);
                String urlParameters = g.toJson(user);
//                String urlParameters = "username="+strings[0]+" password="+strings[2]+" email="+strings[1];


//                JSONObject jobj = new JSONObject();
//                jobj.put("username", strings[0]);
//                jobj.put("password", strings[2]);
//                jobj.put("email", strings[1]);
//                String urlParameters = jobj.toString();

                TextView textView = findViewById(R.id.TV_us);
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
                if (responseCode == 400)
                    return String.valueOf(responseCode);
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

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String s) {
            TextView TV = findViewById(R.id.TV);

            if (s.equals("400")){
                TV.setText("Регистрация не удалась");
            }else{
                startActivity(new Intent(RegistrationActivity.this, PurchasesActivity.class));
            }
        }
    }


    private class User {
        String username;
        String email;
        String password;

        User(String u, String e, String p){
            username = u;
            password = p;
            email = e;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        findByID();
        setListener();
        ET_username.setText("testuser");
        ET_email.setText("testuser@testuser.com");
        ET_password.setText("testuser");
    }

    private void findByID() {
        B_registration = findViewById(R.id.B_registration);
        B_back = findViewById(R.id.B_back);
        ET_username = findViewById(R.id.ET_username);
        ET_email = findViewById(R.id.ET_email);
        ET_password = findViewById(R.id.ET_password);
    }
    private void setListener(){
        B_registration.setOnClickListener(this);
        B_back.setOnClickListener(this);
    }
    private void back(){

    }
    private void connect(String username, String email, String password){
        registration reg = new registration();
        reg.execute(username, email, password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.B_registration:
                connect(ET_username.getText().toString(), ET_email.getText().toString(), ET_password.getText().toString());
                break;
            case R.id.B_back:
                back();
                break;
        }
    }
}
