package com.example.tp2_android;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private Button chiffre;
    private Button dechiffre;
    private Intent intent;
    private EditText txt;

    char[] alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'g',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z' };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chiffre = findViewById(R.id.button_chiffre);
        dechiffre = findViewById(R.id.button_dechiffre);
        EditText key = findViewById(R.id.keyValue);

        chiffre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Resultat.class);
                txt = findViewById(R.id.txtClair);

                String res= "";
                int key_chiffre;
                if(key.getText().toString().equals("")){
                    key_chiffre = readFile(MainActivity.this, R.raw.key);
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
                intent.putExtra("myObj", (new Gson()).toJson(obj));
                startActivity(intent);
            }
        });

        dechiffre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Resultat.class);
                txt = findViewById(R.id.textChiffre);
                int key_chiffre;
                if(key.getText().toString().equals("")){
                    key_chiffre = readFile(MainActivity.this, R.raw.key);
                }else{
                    key_chiffre = Integer.parseInt(key.getText().toString());
                }

                String res="";
                int tmp;
                for (char s: txt.getText().toString().toCharArray()) {
                    for(int i = 0; i<alphabet.length; i++)
                        if(s == alphabet[i]) {
                            tmp = i;
                            for(int j = i; j > i - key_chiffre; j--){
                                tmp--;
                                if(tmp == -1){
                                    tmp = alphabet.length-1;
                                }
                            }
                            res += alphabet[tmp];
                        }
                }

                ParametresParcelable obj = new ParametresParcelable(res, txt.getText().toString(), key_chiffre);
                intent.putExtra("myObj", (new Gson()).toJson(obj));
                startActivity(intent);

            }
        });
    }

    /*public void ChiffrementOnClick(View view){
        Intent intent = new Intent(MainActivity.this, Chiffrement.class);
        startActivity(intent);
    }


    public void DechiffrementOnClick(View view){
        Intent intent = new Intent(MainActivity.this, Dechiffrement.class);
        startActivity(intent);
    }*/


    public int readFile(Context context, int resId){
        InputStream inputStream = context.getResources().openRawResource(resId);
        Scanner sc = new Scanner(inputStream);
        return Integer.parseInt(sc.nextLine());
    }

}