package com.example.android_projet;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.android_projet.menuServices.MenuServicesActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private FirebaseFirestore dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        dataBase = FirebaseFirestore.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    public void onClickRegister(View view){
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }


    //==============================================Permet de vérifier la combinaison email/mot de passe
    public void onClickConnect(View view){

        EditText EditEmail = findViewById(R.id.emailConnect);
        EditText EditPassword = findViewById(R.id.editTextTextPassword);

        String email = EditEmail.getText().toString();
        String password = EditPassword.getText().toString();

        if(email.equals("") || password.equals("")) {
            Toast.makeText(MainActivity.this, "Remplissez tous les champs",
                    Toast.LENGTH_SHORT).show();
        }else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "Welcome",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(user);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Email ou mot de passe incorrect",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }
    }

    public void onClickSet(View view){startActivity(new Intent(MainActivity.this, ProfilSetActivity.class));}


//=================================================  Vérifie si il s'agit d'une première connexion           =================================
//=================================================  Si c'est le cas, redirige vers le complément de profil =================================
//=================================================  sinon vers le menu principal                            =================================
    private void updateUI(FirebaseUser user) {
        if(user != null) {
            dataBase.collection("users")
                    .whereEqualTo("mail", user.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                // Si le résultat de la requête est vide, alors il s'agit d'une première connexion
                                Intent login;
                                if (Objects.requireNonNull(task.getResult()).isEmpty()) {
                                    login = new Intent(MainActivity.this, ProfilSetActivity.class);
                                    Log.d(TAG, "First log");
                                } else {
                                    login = new Intent(MainActivity.this, MenuServicesActivity.class);
                                    Log.d(TAG, "Main menu");
                                }
                                login.putExtra("mail", user.getEmail());
                                startActivity(login);
                            } else {
                                Log.d(TAG, "Error getting user: ", task.getException());
                            }
                        }
                    });
        }
    }


}