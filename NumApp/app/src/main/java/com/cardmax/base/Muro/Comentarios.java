package com.cardmax.base.Muro;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cardmax.base.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.cardmax.base.Muro.Adaptadores.AdaptadorComentarios;
import com.cardmax.base.Muro.Adaptadores.AdaptadorPrincipal;
import com.cardmax.base.Muro.Objetos.ObjetoComentario;
import com.cardmax.base.Muro.Objetos.ObjetoMuro;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Comentarios extends AppCompatActivity {

    private Button atras, enviar;
    private EditText comentario;
    private DatabaseReference mDatabase;
    private Intent intent;
    private RecyclerView lista_contenedor;
    private AdaptadorComentarios xx;
    private ArrayList<ObjetoComentario> Lista;
    private String publicacion;
    private Calendar c = Calendar.getInstance();
    private String fecha_publicacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muro_activity_comentarios);
        atras = (Button)findViewById(R.id.btn_atras_comentarios);
        enviar = (Button)findViewById(R.id.btn_comentario_enviar);
        comentario = (EditText)findViewById(R.id.edit_comentario) ;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        intent = getIntent();
        publicacion = intent.getStringExtra("id_publicacion");
        Lista = new ArrayList<ObjetoComentario>();
        lista_contenedor= (RecyclerView)findViewById(R.id.recycler_comentarios);

        xx = new AdaptadorComentarios(Lista);
        lista_contenedor.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        lista_contenedor.setAdapter(xx);
        lista_contenedor.setLayoutManager(layout);
        fecha_publicacion = DateFormat.getDateInstance().format(c.getTime());


        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String variable, id, user;
                id = mDatabase.push().getKey();


                user = intent.getStringExtra("id_user");

                variable = comentario.getText().toString();

                if (variable.isEmpty()){
                    Toast.makeText(getApplicationContext(), "¿Sin Comentarios?",Toast.LENGTH_SHORT).show();

                }else {

                    ObjetoComentario comentario_user = new ObjetoComentario( publicacion,user ,variable, fecha_publicacion);
                    mDatabase.child("muro_relacion_comentarios").child(id).setValue(comentario_user);
                    comentario.setText("");
                    Toast.makeText(getApplicationContext(), "Enviado",Toast.LENGTH_SHORT).show();


                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    try {
                        mDatabase.child("muro_publicaciones").child(publicacion).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        ObjetoMuro card = dataSnapshot.getValue(ObjetoMuro.class);

                                        int num_public = card.comentarios_publicacion + 1; // "Texas"
                                        mDatabase.child("muro_publicaciones").child(publicacion).child("comentarios_publicacion").setValue(num_public);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    } catch (Exception e) {
                        System.out.println(e);
                    }






                }




            }
        });




        Query q = mDatabase.child("muro_relacion_comentarios")
                .orderByChild("id_publicacion")
                .equalTo(publicacion);

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();
                Lista.removeAll(Lista);

                for (DataSnapshot list_comentario : muroChildren) {
                    ObjetoComentario p = list_comentario.getValue(ObjetoComentario.class);
                    Lista.add(p);

                }
                xx.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });













    }


}
