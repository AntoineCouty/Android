package com.example.tp4_android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;

import java.util.Vector;

public class DrawingView extends SurfaceView implements SurfaceHolder.Callback{

    private DrawingThread drawingThread;
    private Paint fillBlue;
    private Paint strokeRed;
    private Bitmap img;
    private Bitmap img2;
    private float dir [];
    private float dir2 [];
    private float speed;
    private float x = 0;
    private float x1 = 100;
    private float y = 0;
    private float y1 = 200;

    public DrawingView(Context context, AttributeSet attr) {
        super(context, attr);
        getHolder().addCallback(this);
        fillBlue = new Paint();
        fillBlue.setStyle(Paint.Style.FILL);
        fillBlue.setColor(Color.rgb(0,0,255));

        strokeRed = new Paint();
        strokeRed.setColor(Color.rgb(255,0,0));
        strokeRed.setStyle(Paint.Style.STROKE);
        strokeRed.setStrokeWidth(1.0f);

        img = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        img2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        dir = new float[]{0.4F, 0.1F};
        dir2 = new float[]{0.1F, 0.4F};
        speed = 20;



    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        drawingThread = new DrawingThread(holder, this);
        drawingThread.setExec(true);
        setWillNotDraw(false);
        drawingThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
// Permet de relancer le rendu de la surface s'il y a un changement
        this.invalidate();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

        drawingThread.setExec(false);
        try {
            drawingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onDraw(Canvas canvas){


        canvas.drawCircle((float) getWidth()/2, (float)getHeight()/2,getWidth()/4, strokeRed);

        float div = (float)(getWidth()*0.2);

        for(int i = 0 ; i < getHeight(); i++){
            for(int j = 0; j < getWidth(); j++){
                if((i+j)%2 == 0){
                    canvas.drawRect(i*div, j*div,i*div+div, j*div+div, fillBlue);
                }else{
                    canvas.drawRect(i*div, j*div,i*div+div, j*div+div, strokeRed);
                }
            }
        }

        if(x == 0 && y==0) {
            x = (getWidth() - img.getWidth()) / 2.f;
            y = (getHeight() - img.getHeight()) / 2.f;
        } if(x <= 0 || x+img.getWidth() >= getWidth()){
            dir[0]*=-1;
        } if( y <= 0 || y+img.getHeight() >= getHeight()) {
            dir[1]*=-1;
        }

        if(x1 <= 0 || x1+img2.getWidth() >= getWidth()){
            dir2[0]*=-1;
        } if( y1 <= 0 || y1+img2.getHeight() >= getHeight()) {
            dir2[1]*=-1;
        }






        x += dir[0]*speed;
        y += dir[1]*speed;
        canvas.drawBitmap(img, x, y, null);

        x1 += dir2[0]*speed;
        y1 += dir2[1]*speed;
        canvas.drawBitmap(img2, x1, y1, null);


    }


}
