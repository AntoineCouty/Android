package com.example.android_projet.menuServices;

import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.example.android_projet.ProfilSetActivity;
import com.example.android_projet.R;
import com.example.android_projet.Service;
import com.example.android_projet.showService.ShowServiceActivity;
import com.example.android_projet.createService.CreateServiceActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MenuServicesActivity extends FragmentActivity {

    private final String TAG = "Display services";
    private String mail;
    private FirebaseFirestore dataBase;
    private ArrayList<Service> services;
    private boolean dataRecup = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_services);

        dataBase = FirebaseFirestore.getInstance();
        findViewById(R.id.create).setOnClickListener(v -> createService());
        findViewById(R.id.delete).setOnClickListener(v -> deleteService());
        findViewById(R.id.profilSet).setOnClickListener(v -> onClickProfil());

        Intent intent = getIntent();
        mail = intent.getStringExtra("mail");
        services = new ArrayList<>();

        getServices();

        System.out.println(services.get(0).getTitle());


    }

    private void createService(){
        Intent res = new Intent().setClass(this, CreateServiceActivity.class);
        startActivity(res);
    }

    private void deleteService(){
        Intent res = new Intent().setClass(this, ShowServiceActivity.class);
        startActivity(res);
    }

    private void onClickProfil(){
        Intent setProfile = new Intent(MenuServicesActivity.this, ProfilSetActivity.class);
        setProfile.putExtra("lockName", true);
        setProfile.putExtra("mail", mail);
        startActivity(setProfile);
    }

    private void getServices(){

        dataBase.collection("services")
                .whereNotEqualTo("mail", mail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            System.out.println(task.getResult().size());
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                services.add(document.toObject(Service.class));
                            }
                            Log.d(TAG, "data base load");
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        dataRecup = true;
                    }

                });

    }

}