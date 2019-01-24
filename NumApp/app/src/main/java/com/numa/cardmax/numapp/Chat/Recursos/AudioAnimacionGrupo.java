package com.numa.cardmax.numapp.Chat.Recursos;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import dyanamitechetan.vusikview.VusikView;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.numa.cardmax.numapp.Chat.MensajesChats.ChatActivity;
import com.numa.cardmax.numapp.Chat.Recursos.FechaHora;
import com.numa.cardmax.numapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import dyanamitechetan.vusikview.VusikView;

public class AudioAnimacionGrupo extends AppCompatActivity {

    private MediaRecorder mRecorder;
    private MediaPlayer mediaPlayer;
    /*private String mFileName = null;*/

    private ProgressDialog progreso;
    private StorageReference ChatAudioRef;

/*    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;*/


    private DatabaseReference ChatMensajesRef, RootRef;
    private FirebaseAuth mAuth;
    //  private String senderUserID, visit_user_id, visit_user_name, visit_user_image,visit_user_url;
    private String senderUserID;
    // DatabaseReference ref1, ref2;
    private String fecha, hora;

    private Toolbar mToolbar;
    private CircleImageView userProfileImage;
    private TextView userProfileName, grabandoAudio;
    private ImageButton botonatras;

    private ImageButton btn_play_play;
    private SeekBar seekBar;
    private TextView tiempo_reproduccion;
    private VusikView vusikView;
    private int mediaFileLenght;
    private int realtimeLength;
    final Handler handler = new Handler();

    private FloatingActionButton grabar, detener, subirfire, nuevo;
    private RelativeLayout reproductor;
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    FechaHora calendario = new FechaHora();


    public void iniciarValoresFireBase(String nombregrupo,String id, String visit_user_url, String duracion, Context context) {
        Calendar calendario = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDateFormat = new SimpleDateFormat("MM dd, yyyy");
        fecha = currentDateFormat.format(calendario.getTime());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm aaa");
        hora = currentTimeFormat.format(calendario.getTime());


        mAuth = FirebaseAuth.getInstance();
        ChatMensajesRef = FirebaseDatabase.getInstance().getReference().child("Grupos");
        ChatAudioRef = FirebaseStorage.getInstance().getReference().child("Chat audios grupos");
        RootRef = FirebaseDatabase.getInstance().getReference();

      /*  visit_user_id = getIntent().getExtras().get("visit_user_id").toString();
        visit_user_name = getIntent().getExtras().get("visit_user_name").toString();
        visit_user_image = getIntent().getExtras().get("visit_user_image").toString();
        visit_user_url = getIntent().getExtras().get("visit_user_url").toString();*/
        senderUserID = mAuth.getCurrentUser().getUid();
      /*  ref1 = ChatMensajesRef.child(senderUserID).child(visit_user_id);
        ref2 = ChatMensajesRef.child(visit_user_id).child(senderUserID);*/
        subirarchivo( nombregrupo,id, visit_user_url, duracion, context);
    }

    private void subirarchivo(final String nombregrupo, final String visit_user_id, String visit_user_url, final String duracion, final Context context) {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
        Date hora = Calendar.getInstance().getTime();
        String dataFormatada = formato.format(hora);
        //para el storage
        progreso = new ProgressDialog(context);
        progreso.setTitle("Subiendo Audio");
        progreso.show();

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("audio/3gp")
                .build();
        //  Uri url = Uri.fromFile(new File(mFileName));
        Uri url = Uri.parse(visit_user_url);
        UploadTask filepath = ChatAudioRef.child(nombregrupo).child(senderUserID).child("audio_" + dataFormatada + ".3gp").putFile(url, metadata);


        filepath.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progreso.setMessage("Subiendo " + ((int) progress) + "%...");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Carga pausada");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //     progreso.dismiss();
                //   Toast.makeText(AudioChat.this, "Archivo subio a storage", Toast.LENGTH_SHORT).show();
                final String downloadUrl = taskSnapshot.getMetadata().getDownloadUrl().toString();
                Log.i("MENSAJE", downloadUrl);
                subirparametros(nombregrupo, downloadUrl, visit_user_id, duracion, context);

            }


        });


    }

    private void subirparametros(String nombregrupo, String urlaudio, String visit_user_id, String duracion, final Context context) {
        calendario.getHora();
        String fecha = calendario.getGuardarFecha2();
        String hora = calendario.getGuardarhora1();
        String fullh = calendario.getGuardarhora3();
        String nombre = "audio" + "_" + fullh;


        String mensajeSenderRef1 = "Grupos/" + nombregrupo;
       // String mensajeReciveRef2 = "Chat Mensajes/" + visit_user_id + "/" + senderUserID;

        DatabaseReference userMensajeKeyRef = ChatMensajesRef
                .child(nombregrupo).push();

        String mensajePushID = userMensajeKeyRef.getKey();

        // String duration = String.valueOf(mediaPlayer.getDuration());
      //  String duration = "50";
        //  String duration = String.valueOf(chronometer);
        Map<String, String> mensajeDeTexto = new HashMap<>();
        mensajeDeTexto.put("mensaje", "");
        mensajeDeTexto.put("audio", urlaudio);
        mensajeDeTexto.put("tipo", "audio");
        mensajeDeTexto.put("fecha", fecha);
        mensajeDeTexto.put("ngrupo", nombregrupo);
        mensajeDeTexto.put("nombre", nombre);
        mensajeDeTexto.put("hora", hora);
        mensajeDeTexto.put("from", senderUserID);
        mensajeDeTexto.put("duracion", duracion);

        Map<String, Object> mensajeDeTextoDetalles = new HashMap<>();
        mensajeDeTextoDetalles.put(mensajeSenderRef1 + "/" + mensajePushID, mensajeDeTexto);
     //   mensajeDeTextoDetalles.put(mensajeReciveRef2 + "/" + mensajePushID, mensajeDeTexto);
        RootRef.updateChildren(mensajeDeTextoDetalles).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progreso.dismiss();
                    Toast.makeText(context, "Mensaje Enviado", Toast.LENGTH_SHORT).show();
                    //irAtras();
                } else {
                    String mensajeError = Objects.requireNonNull(task.getException()).toString();
                    Toast.makeText(context, "Error: " + mensajeError, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

/*    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        btn_play_play.setImageResource(R.drawable.ic_play);
        vusikView.clearAnimation();
        vusikView.clearFocus();
        vusikView.pauseNotesFall();
        vusikView.setVisibility(View.GONE);
    }*/

    /*   public void irAtras() {
     *//*      pauseCronometro();
        resetCronometro();
        paraReproduccion();*//*
        Intent ChatMensajeIntent = new Intent(this, ChatActivity.class);
        ChatMensajeIntent.putExtra("visit_user_id", visit_user_id);
        ChatMensajeIntent.putExtra("visit_user_name", visit_user_name);
        ChatMensajeIntent.putExtra("visit_user_image", visit_user_image);
        startActivity(ChatMensajeIntent);

    }

    @Override
    public void onBackPressed() {
        irAtras();
    }
*/

}
