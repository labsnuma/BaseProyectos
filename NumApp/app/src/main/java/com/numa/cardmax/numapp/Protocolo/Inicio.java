package com.numa.cardmax.numapp.Protocolo;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.numa.cardmax.numapp.R;

public class Inicio extends AppCompatActivity {

    private Button btn_Inicio;
    private static final String PATH_START = "start";
    private static final String PATH_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocolo_inicio);


    //  final TextView tvMessage = findViewById(R.id.tvMessage);

        //Variable de base de datos
    //   FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Referencia a ruta para leer o ecribir mensaje anidado
   //     final DatabaseReference reference = database.getReference(PATH_START).child(PATH_MESSAGE);

        //Consultar dato
     /*  reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               tvMessage.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Inicio.this, "Error al consultar en Firebase.",
                        Toast.LENGTH_LONG).show();
            }
        }    );*/

        btn_Inicio = (Button) findViewById(R.id.btn_Inicio);
        btn_Inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentInicio = new Intent(Inicio.this, Perfiles.class);
                startActivity(intentInicio);
            }
        });

    }

}
