package com.numa.cardmax.numapp.Protocolo;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.numa.cardmax.numapp.R;

public class Entornos extends AppCompatActivity {

    private Button  btn_NoveRejas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocolo_entornos);
        setActionBar();


        btn_NoveRejas = (Button) findViewById(R.id.btn_NovRejas);
        btn_NoveRejas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNoveRejas = new Intent(Entornos.this, ReporteNovedad.class);
                startActivity(intentNoveRejas);
            }
        });

    }





    private void setActionBar() {
    setTitle("Verificación de entorno");

    }
}
