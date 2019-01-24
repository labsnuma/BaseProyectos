package com.cardmax.base.Perfil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.cardmax.base.Chat.InicioActivity;
import com.cardmax.base.Muro.Adaptadores.AdaptadorPrincipal;
import com.cardmax.base.Muro.CrearPublicacion;
import com.cardmax.base.Muro.MuroMainActivity;
import com.cardmax.base.Muro.Objetos.ObjetoMuro;
import com.cardmax.base.Muro.Objetos.ObjetoSaveCard;
import com.cardmax.base.Muro.Objetos.ObjetoUser;
import com.cardmax.base.Perfil.Fragment.Card;
import com.cardmax.base.Perfil.Fragment.HeightWrappingViewPager;
import com.cardmax.base.Perfil.Fragment.MuropFragment;
import com.cardmax.base.Perfil.Fragment.SavexFragment;
import com.cardmax.base.Perfil.Fragment.ServiciosFragment;
import com.cardmax.base.Perfil.TabAdapter.PagerAdapter;
import com.cardmax.base.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilMainActivity extends AppCompatActivity implements Card.OnFragmentInteractionListener, MuropFragment.OnFragmentInteractionListener, ServiciosFragment.OnFragmentInteractionListener, SavexFragment.OnFragmentInteractionListener {
    private CircleImageView perfil;
    private Button cerrar, crear,  muro, edit;
    private DatabaseReference mDatabase;
    private RecyclerView contenedorx;
    private ArrayList<ObjetoMuro> Lista;
    private AdaptadorPrincipal xx;
    public String fotourl, iduser, name;
    private TextView nombre, carrera;
    private int contador = 0;
    ProgressBar simulated;
    View content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.perfil_activity_main);
        }catch (Exception e ){

            System.out.println("ERRRRRRORRRRRRR "+e);



        }

        edit = (Button) findViewById(R.id.btn_edit_perfil);
        // fotourl = getIntent().getExtras().getString("foto");
        iduser = getIntent().getExtras().getString("id_user");
        // name = getIntent().getExtras().getString("nombreperfil");
        nombre = (TextView) findViewById(R.id.txt_nombre_princial);
        carrera = (TextView) findViewById(R.id.txt_carrera_perfil);
        crear = (Button) findViewById(R.id.btn_crear_pub);
        perfil = (CircleImageView) findViewById(R.id.profile_image);
        cerrar = (Button) findViewById(R.id.btn_cerrar_perfil);

        muro = (Button) findViewById(R.id.btn_muro);
        simulated = (ProgressBar)findViewById(R.id.simulated);
        content = (View)findViewById(R.id.content);


        mDatabase = FirebaseDatabase.getInstance().getReference();
/*
        contenedorx = (RecyclerView) findViewById(R.id.recyclerview_perfil);

        Lista = new ArrayList<ObjetoMuro>();
        xx = new AdaptadorPrincipal(Lista);
        contenedorx.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        contenedorx.setAdapter(xx);
        contenedorx.setLayoutManager(layout);
        ViewCompat.setNestedScrollingEnabled(contenedorx, false);

        */
        TabLayout more = (TabLayout)findViewById(R.id.tabs) ;
        more.addTab(more.newTab().setText("Inicio"));
        more.addTab(more.newTab().setText("Mis publicaciones"));
        more.addTab(more.newTab().setText("Servicios"));
        more.addTab(more.newTab().setText("Publicaciones guardadas"));

        final HeightWrappingViewPager viewPager = ( HeightWrappingViewPager ) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),more.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(more));
        more.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intento;
                intento = new Intent(PerfilMainActivity.this, OpcionesActivity.class);
                intento.putExtra("foto", fotourl);
                intento.putExtra("id_user", iduser);
                intento.putExtra("nombreperfil", name);
                startActivity(intento);

                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);


            }
        });


        muro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                muro.setVisibility(View.GONE);


                Lista = new ArrayList<ObjetoMuro>();
                xx = new AdaptadorPrincipal(Lista);
                contenedorx.setHasFixedSize(true);
                LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
                layout.setOrientation(LinearLayoutManager.VERTICAL);
                contenedorx.setAdapter(xx);
                contenedorx.setLayoutManager(layout);

                final Query q = mDatabase.child("muro_publicaciones")
                        .orderByChild("id_usuario")
                        .equalTo(iduser);


                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Lista.removeAll(Lista);
                        Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();
                        int count = 0;

                        for (DataSnapshot murox : muroChildren) {
                            ObjetoMuro p = murox.getValue(ObjetoMuro.class);
                            Lista.add(0, p);
                            count += 1;
                        }
                        xx.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });



        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intento;
                intento = new Intent(PerfilMainActivity.this, CrearPublicacion.class);
                intento.putExtra("foto", fotourl);
                intento.putExtra("id_user", iduser);
                intento.putExtra("nombreperfil", name);
                startActivity(intento);
                overridePendingTransition(R.anim.goup, R.anim.hold);


            }
        });


        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String layout = getIntent().getExtras().getString("layout");

                if (layout.equals("1")){

                    Intent intento;
                    intento = new Intent(PerfilMainActivity.this, MuroMainActivity.class);
                    startActivity(intento);
                    finish();
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

                }else{
                    Intent intento;
                    intento = new Intent(PerfilMainActivity.this, InicioActivity.class);
                    startActivity(intento);
                    finish();
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

                }


            }
        });


        final Query datosperfil = mDatabase.child("Users").child(iduser);

        datosperfil.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    ObjetoUser p = dataSnapshot.getValue(ObjetoUser.class);

                    fotourl = p.image;
                    name = p.name;
                    carrera.setText(p.carrera);

                    Glide.with(getApplicationContext())
                            .load(fotourl)
                            .into(perfil);

                    nombre.setText(name);

                } catch (Exception e) {
                    System.out.println("error grande --> " + e);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        new Handler().postDelayed(new Runnable() {
            public void run() {

                simulated.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
            }
        }, 2000);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
