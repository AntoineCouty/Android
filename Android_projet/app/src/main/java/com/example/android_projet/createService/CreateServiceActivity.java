package com.example.android_projet.createService;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import com.example.android_projet.Service;
import com.example.android_projet.menuServices.MenuServicesActivity;
import com.example.android_projet.R;
import com.google.android.gms.maps.MapView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateServiceActivity extends FragmentActivity {
    private EditText title;
    private EditText desc;
    private FirebaseFirestore dataBase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);

        title = findViewById(R.id.titleCreateService);
        desc = findViewById(R.id.descriptionCreateService);
        dataBase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        Button button = findViewById(R.id.buttonFrag);
        button.setOnClickListener(v -> submit());
        button.setText("submit");
    }

    private void submit(){
        String title_txt = title.getText().toString();
        String desc_txt = desc.getText().toString();


        FirebaseUser user = mAuth.getCurrentUser();
        Map<String, Object> service = new HashMap<>();
        service.put("title", title_txt);
        service.put("description", desc_txt);
        service.put("localisation", "fst");
        service.put("user", user.getEmail());

        if( title_txt.matches("") ||
                desc_txt.matches("") ){
            Toast.makeText(this, "Informations non valides !", Toast.LENGTH_LONG).show();
        }else{
            DocumentReference serviceRef = dataBase.collection("services").document();
            serviceRef.set(service);
            Intent res = new Intent().setClass(this, MenuServicesActivity.class);
            startActivity(res);
        }
    }


}