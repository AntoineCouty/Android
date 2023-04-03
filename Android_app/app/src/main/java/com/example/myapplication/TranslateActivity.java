package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class TranslateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
    }


    public void Translate(View view){
        EditText value = findViewById(R.id.editTextTextPersonName2);
        TextView txt = findViewById(R.id.textView2);

        RadioButton rbDollar = findViewById(R.id.radioButton2);
        RadioButton rbEuro = findViewById(R.id.radioButton);
        if(rbDollar.isChecked())
            txt.setText(Float.toString((float) (Integer.parseInt(value.getText().toString()) * 0.88)));
        else if(rbEuro.isChecked())
            txt.setText(Float.toString((float) (Integer.parseInt(value.getText().toString()) / 0.88)));
        else{
            txt.setText("please choose a translation");
        }

    }
}