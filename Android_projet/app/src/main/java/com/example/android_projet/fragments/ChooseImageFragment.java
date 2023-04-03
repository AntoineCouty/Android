package com.example.android_projet.fragments;

import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import androidx.fragment.app.Fragment;
import com.example.android_projet.R;

public class ChooseImageFragment extends Fragment {
    private CameraManager camera;
    private Context context;
    private ImageButton iconCamera;
    private ImageButton iconGallery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_image, container, false);

        context = view.getContext();
        camera = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        iconCamera = view.findViewById(R.id.chooseImageCamera);
        iconGallery = view.findViewById(R.id.chooseImageGallery);

        view.findViewById(R.id.chooseImagePlus).setOnClickListener(v -> display());
        iconCamera.setOnClickListener(v -> camera());
        iconGallery.setOnClickListener(v -> gallery());

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

    private void camera(){}

    private void gallery(){}
}