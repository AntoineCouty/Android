package com.example.android_projet.login;

import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.android_projet.R;
import com.example.android_projet.profile.ProfileActivity;
import com.example.android_projet.register.RegisterActivity;
import com.example.android_projet.servicesManager.ServicesManagerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        dataBase = FirebaseFirestore.getInstance();

        findViewById(R.id.buttonLogin).setOnClickListener(v -> connect());
        findViewById(R.id.registerLogin).setOnClickListener(v -> register());
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){currentUser.reload();}
    }

    public void register(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    //==============================================Permet de vérifier la combinaison email/mot de passe
    public void connect(){
        EditText EditEmail = findViewById(R.id.EmailLogin);
        EditText EditPassword = findViewById(R.id.PasswordLogin);

        String email = EditEmail.getText().toString();
        String password = EditPassword.getText().toString();

        if(email.equals("")){ EditEmail.setError("Veuillez renseigner votre email."); }
        else if(password.equals("")) { EditPassword.setError("Veuillez renseigner votre mot de passe"); }
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_LONG).show();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Email ou mot de passe incorrect", Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }
                    });
        }
    }

    //=================================================  Vérifie si il s'agit d'une première connexion           =================================
    //=================================================  Si c'est le cas, redirige vers le complément de profil =================================
    //=================================================  sinon vers le menu principal                            =================================
    private void updateUI(FirebaseUser user) {
        if(user != null) {
            dataBase.collection("users")
                    .whereEqualTo("mail", user.getEmail())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Si le résultat de la requête est vide, alors il s'agit d'une première connexion
                            Intent login;
                            if (Objects.requireNonNull(task.getResult()).isEmpty()) {
                                login = new Intent(LoginActivity.this, ProfileActivity.class);
                            } else {
                                login = new Intent(LoginActivity.this, ServicesManagerActivity.class);
                            }
                            login.putExtra("mail", user.getEmail());
                            startActivity(login);
                        }
                    });
        }
    }
}