package com.example.tp2_android;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void ChiffrementOnClick(View view){
        Intent intent = new Intent(MainActivity.this, Chiffrement.class);
        startActivity(intent);
    }


    public void DechiffrementOnClick(View view){
        Intent intent = new Intent(MainActivity.this, Dechiffrement.class);
        startActivity(intent);
    }
}