package com.example.rafa.pdmdpaint;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout layout;
    private Vista v;
    private Button bt;
    public static int indicador = 1;
    public static int indicadorColor = 1;
    public static Uri uri;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        v = new Vista(this);
        layout = (RelativeLayout) findViewById(R.id.layout);
        layout.addView(v);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.limpiar();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.azul) {
            indicadorColor = 1;
        }else if(id ==  R.id.rojo){
            indicadorColor = 2;
        }else if(id ==  R.id.amarillo){
            indicadorColor = 3;
        }else if(id == R.id.action_guardar){
            v.guardar(this);
        }else if(id == R.id.action_cargar){
            cargar();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            Bitmap thumbnail = data.getParcelableExtra("data");
            Uri uri = data.getData();
            if (uri != null) { //Obtiene la Uri de la imagen
                Uri imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                v.cargar(bitmap);
            }
        }
    }

    //Selector----------------------

    public void brocha(View v){
        indicador = 1;
    }

    public void curva(View v){
        indicador = 2;
    }

    public void linea(View v){
        indicador  = 3;
    }

    public void cuadrado(View v){
        indicador = 4;
    }

    public void circulo(View v){
        indicador = 5;
    }

    public void cargar(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }
}
