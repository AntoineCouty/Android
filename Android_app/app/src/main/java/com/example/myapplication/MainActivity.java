package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public int count = (int) (Math.random() * 100);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressLint("SetTextI18n")
    public void generateToast(View view){
        EditText txt = findViewById(R.id.editTextTextPersonName);
        TextView txtView = findViewById(R.id.textView);

        if(Integer.parseInt(txt.getText().toString()) == count)
            txtView.setText("Gagné !");
        else if(Integer.parseInt(txt.getText().toString()) > count)
            txtView.setText("Plus petit");
        else if(Integer.parseInt(txt.getText().toString()) < count)
            txtView.setText("Plus grand");



    }
}