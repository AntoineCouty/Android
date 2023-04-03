package com.example.android_projet;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.example.android_projet.menuServices.MenuServicesActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

public class ProfilSetActivity extends AppCompatActivity {

    private static final String TAG = "Add user";
    private FirebaseFirestore dataBase;
    private EditText nom;
    private EditText prenom;
    private EditText tel;
    private ImageView img;

    private String mail;
    private Bitmap bitmap;
    private boolean lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_set);

        dataBase = FirebaseFirestore.getInstance();

        nom = findViewById(R.id.editTextNom);
        prenom = findViewById(R.id.editTextPrenom);
        tel = findViewById(R.id.editTextTel);
        img = findViewById(R.id.imgProfil);

        Intent currentUser = getIntent();
        mail = currentUser.getStringExtra("mail");
        lock = currentUser.getBooleanExtra("lockName", false);
        System.out.println(lock);
        onLoad();
    }



    public void onClickImg(View view){
        ImageButton camera = findViewById(R.id.imageCamera);
        ImageButton gallery = findViewById(R.id.imageGallery);

        Animation aniFade_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        Animation aniFade_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);

        if(camera.getVisibility() == View.VISIBLE){
            camera.startAnimation(aniFade_out);
            gallery.startAnimation(aniFade_out);

            camera.setVisibility(View.GONE);
            gallery.setVisibility(View.GONE);
        }else{
            camera.startAnimation(aniFade_in);
            gallery.startAnimation(aniFade_in);

            camera.setVisibility(View.VISIBLE);
            gallery.setVisibility(View.VISIBLE);
        }

    }

    public void onClickCamera(View view) throws CameraAccessException {

    }



    public void onClickSave(View view){
        if(nom.toString().equals("") || prenom.toString().equals("")){
            Toast.makeText(ProfilSetActivity.this, "Rentrez votre Nom et Prénom",
                    Toast.LENGTH_SHORT).show();
            return;
        }else {
            BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            User user = new User(mail, nom.getText().toString(), prenom.getText().toString(), tel.getText().toString(), bitmap);

            dataBase.collection("users").document(mail).set(user);

            startActivity(new Intent(ProfilSetActivity.this, MenuServicesActivity.class));
        }
    }

    public void onLoad(){
        if(lock){
            dataBase.collection("users")
                    .document(mail)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                // Si le résultat de la requête est vide, alors il s'agit d'une première connexion
                                User user = documentSnapshot.toObject(User.class);
                                nom.setEnabled(false);
                                nom.setText(user.getNom());
                                prenom.setEnabled(false);
                                prenom.setText(user.getPrenom());
                                tel.setText(user.getTelephone());
                        }
                    });
            }
        }
}