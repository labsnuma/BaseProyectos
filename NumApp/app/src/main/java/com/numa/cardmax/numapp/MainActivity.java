package com.numa.cardmax.numapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.numa.cardmax.numapp.Muro.MuroMainActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocolo_main);
        System.out.println("Estoy Funcionando Bien!!");
    }

    public void muro(View v) {
        Intent intento;
        intento = new Intent(MainActivity.this, MuroMainActivity.class);
        startActivity(intento);


    }

}
