package com.example.android_projet;

import android.content.Intent;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
    }

    public void onClickRegister(View view){
        EditText mail = findViewById(R.id.email);
        EditText mdp = findViewById(R.id.mdp);
        EditText mdpConfirm = findViewById(R.id.mdpConfirm);

        String email = mail.getText().toString();
        String password =  mdp.getText().toString();
        String passwordConfirm = mdpConfirm.getText().toString();

        String regExpMail = "^[A-Za-z0-9._-]+@[A-Za-z0-9._-]+\\.[a-z][a-z]+$";
        String regExpPass = "(?=.*[0-9])(?=.*[A-Z]).{6,20}";


        if(email.matches(regExpMail)) {

            if(password.matches(regExpPass)) {

                if (password.equals(passwordConfirm)) {

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(RegisterActivity.this, "Thanks for register",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(user);
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()),
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "Password différent de confirm.",
                            Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(RegisterActivity.this, "Le mot de passe doit contenir une majuscule et un chiffre.",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(RegisterActivity.this, "Email incorrect ou déjà utilisée.",
                    Toast.LENGTH_SHORT).show();
        }


    }

    private void updateUI(FirebaseUser user) {

    }
}