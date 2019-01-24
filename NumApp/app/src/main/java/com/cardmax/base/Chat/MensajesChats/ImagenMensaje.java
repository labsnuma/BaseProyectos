package com.cardmax.base.Chat.MensajesChats;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.cardmax.base.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.senab.photoview.PhotoViewAttacher;


//public class ImagenMensaje extends AppCompatActivity implements PhotoViewAttacher.OnViewTapListener {
public class ImagenMensaje extends AppCompatActivity  {

    private Toolbar mToolbar;
    private ImageButton botonatras;
    private TextView userProfileName, userLasTSeen;
    private CircleImageView userProfileImage;

    private ImageView imagen;
    private String imagenurl, nombre, fecha, hora;
    private int opcion;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    // private String date;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_fragment_chat_imagen);

        iniciarValores();
        obtenerValores();
        preguntar();


        botonatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Picasso.get().load(imagenurl).placeholder(R.drawable.profile_image).into(imagen);
    }

    private void preguntar() {
        if(opcion==1){
            userProfileName.setText(nombre);
            userLasTSeen.setText(fecha + hora);
        }
        else if(opcion==2){
            obtenerNombre();
        }

    }

    private void obtenerValores() {
        imagenurl = getIntent().getExtras().get("imagen").toString();
        nombre = getIntent().getExtras().get("nombre").toString();
        hora = getIntent().getExtras().get("hora").toString();
        fecha = getIntent().getExtras().get("fecha").toString();
        opcion= (int) getIntent().getExtras().get("opcion");

    }

    private void iniciarValores() {
        mToolbar = findViewById(R.id.chat_toolbar_imagen);
        setSupportActionBar(mToolbar);


        userProfileImage = (CircleImageView) findViewById(R.id.fotoDePerfilSolicitud);
        userProfileName = (TextView) findViewById(R.id.nombreamigo);
        userLasTSeen = (TextView) findViewById(R.id.fecha);
        botonatras = (ImageButton) findViewById(R.id.atras);

        imagen = (ImageView) findViewById(R.id.imagen_seleccioanda);
        PhotoViewAttacher photoView = new PhotoViewAttacher(imagen);
        photoView.update();
    //    photoView.setOnViewTapListener(ImagenMensaje.this);
        photoView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                mToolbar.setVisibility(View.VISIBLE);
                return false;
            }
        });

        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View view, float x, float y) {
                mToolbar.setVisibility(View.GONE);
            }

            @Override
            public void onOutsidePhotoTap() {
             //   Toast.makeText(ImagenMensaje.this, "estado4", Toast.LENGTH_SHORT).show();
            }
        });
    /*    photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {

            @Override
            public void onViewTap(View view, float x, float y) {
                Toast.makeText(ImagenMensaje.this, "estado5", Toast.LENGTH_SHORT).show();
            }
        });*/
    }



   /* public void onViewTap(View view, float x, float y) {
        // your code here


    }*/
/*
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        if (hasCapture == true) {
            Toast.makeText(this, "llll", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "llasdasdll", Toast.LENGTH_SHORT).show();

    }*/

    private void obtenerNombre() {
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.child("Users").child(nombre).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").exists()) {
                  String userName = dataSnapshot.child("name").getValue().toString();
                  String userImage = dataSnapshot.child("image").getValue().toString();
                    userProfileName.setText(userName);
                    userProfileImage.setVisibility(View.VISIBLE);
                     Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(userProfileImage);
                    userLasTSeen.setText(fecha + hora);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
