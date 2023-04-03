package com.example.android_projet.register;

import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.android_projet.R;
import com.example.android_projet.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonRegister).setOnClickListener(v -> register());
        findViewById(R.id.loginRegister).setOnClickListener(v -> login());
    }

    public void login(){ startActivity(new Intent(RegisterActivity.this, LoginActivity.class)); }

    public void register() {
        EditText mail = findViewById(R.id.emailRegister);
        EditText mdp = findViewById(R.id.passwordRegister);
        EditText mdpConfirm = findViewById(R.id.passwordConfirmRegister);

        String email = mail.getText().toString();
        String password = mdp.getText().toString();
        String passwordConfirm = mdpConfirm.getText().toString();

        String regExpMail = "^[A-Za-z0-9._-]+@[A-Za-z0-9._-]+\\.[a-z][a-z]+$";
        String regExpPass = "(?=.*[0-9])(?=.*[A-Z]).{6,20}";

        if (email.matches(regExpMail)) {
            if (password.matches(regExpPass)) {
                if (password.equals(passwordConfirm)) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(RegisterActivity.this, "Thanks for register",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(user);
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(RegisterActivity.this, Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()),
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            });
                } else { mdpConfirm.setError("Password différent."); }
            } else { mdp.setError("Le mot de passe doit contenir une majuscule et un chiffre."); }
        } else { mail.setError("Email incorrect ou déjà utilisé."); }
    }

    private void updateUI(FirebaseUser user) {}
}