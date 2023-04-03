package com.example.tp2_android;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.Scanner;

public class Chiffrement extends AppCompatActivity {

    char[] alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'g',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z' };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chiffrement);

    }

    public void Chiffre(View view){

        Intent intentChiffre = new Intent(Chiffrement.this, Resultat.class);
        EditText txt = findViewById(R.id.editTextTextPersonName);
        EditText key = findViewById(R.id.keyValue);

        String res= "";
        int key_chiffre;
        if(key.getText().toString().equals("")){
            key_chiffre = readFile(this, R.raw.key);
        }else{
            key_chiffre = Integer.parseInt(key.getText().toString());
        }

        for (char s: txt.getText().toString().toCharArray()) {
            for(int i = 0; i<alphabet.length; i++)
                if(s == alphabet[i]) {
                    res += alphabet[(i+key_chiffre)%alphabet.length];
                }
        }
        ParametresParcelable obj = new ParametresParcelable(txt.getText().toString(), res, key_chiffre);
        intentChiffre.putExtra("myObj", (new Gson()).toJson(obj));
        startActivity(intentChiffre);
    }

    public int readFile(Context context, int resId){
        InputStream inputStream = context.getResources().openRawResource(resId);
        Scanner sc = new Scanner(inputStream);
        return Integer.parseInt(sc.nextLine());
    }


}