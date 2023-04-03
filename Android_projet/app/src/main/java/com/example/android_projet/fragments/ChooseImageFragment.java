package com.example.android_projet.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import com.example.android_projet.R;

public class ChooseImageFragment extends Fragment {
    private CameraManager camera;
    private Context context;
    private ImageButton iconCamera;
    private ImageButton iconGallery;
    private ImageView image;

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher;
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_image, container, false);

        context = view.getContext();
        camera = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        iconCamera = view.findViewById(R.id.chooseImageCamera);
        iconGallery = view.findViewById(R.id.chooseImageGallery);
        image = view.findViewById(R.id.chooseImageImage);

        view.findViewById(R.id.chooseImagePlus).setOnClickListener(v -> display());
        iconCamera.setOnClickListener(v -> camera());
        iconGallery.setOnClickListener(v -> gallery());

        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        image.setImageURI(result.getData().getData());
                    }
                });

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Bundle extras = data.getExtras();
                        Bitmap img = (Bitmap) extras.get("data");
                        image.setImageBitmap(img);
                    }
                });

        return view;
    }

    private void display(){
        Animation animation;
        int state;

        if(iconCamera.getVisibility() == View.VISIBLE){
            animation = AnimationUtils.loadAnimation(context.getApplicationContext(),R.anim.fade_out);
            state = View.GONE;
        }else{
            animation = AnimationUtils.loadAnimation(context.getApplicationContext(),R.anim.fade_in);
            state = View.VISIBLE;
        }
        iconCamera.startAnimation(animation);
        iconGallery.startAnimation(animation);
        iconCamera.setVisibility(state);
        iconGallery.setVisibility(state);
    }

    private void camera(){
        try { cameraActivityResultLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)); }
        catch (ActivityNotFoundException e) {
            Toast.makeText( getContext(), "Probleme avec la camera", Toast.LENGTH_LONG).show();
        }
    }

    private void gallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }
}
