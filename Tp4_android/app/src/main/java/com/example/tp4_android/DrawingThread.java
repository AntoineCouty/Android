package com.example.tp4_android;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class DrawingThread extends Thread{

    private SurfaceHolder surfaceHolder;
    private DrawingView drawingView;
    private boolean exec;

    DrawingThread(SurfaceHolder surfaceHolder, DrawingView drawingView){
        super();
        this.surfaceHolder = surfaceHolder;
        this.drawingView = drawingView;
    }

    public void setExec(boolean exec) {
        this.exec = exec;
    }

    public void run() {
        Canvas c;
        while(exec) {
            c = null;
            try {
                c = surfaceHolder.lockCanvas(null);
                synchronized(surfaceHolder) {
// ici, mettre les méthodes de modification des positions des objets
                    drawingView.postInvalidate();
// = remplacement de onDraw() (optimisation)
                }
            } finally {
                if( c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }




}
