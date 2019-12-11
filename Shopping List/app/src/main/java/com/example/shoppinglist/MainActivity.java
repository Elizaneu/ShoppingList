package com.example.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File cache = new File(getCacheDir(), "purchases.txt");
        try {
            FileReader in = new FileReader(cache);
            BufferedReader buffer = new BufferedReader(in);
            String line = buffer.readLine();
            if (line != null)
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            else
                startActivity(new Intent(MainActivity.this, AuthActivity.class));
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Не удалось открыть файл кэша", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Возникла ошибка при загрузки из кэша", Toast.LENGTH_LONG).show();
        }



        finish();
    }


}
