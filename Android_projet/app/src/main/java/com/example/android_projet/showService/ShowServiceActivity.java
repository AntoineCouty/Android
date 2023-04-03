package com.example.android_projet.showService;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import com.example.android_projet.R;
import com.example.android_projet.Service;
import com.example.android_projet.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShowServiceActivity extends FragmentActivity {

    private FirebaseFirestore dataBase;

    TextView userName;
    TextView userLastname;
    TextView userMail;
    TextView userTel;

    TextView title;
    TextView desc;

    User user;
    Service serviceDisp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_service);

        Intent showService = getIntent();
        serviceDisp = showService.getParcelableExtra("service");
        getUser();
        // set text
        //findViewById(R.id.titleShowService);
        //findViewById(R.id.descriptionShowService);

        userLastname = findViewById(R.id.userLastName);
        userName = findViewById(R.id.userName);
        userMail = findViewById(R.id.userMail);
        userTel = findViewById(R.id.userTel);
        title = findViewById(R.id.titleShowService);
        desc = findViewById(R.id.descriptionShowService);

        dataBase = FirebaseFirestore.getInstance();



        Button button = findViewById(R.id.buttonFrag);
        button.setOnClickListener(v -> contact());
        button.setText("contact");

        setView();
    }

    private void contact(){
        Toast.makeText(this, "contact", Toast.LENGTH_LONG).show();
    }

    private void getUser(){
        dataBase.collection("users")
                .document(serviceDisp.getUser())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(User.class);
                    }
                });
    }

    private void setView(){

        userLastname.setText(user.getNom());
        userName.setText(user.getPrenom());
        userMail.setText(user.getMail());
        userTel.setText(user.getTelephone());

        title.setText(serviceDisp.getTitle());
        desc.setText(serviceDisp.getDescription());
    }
}