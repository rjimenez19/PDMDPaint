package com.example.rafa.pdmdpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class Vista extends View {

    private Paint pincel;
    private Canvas lienzo;
    private Bitmap mapaBit;
    private float x1, y1, x2, y2;
    private Path rectaPoligonal = new Path();

    public Vista(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas c) {

        pincel = new Paint();
        pincel.setAntiAlias(true);
        pincel.setStrokeWidth(5);
        pincel.setStyle(Paint.Style.STROKE);

        switch(MainActivity.indicadorColor) {
            case 1:
                pincel.setColor(Color.rgb(25, 118, 210));
                break;
            case 2:
                pincel.setColor(Color.rgb(211,47,47));
                break;
            case 3:
                pincel.setColor(Color.rgb(251,192,45));
                break;
        }

        if(MainActivity.indicador == 1){
            pincel.setStrokeWidth(10);
            pincel.setStyle(Paint.Style.FILL);
        }

        c.drawBitmap(mapaBit, 0, 0, null);
        c.drawPath(rectaPoligonal, pincel);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mapaBit = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        lienzo = new Canvas(mapaBit);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch(MainActivity.indicador) {
            case 1:
                metodoBrocha(event,x,y);
                break;
            case 2:
                metodoCurva(event,x,y);
                break;
            case 3:
                metodoLinea(event,x,y);
                break;
            case 4:
                metodoCuadrado(event,x,y);
                break;
            case 5:
                metodoCirculo(event,x,y);
                break;
        }
        invalidate();
        return true;
    }


    //Metodos-------------------------

    public void metodoBrocha(MotionEvent event, float x, float y){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = x;
                y1 = y;
                rectaPoligonal.moveTo(x1, y1);
                break;
            case MotionEvent.ACTION_MOVE:
                x2=x;
                y2=y;
                rectaPoligonal.quadTo(x2, y2,(x + x2) / 2, (y + y2)/2);
                break;
            case MotionEvent.ACTION_UP:
                x2=x;
                y2=y;
                lienzo.drawPath(rectaPoligonal, pincel);
                rectaPoligonal.reset();
                break;
        }
    }

    public void metodoCurva(MotionEvent event, float x, float y){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = x;
                y1 = y;
                rectaPoligonal.moveTo(x1, y1);
                break;
            case MotionEvent.ACTION_MOVE:
                x2=x;
                y2=y;
                rectaPoligonal.quadTo(x2, y2,(x + x2) / 2, (y + y2)/2);
                break;
            case MotionEvent.ACTION_UP:
                x2=x;
                y2=y;
                lienzo.drawPath(rectaPoligonal, pincel);
                rectaPoligonal.reset();
                break;
        }
    }

    public void metodoLinea(MotionEvent event, float x, float y){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1=x;
                y1=y;
                rectaPoligonal.moveTo(x1,y1);
                break;
            case MotionEvent.ACTION_MOVE:
                x2=x;
                y2=y;
                break;
            case MotionEvent.ACTION_UP:
                x2=x;
                y2=y;
                lienzo.drawLine(x1, y1, x2, y2, pincel);
                rectaPoligonal.reset();
                break;
        }
    }

    public void metodoCuadrado(MotionEvent event, float x, float y){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1=x;
                y1=y;
                rectaPoligonal.moveTo(x1,y1);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                x2=x;
                y2=y;
                lienzo.drawRect(x1, y1, x2, y2, pincel);
                rectaPoligonal.reset();
                break;
        }
    }

    public void metodoCirculo(MotionEvent event, float x, float y){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1=x;
                y1=y;
                rectaPoligonal.moveTo(x1,y1);
                break;
            case MotionEvent.ACTION_MOVE:
                x2=x;
                y2=y;
                break;
            case MotionEvent.ACTION_UP:
                float radio = (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1-y2,2));
                lienzo.drawCircle(x1, y1, radio, pincel);
                rectaPoligonal.reset();
                break;
        }
    }

    public void limpiar() {
        mapaBit = Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.ARGB_8888);
        lienzo = new Canvas(mapaBit);
        invalidate();
    }

    public void guardar(Context contexto){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), "foto.jpg");
        try {
            FileOutputStream out = new FileOutputStream(file);
            mapaBit.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(contexto, "Foto guardada", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargar(Bitmap bitMap){
        limpiar();
        lienzo.drawBitmap(bitMap,getLeft(),getTop(),pincel);
    }
}
