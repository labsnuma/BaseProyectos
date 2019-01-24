package com.cardmax.base.Protocolo;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.cardmax.base.R;


public class protocolo_Administrador extends AppCompatActivity {


    private Button btn_Entornos;
    private Button btn_Sistema;
    private Button btn_Vigilante;
    private Button btn_Auxiliar;
    private Button btn_AdmonValores;
    private Button btn_Regresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocolo_administrador);
        setActionBar();

        //Abrir actividad de entornos desde el administrador
        btn_Entornos = (Button) findViewById(R.id.btn_entorno);
        btn_Entornos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEntorno = new Intent(protocolo_Administrador.this, Entornos.class);
                startActivity(intentEntorno);

            }
        });


        //Abrir actividad de Sistema de seguridad

        btn_Sistema = (Button) findViewById(R.id.btn_sistema);
        btn_Sistema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSistema = new Intent(protocolo_Administrador.this, SistemaSeguridad.class);
                startActivity(intentSistema);
            }
        });


        //Abrir Actividad de Vigilante PERFIL
        btn_Vigilante = (Button) findViewById(R.id.btn_vigilante);
        btn_Vigilante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVigilante = new Intent(protocolo_Administrador.this, Vigilante.class);
                startActivity(intentVigilante);
            }
        });


        //Abrir actividad de protocolo_Auxiliar de Prevencion PERFIL
        btn_Auxiliar = (Button) findViewById(R.id.btn_prevencion);
        btn_Auxiliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAuxiliarPrev = new Intent(protocolo_Administrador.this, AuxiliarPrevencion.class);
                startActivity(intentAuxiliarPrev);
            }
        });


        //Abrir Actividad de Administracion de valores

        btn_AdmonValores = (Button) findViewById(R.id.btn_AdmonVal);
        btn_AdmonValores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdminVal = new Intent(protocolo_Administrador.this, AdministracionValores.class);
                startActivity(intentAdminVal);
            }
        });

        //Boton para regresar

        btn_Regresar = (Button) findViewById(R.id.btn_atras);
        btn_Regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegresar = new Intent(protocolo_Administrador.this, Perfiles.class);
                startActivity(intentRegresar);
            }
        });

    }





    private void setActionBar() {
        setTitle("protocolo_Administrador de tienda");

    }
}
