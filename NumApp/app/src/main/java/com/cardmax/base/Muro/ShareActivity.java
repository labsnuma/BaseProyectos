package com.cardmax.base.Muro;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.cardmax.base.Muro.Adaptadores.AdaptadorComentarios;
import com.cardmax.base.Muro.Adaptadores.AdaptadorShare;
import com.cardmax.base.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ShareActivity extends AppCompatActivity {

    private RecyclerView lista_contenedor;
    private AdaptadorShare xx;
    private DatabaseReference mDatabase;
    private List<String> listaObjeto;
    private String publicacion, currentUserID;
    private FirebaseAuth mAuth;
    private Button close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muro_activity_share);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        publicacion = getIntent().getExtras().getString("key");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        listaObjeto = new ArrayList<String>();
        lista_contenedor = (RecyclerView)findViewById(R.id.recycler_contactos);
        close = (Button)findViewById(R.id.btn_close_enviar);
        xx = new AdaptadorShare(listaObjeto);
        lista_contenedor.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        lista_contenedor.setAdapter(xx);
        lista_contenedor.setLayoutManager(layout);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





        Query q = mDatabase.child("Chat Mensajes")
                .child(currentUserID)
                ;


        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();
                listaObjeto.removeAll(listaObjeto);

                for (DataSnapshot murox : muroChildren) {
                    listaObjeto.add(murox.getKey());

                    System.out.println(murox);

                }
                listaObjeto.add(publicacion);
                xx.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
