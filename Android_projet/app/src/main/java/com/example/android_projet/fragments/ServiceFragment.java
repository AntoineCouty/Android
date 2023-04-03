package com.example.android_projet.fragments;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.example.android_projet.R;
import com.example.android_projet.Service;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ServiceFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);

        Service service = getArguments().getParcelable("service");
        StorageReference serviceRef = FirebaseStorage.getInstance().getReference().child("images/services/"+ service.getUser()+"/"+ service.getTitle()+".png");

        TextView title = view.findViewById(R.id.textServiceTitle);
        TextView desc = view.findViewById(R.id.textServiceDescription);
        TextView loc = view.findViewById(R.id.textServiceLocalisation);
        ImageView img = view.findViewById(R.id.imageService);

        title.setText(service.getTitle());
        desc.setText(service.getDescription());
        loc.setText(service.getLocalisation());

        serviceRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String URL = uri.toString();
            Glide.with(view.getContext().getApplicationContext()).load(URL).error(R.drawable.ic_launcher_background).into(img);
        });

        return view;
    }
}