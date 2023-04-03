package com.example.tp2_android;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.gson.Gson;

public class Resultat extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat);
        Intent res = getIntent();

        TextView txtClair = findViewById(R.id.resClair);
        TextView txtChiffre = findViewById(R.id.resChiffre);

        ParametresParcelable obj = (new Gson()).fromJson(res.getStringExtra("myObj"), ParametresParcelable.class);
        txtClair.setText("Le message original est "+ obj.getClair());
        txtChiffre.setText("Le message chiffré est "+ obj.getChiffre());

    }

    public void returnOnClick(View view){
        Intent intent = new Intent(Resultat.this, MainActivity.class);
        startActivity(intent);
    }
}