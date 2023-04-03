package com.example.android_projet.servicesManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.example.android_projet.R;
import com.example.android_projet.Service;
import com.example.android_projet.User;
import com.example.android_projet.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ShowServiceActivity extends FragmentActivity {
    private FirebaseFirestore dataBase;
    private FirebaseAuth mAuth;
    private StorageReference serviceRef;
    private FirebaseUser currentUser;

    private TextView identity;
    private TextView userMail;
    private TextView userTel;
    private ImageView img;
    private TextView title;
    private TextView desc;
    private TextView loc;

    private User user;
    private Service serviceDisp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_service);

        mAuth = FirebaseAuth.getInstance();
        dataBase = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        if(mAuth.getCurrentUser() == null){
            Toast.makeText(this, "Veuillez vous connecter", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));
        }

        Intent showService = getIntent();
        serviceDisp = showService.getParcelableExtra("service");
        getUserService();

        serviceRef = FirebaseStorage.getInstance().getReference().child("images/services/"+serviceDisp.getUser()+"/"+serviceDisp.getTitle()+".png");

        identity = findViewById(R.id.identity);
        userMail = findViewById(R.id.userMail);
        userTel = findViewById(R.id.userTel);
        img = findViewById(R.id.imageShowService);
        title = findViewById(R.id.titleShowService);
        desc = findViewById(R.id.descriptionShowService);
        loc = findViewById(R.id.locShowService);
    }

    private void getUserService(){
        dataBase.collection("users")
                .document(serviceDisp.getUser())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    user = documentSnapshot.toObject(User.class);
                    setView();
                });
    }

    private void setView(){
        identity.setText(user.getNom()+" "+user.getPrenom());
        userMail.setText("mail: " +user.getMail());
        userTel.setText("tel: " +user.getTelephone());

        serviceRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String URL = uri.toString();
            Glide.with(getApplicationContext()).load(URL).error(R.drawable.baseline_account_circle_24).into(img);
        });

        title.setText(serviceDisp.getTitle());
        desc.setText(serviceDisp.getDescription());
        loc.setText(serviceDisp.getLocalisation());
    }
}