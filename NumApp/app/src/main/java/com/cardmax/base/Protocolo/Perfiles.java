package com.cardmax.base.Protocolo;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.cardmax.base.R;

public class Perfiles extends AppCompatActivity {

    private Button btnAux;
    private Button botonAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocolo_perfiles);
        setActionBar();


        //Abrir actividad de perfil protocolo_Administrador de tienda
       botonAdmin = (Button) findViewById(R.id.boton_Administrador);
       botonAdmin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intentAdministra = new Intent(Perfiles.this, protocolo_Administrador.class);
               startActivity(intentAdministra);
           }
       });

        //Abrir actividad de perfil protocolo_Auxiliar de prevención
        btnAux = (Button) findViewById(R.id.btn_aux);
        btnAux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intetnAux = new Intent(Perfiles.this, protocolo_Auxiliar.class);
                startActivity(intetnAux);
            }
        });


    }

    private void setActionBar() {

        setTitle("Perfiles");
    }
}
