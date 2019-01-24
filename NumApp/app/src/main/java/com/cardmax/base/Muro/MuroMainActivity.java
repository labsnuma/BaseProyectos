package com.cardmax.base.Muro;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cardmax.base.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.cardmax.base.Chat.InicioActivity;
import com.cardmax.base.Chat.LoginActivity;
import com.cardmax.base.Chat.Recursos.CerrarSesion;
import com.cardmax.base.Chat.Recursos.EstadoOnline;
import com.cardmax.base.Chat.Recursos.SalirAplicacion;
import com.cardmax.base.Chat.Services.ConfigShared;
import com.cardmax.base.Muro.Fragmentos.CarnetFragment;
import com.cardmax.base.Muro.Fragmentos.MuroFragment;
import com.cardmax.base.Muro.Fragmentos.NavegadorFragment;
import com.cardmax.base.Muro.Fragmentos.PerfilFragment;
import com.cardmax.base.Muro.Objetos.ObjetoComentario;
import com.cardmax.base.Perfil.OpcionesActivity;
import com.cardmax.base.Perfil.PerfilMainActivity;
import com.cardmax.base.Protocolo.FragmentProtocolo1;

import com.cardmax.base.Services.MuroServ;

import de.hdodenhof.circleimageview.CircleImageView;

public class MuroMainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private FragmentProtocolo1 protocolo;
    private FrameLayout mainFrame;
    private MuroFragment murofrag;
    private PerfilFragment notifrag;
    private CarnetFragment perfilfrag;
    private NavegadorFragment navegador_web;
    private Toolbar mTopToolbar;
    private EditText searchbar;
    private BottomNavigationView navigation;
    private FloatingActionButton agregar;
    private DatabaseReference UserRef;
    public String currentUserID, fotoperfil, nombreuser, rol;
    private FirebaseAuth mAuth;
    private CircleImageView perfil;
    private DatabaseReference RootRef;
    private FirebaseUser currentUser;
    EstadoOnline estadoOnline = new EstadoOnline();
    public DatabaseReference mrootShared;
    String mAuthShared;

    public String alert;
    private String key_youtube="AIzaSyB8MJf557XN9aDBtwFsD7rhhRMC77yBbeM";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:


                    Intent intento1;
                    intento1 = new Intent(MuroMainActivity.this, PerfilMainActivity.class);
                    intento1.putExtra("foto", fotoperfil);
                    intento1.putExtra("id_user", currentUserID);
                    intento1.putExtra("layout", "1");
                    startActivity(intento1);
                    finish();
                    overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);

                    return true;
                case R.id.navigation_dashboard:
                    agregar.setVisibility(View.VISIBLE);
                    setFrame(murofrag);
                    return true;
              /*  case R.id.navigation_notifications:
                    agregar.setVisibility(View.INVISIBLE);
                    setFrame(notifrag);
                    return true;*/

/*
                case R.id.Protocolo:
                    agregar.setVisibility(View.VISIBLE);
                    setFrame(protocolo);
                    return true;


*/
                case R.id.Chat:
                    Intent intento;
                    intento = new Intent(MuroMainActivity.this, InicioActivity.class);
                    startActivity(intento);
                    overridePendingTransition(R.anim.goup, R.anim.hold);
                    return true;
            }
            return false;
        }


    };




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.muro_navigation, menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muro_activity_main);
        try {
            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();
            RootRef = FirebaseDatabase.getInstance().getReference();

            if (currentUser == null) {
                SendUserToLoginActivity();
                return;
            } else {
                VerificarSiUsuarioExsiste();

            }
            UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
            currentUserID = mAuth.getCurrentUser().getUid();
            miraEstadoOnline();
        } catch (Exception e) {
            Log.e("Error1: ", e.getMessage());
        }

        perfil = (CircleImageView) findViewById(R.id.img_perfil);
        searchbar = (EditText) findViewById(R.id.editSearch);
        mTopToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        murofrag = new MuroFragment();
        perfilfrag = new CarnetFragment();
        notifrag = new PerfilFragment();
        navegador_web = new NavegadorFragment();
        protocolo = new FragmentProtocolo1();

        mainFrame = (FrameLayout) findViewById(R.id.mainfragment);
        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        agregar = (FloatingActionButton) findViewById(R.id.floatingActionButton3);
        navigation.setSelectedItemId(R.id.navigation_dashboard);


        startService(new Intent(getApplicationContext(), MuroServ.class));








        try {
            perfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intento;
                    intento = new Intent(MuroMainActivity.this, PerfilMainActivity.class);
                    intento.putExtra("foto", fotoperfil);
                    intento.putExtra("id_user", currentUserID);
                    intento.putExtra("nombreperfil", nombreuser);
                    startActivity(intento);
                    finish();
                    overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);



                }
            });
        } catch (Exception e) {

            System.out.println("mi ERRROR = " + e);
        }
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento;
                intento = new Intent(MuroMainActivity.this, CrearPublicacion.class);
                intento.putExtra("foto", fotoperfil);
                intento.putExtra("id_user", currentUserID);
                intento.putExtra("nombreperfil", nombreuser);
                intento.putExtra("rol",rol);
                startActivity(intento);
                overridePendingTransition(R.anim.goup, R.anim.hold);

            }
        });

        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intento;
                intento = new Intent(MuroMainActivity.this, BusquedaMuro.class);
                intento.putExtra("key", "");
                startActivity(intento);
                overridePendingTransition(R.anim.goup, R.anim.hold);

            }
        });

        GetUserInfo();




    }
    private void miraEstadoOnline() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuthShared = mAuth.getCurrentUser().getUid();
        estadoOnline.actualizarStatus("Online", mAuthShared);
    }

    private void VerificarSiUsuarioExsiste() {
        //
        final String currentUserID = mAuth.getCurrentUser().getUid();
    RootRef.child("Users").child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.child("name").exists()) {
                String userName = dataSnapshot.child("name").getValue().toString();
                SharedPreferences sharedPreferences = getSharedPreferences(ConfigShared.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(ConfigShared.LOGGEDIN_SHARED_PREF, true);
                editor.putString(ConfigShared.SHARED_NAME_CARGADO, userName);
                editor.putString(ConfigShared.CURRENTUSERID_SHARED_PREF, currentUserID);
                editor.apply();

            } else {
                SendUserToOpcionesActivity();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }

    private void SendUserToLoginActivity() {
        Intent LoginIntent = new Intent(MuroMainActivity.this, LoginActivity.class);
        LoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(LoginIntent);
        finish();
    }

    private void SendUserToOpcionesActivity() {
        Intent OpcionesIntent = new Intent(MuroMainActivity.this, OpcionesActivity.class);
        startActivity(OpcionesIntent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.actualizar) {
            Toast.makeText(getApplicationContext(), "Actualizado", Toast.LENGTH_SHORT).show();
            FragmentTransaction transcicion = getSupportFragmentManager().beginTransaction();
            transcicion.detach(murofrag);
            transcicion.attach(murofrag);
            transcicion.commit();

            return true;
        }

        if (id == R.id.menu_web) {


            try{

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // Get the layout inflater
                LayoutInflater inflater = this.getLayoutInflater();
                View mView = inflater.inflate(R.layout.muro_navegador_dialogo, null);
                final EditText url = (EditText)mView.findViewById(R.id.urltxt);

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(mView)
                        // Add action buttons
                        .setPositiveButton("Abrir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // sign in the user ...
                                alert = url.getText().toString();
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .detach(navegador_web)
                                        .attach(navegador_web)
                                        .commit();
                                setFrame(navegador_web);
                            }
                        })

                        .show();


               //

            }catch (Exception e){
                System.out.println(e);

            }



            return true;
        }


        if (item.getItemId() == R.id.menu_log_out) {
         /*   mAuth.signOut();
            SendUserToLoginActivity();*/
            CerrarSesion cerrar = new CerrarSesion();
            cerrar.logout(this);
        }
        return super.onOptionsItemSelected(item);





    }

    public void baroculta(View v) {

        agregar.setVisibility(View.GONE);
        navigation.setVisibility(View.GONE);
    }

    public void barmuestra(View v) {

        agregar.setVisibility(View.VISIBLE);
        navigation.setVisibility(View.VISIBLE);
    }

    public void GetUserInfo() {

        try{
        UserRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    nombreuser = dataSnapshot.child("name").getValue().toString();
                    currentUserID = dataSnapshot.child("uid").getValue().toString();
                    fotoperfil = dataSnapshot.child("image").getValue().toString();
                    rol = dataSnapshot.child("rol").getValue().toString();


                    RequestOptions options = new RequestOptions();


                    Glide.with(getApplicationContext())
                            .load(fotoperfil)
                            .apply(RequestOptions.circleCropTransform())

                            .into(perfil);
                }







            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }catch (Exception e){
            System.out.println("MIRAME  == "+e);
        }

    }


    @Override
    protected void onRestart() {
        super.onRestart();

        if (currentUserID != null) {
            estadoOnline.actualizarStatus("Online", mAuthShared);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (currentUserID != null) {
           estadoOnline.actualizarStatus("Online", mAuthShared);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (currentUserID != null) {
            estadoOnline.actualizarStatus("Offline", mAuthShared);
        }
    }

     @Override
    protected void onDestroy() {
        super.onDestroy();

        if (currentUserID != null) {
           estadoOnline.actualizarStatus("Offline", mAuthShared);
        }
    }

    @Override
    public void onBackPressed() {
        SalirAplicacion salirdeaplicacion = new SalirAplicacion();
        salirdeaplicacion.ahora(this, MuroMainActivity.this, "Pulse otra vez para cerrar", 2500);
    }


    private void setFrame(Fragment fragment) {
        FragmentTransaction transcicion = getSupportFragmentManager().beginTransaction();
        transcicion.replace(R.id.mainfragment, fragment);
        transcicion.commit();
    }




}
