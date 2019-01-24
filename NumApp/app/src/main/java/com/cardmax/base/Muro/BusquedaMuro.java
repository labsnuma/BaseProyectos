package com.cardmax.base.Muro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.cardmax.base.Muro.Adaptadores.AdaptadorPrincipal;
import com.cardmax.base.Muro.Objetos.ObjetoMuro;
import com.cardmax.base.R;

import java.util.ArrayList;


public class BusquedaMuro extends AppCompatActivity {


    private SearchView searchView;
    private DatabaseReference mDatabase;
    private RecyclerView contenedorx;
    private ArrayList<ObjetoMuro> Lista;
    private AdaptadorPrincipal xx;
    private Toolbar barraact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muro_activity_busqueda_muro);
        String key = getIntent().getExtras().getString("key");

        searchView = (SearchView) findViewById(R.id.searchv);
        searchView.onActionViewExpanded();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {



                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText != null && newText.equals("")) {

                } else {
                    contenedorx = (RecyclerView) findViewById(R.id.recycler_busqueda);
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    Lista = new ArrayList<ObjetoMuro>();
                    xx = new AdaptadorPrincipal(Lista);
                    contenedorx.setHasFixedSize(true);
                    LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
                    layout.setOrientation(LinearLayoutManager.VERTICAL);
                    contenedorx.setAdapter(xx);
                    contenedorx.setLayoutManager(layout);

                    System.out.println(newText+"UTF-8");

                    Query q = mDatabase.child("muro_publicaciones")
                            .orderByChild("titulo_publicacion")
                            .startAt(newText)
                            .endAt(newText+"\uF8FF")
                            .limitToLast(10);





                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Lista.removeAll(Lista);
                            Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();
                            int count = 0;

                            for (DataSnapshot murox : muroChildren) {
                                ObjetoMuro p = murox.getValue(ObjetoMuro.class);
                                Lista.add(p);
                                count += 1;

                            }



                            Toast.makeText(getApplicationContext(), "Encontrados :" + count, Toast.LENGTH_LONG).show();


                            xx.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });



                }


                return false;
            }
        });

        if(!key.equals("") && key != null){
            contenedorx = (RecyclerView) findViewById(R.id.recycler_busqueda);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            Lista = new ArrayList<ObjetoMuro>();
            xx = new AdaptadorPrincipal(Lista);
            contenedorx.setHasFixedSize(true);
            LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
            layout.setOrientation(LinearLayoutManager.VERTICAL);
            contenedorx.setAdapter(xx);
            contenedorx.setLayoutManager(layout);
            searchView.clearFocus();


            mDatabase.child("muro_publicaciones")
                    .orderByChild("key")
                    .equalTo(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Lista.removeAll(Lista);
                    Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();
                    int count = 0;

                    for (DataSnapshot murox : muroChildren) {
                        ObjetoMuro p = murox.getValue(ObjetoMuro.class);
                        Lista.add(p);
                        count += 1;

                    }


                    xx.notifyDataSetChanged();



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


    }

    public void cerrar(View v) {

        finish();

    }


}
