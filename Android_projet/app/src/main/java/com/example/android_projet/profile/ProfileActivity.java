package com.example.android_projet.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.android_projet.R;
import com.example.android_projet.User;
import com.example.android_projet.login.LoginActivity;
import com.example.android_projet.servicesManager.ServicesManagerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseFirestore dataBase;
    private FirebaseAuth mAuth;
    private StorageReference profil;

    private EditText nom;
    private EditText prenom;
    private EditText tel;
    private ImageView img;

    private String mail;
    private boolean lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dataBase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        profil = FirebaseStorage.getInstance().getReference().child("images/profil/"+user.getEmail()+".png");

        if(mAuth.getCurrentUser() == null){
            Toast.makeText(this, "Veuillez vous connecter", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));
        }

        nom = findViewById(R.id.fragProfileLastname);
        prenom = findViewById(R.id.fragProfileFirstname);
        tel = findViewById(R.id.fragProfileTel);
        img = findViewById(R.id.chooseImageImage);
        findViewById(R.id.buttonProfile).setOnClickListener(v -> save());

        Intent currentUser = getIntent();
        mail = user.getEmail();
        lock = currentUser.getBooleanExtra("lockName", false);

        onLoad();
    }

    public void save(){
        if(nom.toString().equals("")){ nom.setError("Renseignez votre nom."); return;}
        if(prenom.toString().equals("")){ prenom.setError("Renseignez votre prénom."); return;}

        if(img.getDrawable() instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = profil.putBytes(data);
        }

        User user = new User(mail, nom.getText().toString(), prenom.getText().toString(), tel.getText().toString());
        dataBase.collection("users").document(mail).set(user);
        startActivity(new Intent(ProfileActivity.this, ServicesManagerActivity.class).putExtra("mail", mail));
    }

    public void onLoad(){
        if(lock){
            dataBase.collection("users")
                    .document(mail)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        // Si le résultat de la requête est vide, alors il s'agit d'une première connexion
                        User user = documentSnapshot.toObject(User.class);
                        nom.setEnabled(false);
                        nom.setText(user.getNom());
                        prenom.setEnabled(false);
                        prenom.setText(user.getPrenom());
                        tel.setText(user.getTelephone());
                    });

            profil.getDownloadUrl().addOnSuccessListener(uri -> {
                String URL = uri.toString();
                Glide.with(getApplicationContext()).load(URL).error(R.drawable.baseline_account_circle_24).into(img);
            });
        }
    }
}