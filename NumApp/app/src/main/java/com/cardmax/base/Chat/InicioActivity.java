package com.cardmax.base.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cardmax.base.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.cardmax.base.Chat.Recursos.CerrarSesion;
import com.cardmax.base.Chat.Recursos.EstadoOnline;
import com.cardmax.base.Chat.Recursos.IraActividades;
import com.cardmax.base.Chat.Recursos.SalirAplicacion;
import com.cardmax.base.Muro.MuroMainActivity;
import com.cardmax.base.Perfil.PerfilMainActivity;


import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import me.leolin.shortcutbadger.ShortcutBadger;


public class InicioActivity extends AppCompatActivity implements View.OnClickListener {

    private BottomNavigationView navi;
    private FloatingActionButton fab;
    private FirebaseAuth mAuth;
    public CircleImageView imagenperfil;
    IraActividades actividadesir = new IraActividades();
    EstadoOnline estadoOnline = new EstadoOnline();

    String mAuthShared;
    public String currentUserID;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_chat:
                   // actividadesir.iraChatActivity(InicioActivity.this);
                    return true;
                case R.id.navigation_home:


                    Intent intento1;
                    intento1 = new Intent(InicioActivity.this, PerfilMainActivity.class);
                    intento1.putExtra("id_user", currentUserID);
                    intento1.putExtra("layout", "2");
                    startActivity(intento1);
                    finish();
                    overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);


                    return true;

                   /* String mFotoStorageString = "Imagen prueba/"+"Pruebas/";
                    String mNombreStorageString="mi_foto_final"+".jpg";
                    String mFotoDatabaseString = "Pruebas/" + "Imagen/"+"mas";
                    String mNombreDataString = "imagen";
                    Intent pruebaimagenes = new Intent(InicioActivity.this, SelectorImagenes.class);
                    pruebaimagenes.putExtra("stroageRef", mFotoStorageString);
                    pruebaimagenes.putExtra("nombreStorageRef", mNombreStorageString);
                    pruebaimagenes.putExtra("databaseRef", mFotoDatabaseString);
                    pruebaimagenes.putExtra("nombreDataRef", mNombreDataString);
                    startActivity(pruebaimagenes);
                     return true;
                    */


                case R.id.navigation_muro:
                    actividadesir.iraMuroActivity(InicioActivity.this);
                    overridePendingTransition(R.anim.goup, R.anim.godown);
                    return true;

            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_inicio);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        IniciarValores();
        ParametrosFireB();
        miraEstadoOnline();
        ShortcutBadger.applyCount(this, 2);

    }

    private void IniciarValores() {
        Toolbar mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);

        imagenperfil =findViewById(R.id.img_perfil);
        EditText searchbar =findViewById(R.id.editSearch);
        searchbar.setOnClickListener(this);

        ViewPager myViewPager = findViewById(R.id.main_tabs_pager);
        TabsAccessorAdapter myTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessorAdapter);

        TabLayout myTabLyaout = findViewById(R.id.main_tabs);
        myTabLyaout.setupWithViewPager(myViewPager);

        fab = findViewById(R.id.fab);
        fab.setEnabled(true);
        fab.setOnClickListener(this);

        navi =findViewById(R.id.navi_abajo);
        navi.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navi.setSelectedItemId(R.id.navigation_chat);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                actividadesir.iraContactos(InicioActivity.this);
                break;
            case R.id.editSearch:
                actividadesir.iraBuscarAmigosActivity(InicioActivity.this);
                overridePendingTransition(R.anim.goup, R.anim.hold);
                break;
        }

    }

    private void ParametrosFireB() {
        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        RootRef.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").exists()) {
                    String userImage = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();

                    final RequestOptions opciones = new RequestOptions()
                            .error(R.drawable.profile_image)
                            .placeholder(R.drawable.profile_image).fitCenter()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL);//para que quede guardada en chache y no vuelva a descargar

                    Glide.with(getApplicationContext())
                            .load(userImage).apply(opciones)
                            .into(imagenperfil);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void miraEstadoOnline() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuthShared = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        estadoOnline.actualizarStatus("Online", mAuthShared);
    }

    public void baroculta(View v) {
        navi.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.INVISIBLE);
    }

    public void barmuestra(View v) {
        navi.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
        fab.setEnabled(true);
    }

    public void barmuestra1(View v) {
        navi.setVisibility(View.VISIBLE);
        fab.setVisibility(View.INVISIBLE);
        fab.setEnabled(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.chat_opciones_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_amigo) {
            actividadesir.iraBuscarAmigosActivity(InicioActivity.this);
        }

        if (item.getItemId() == R.id.menu_opciones1) {
            actividadesir.iraOpcionesActivity(InicioActivity.this);
        }
        if (item.getItemId() == R.id.menu_log_out) {

            CerrarSesion cerrar = new CerrarSesion();
            cerrar.logout(this);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        SalirAplicacion salirdeaplicacion = new SalirAplicacion();
        salirdeaplicacion.ahora(this, InicioActivity.this, "Pulse otra vez para cerrar", 2500);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        estadoOnline.actualizarStatus("Online", mAuthShared);
    }

    @Override
    protected void onResume() {
        super.onResume();
        estadoOnline.actualizarStatus("Online", mAuthShared);
    }


    @Override
    protected void onPause() {
        super.onPause();
        estadoOnline.actualizarStatus("Offline", mAuthShared);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        estadoOnline.actualizarStatus("Offline", mAuthShared);
    }



}
