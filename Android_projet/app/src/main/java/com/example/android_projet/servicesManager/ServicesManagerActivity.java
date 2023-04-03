package com.example.android_projet.servicesManager;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.android_projet.R;
import com.example.android_projet.Service;
import com.example.android_projet.fragments.ServiceFragment;
import com.example.android_projet.login.LoginActivity;
import com.example.android_projet.profile.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;

public class ServicesManagerActivity extends AppCompatActivity {
    private final int baseColor = 0xFF3700B3;
    private final int targetColor = 0x606060E0;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dataBase;
    private ArrayList<Service> services;
    private ArrayList<Service> services_delete;
    private ImageView profileIcon;
    private ImageView searchIcon;
    private ImageView disconnectIcon;
    private GridLayout gridLayout;
    private String mail;
    private boolean del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_manager);

        mAuth = FirebaseAuth.getInstance();
        dataBase = FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser() == null){
            Toast.makeText(this, "Veuillez vous connecter", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));
        }

        Intent intent = getIntent();
        mail = intent.getStringExtra("mail");

        findViewById(R.id.createButtonMenu).setOnClickListener(v -> createService());
        findViewById(R.id.deleteButtonMenu).setOnClickListener(v -> deleteService());

        services = new ArrayList<>();
        services_delete = new ArrayList<>();
        gridLayout = findViewById(R.id.menuGridView);
        getServices();
        getOwnServices();

        profileIcon = findViewById(R.id.profileIcon);
        profileIcon.setOnClickListener(v -> gotoProfile());
        searchIcon = findViewById(R.id.searchIcon);
        searchIcon.setOnClickListener(v -> searchWord());
        disconnectIcon = findViewById(R.id.disconnectIcon);
        disconnectIcon.setOnClickListener(v -> disconnect());

        del = false;
    }

    private void createService(){
        Intent res = new Intent(this, CreateServiceActivity.class);
        startActivity(res);
    }

    private void deleteService(){
        del = !del;
        if(del) {displayServices(services_delete); }
        else{ displayServices(services); }
    }

    private void gotoProfile(){
        animation(baseColor,targetColor,profileIcon);
        animation(targetColor,baseColor,profileIcon);
        Intent res = new Intent(this, ProfileActivity.class);
        res.putExtra("lockName", true);
        res.putExtra("mail", mail);
        startActivity(res);
    }

    private void searchWord(){
        animation(baseColor,targetColor,searchIcon);
        animation(targetColor,baseColor,searchIcon);
        EditText search = findViewById(R.id.searchBar);
        String msg = search.getText().toString();
        ArrayList<Service> service_search = new ArrayList<>();
        for(Service s : services){
            if(s.getTitle().matches("(.*)"+msg+"(.*)"))
                service_search.add(s);
        }
        displayServices(service_search);
        search.setText("");
    }

    private void disconnect(){
        animation(baseColor,targetColor,disconnectIcon);
        animation(targetColor,baseColor,disconnectIcon);
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void animation(int from, int to, ImageView target){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), from, to);
        colorAnimation.setDuration(250); // milliseconds
        colorAnimation.addUpdateListener(animator -> target.setBackgroundColor((int) animator.getAnimatedValue()));
        colorAnimation.start();
    }

    private void getServices(){
        dataBase.collection("services")
                .whereNotEqualTo("user", mail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            services.add(document.toObject(Service.class));
                        }
                        displayServices(services);
                    }
                });
    }

    private void getOwnServices(){
        dataBase.collection("services")
                .whereEqualTo("user", mail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            services_delete.add(document.toObject(Service.class));
                        }
                    }
                });
    }

    public void displayServices(ArrayList<Service> service){
        gridLayout.removeAllViews();

        float cellSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int screenSize = getResources().getDisplayMetrics().widthPixels;

        int total = service.size();
        int column = (int) (screenSize/cellSize);
        int row = total / column;
        gridLayout.setColumnCount(column);
        gridLayout.setRowCount(row + 1);

        FragmentTransaction ft ;
        for(Service s : service){
            ft = getSupportFragmentManager().beginTransaction();
            Fragment frag = new ServiceFragment();
            Bundle bdl = new Bundle();
            bdl.putParcelable("service", s);
            frag.setArguments(bdl);
            ft.add(R.id.menuGridView, frag).commit();
        }
    }

    public void clickService(View view) {
        TextView title = view.findViewById(R.id.textServiceTitle);
        TextView desc = view.findViewById(R.id.textServiceDescription);
        TextView loc = view.findViewById(R.id.textServiceLocalisation);

        if(del){
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Confirmation").setMessage("Voulez vous vraiment supprimer ce service ?");
            builder.setCancelable(true);

            builder.setPositiveButton("OUI", (dialog, id) -> {
                for (Service s : services_delete) {
                    if (s.getTitle().equals(title.getText().toString()) &&
                            s.getDescription().equals(desc.getText().toString()) &&
                            s.getLocalisation().equals(loc.getText().toString())) {
                        deleteInDataBase(s);
                        break;
                    }
                }
            });
            builder.setNegativeButton("NON", (dialog, id) -> dialog.cancel());

            AlertDialog alert = builder.create();
            alert.show();
        }else {
            Intent show = new Intent(this, ShowServiceActivity.class);
            for (Service s : services) {
                if (s.getTitle().equals(title.getText().toString()) &&
                        s.getDescription().equals(desc.getText().toString()) &&
                        s.getLocalisation().equals(loc.getText().toString())) {
                    show.putExtra("service", s);
                    break;
                }
            }
            startActivity(show);
        }
    }

    public void deleteInDataBase(Service s){
        dataBase.collection("services")
                .document(s.getUser()+s.getTitle())
                .delete()
                .addOnSuccessListener(aVoid -> {});
        StorageReference serviceRef = FirebaseStorage.getInstance().getReference().child("images/services/"+s.getUser()+"/"+s.getTitle()+".png");
        serviceRef.delete();

        services_delete.remove(s);
        del = false;
        displayServices(services_delete);
    }
}