package com.numa.cardmax.numapp.Protocolo;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.numa.cardmax.numapp.R;

public class protocolo_Auxiliar extends AppCompatActivity {

    private Button Entornosbtn;        //Variable Boton Entornos
    private Button Fisicabtn;          //Variable Boton Seguridad física
    private Button Electronicabtn;     //Variable boton seguridad electrónica
    private Button Mercanciabtn;       //Variable boton mercancia
    private Button Regresarbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocolo_main);
        setActionBar();



//Abrir actividad Entornos
        Entornosbtn = (Button) findViewById(R.id.btn_entorno);
        Entornosbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentEntornos = new Intent(protocolo_Auxiliar.this, Entornos.class);
                startActivity(intentEntornos);
            }
        });


//Abrir actividad Seguridad Fisica
        Fisicabtn = (Button) findViewById(R.id.btn_fisica);
        Fisicabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentFisica = new Intent(protocolo_Auxiliar.this, SeguridadFisica.class);
                startActivity(intentFisica);
            }
        });

        //Arbri actividad seguridad electrónica
        Electronicabtn = (Button) findViewById(R.id.btn_electronica);
        Electronicabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentElectronica = new Intent(protocolo_Auxiliar.this, SeguridadElectronica.class);
                startActivity(intentElectronica);
            }
        });


        //Abrir actividad proteccion de mercancia
        Mercanciabtn = (Button) findViewById(R.id.btn_mercancia);
        Mercanciabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intetnMercancia = new Intent(protocolo_Auxiliar.this, ProteccionMercancia.class);
                startActivity(intetnMercancia);
            }
        });


        //Habilitar boton de regresar
        Regresarbtn = (Button) findViewById(R.id.btn_atras);
        Regresarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack = new Intent(protocolo_Auxiliar.this, Perfiles.class);
                        startActivity(intentBack);
            }
        });



    }

    private void setActionBar() {
        setTitle("protocolo_Auxiliar de prevención");
    }
}