package com.example.android_projet.servicesManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.android_projet.R;
import com.example.android_projet.Service;
import com.example.android_projet.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;

public class CreateServiceActivity extends AppCompatActivity {
    private EditText title;
    private EditText desc;
    private EditText loc;
    private ImageView img;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dataBase;
    private StorageReference serviceRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);

        title = findViewById(R.id.titleCreateService);
        desc = findViewById(R.id.descriptionCreateService);
        loc = findViewById(R.id.locCreateService);
        img = findViewById(R.id.chooseImageImage);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        dataBase = FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser() == null){
            Toast.makeText(this, "Veuillez vous connecter", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));
        }

        findViewById(R.id.buttonCreateService).setOnClickListener(v -> submit());
    }

    private void submit(){
        String title_txt = title.getText().toString();
        String desc_txt = desc.getText().toString();
        String loc_txt = loc.getText().toString();

        if(title_txt.matches("")){title.setError("Il faut impérativement un titre!"); return;}
        if(desc_txt.matches("")){desc.setError("Il faut impérativement une description!"); return;}
        if(desc_txt.matches("")){loc.setError("Il faut impérativement une localisation!"); return;}

        FirebaseUser user = mAuth.getCurrentUser();
        Service service = new Service(title_txt, desc_txt, loc_txt, user.getEmail());

        if(img.getDrawable() instanceof BitmapDrawable) {
            serviceRef = FirebaseStorage.getInstance().getReference().child("images/services/"+user.getEmail()+"/"+title.getText().toString()+".png");
            BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = serviceRef.putBytes(data);
        }

        dataBase.collection("services").document(user.getEmail() + title_txt).set(service);
        Intent res = new Intent().setClass(this, ServicesManagerActivity.class);
        res.putExtra("mail", user.getEmail());
        startActivity(res);
    }
}