package com.cardmax.base.Chat.Grupos.Grupov2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.cardmax.base.Chat.InicioActivity;
import com.cardmax.base.Chat.Recursos.AudioAnimacion;
import com.cardmax.base.Chat.Recursos.AudioAnimacionGrupo;
import com.cardmax.base.Chat.Recursos.FechaHora;
import com.cardmax.base.Chat.Recursos.PermisosPreguntar;
import com.cardmax.base.Chat.Recursos.SelectorImagenes;
import com.cardmax.base.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class GrupoActivityv2 extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView userProfileImage;
    private TextView userProfileName, nombreGrupo;

   // private FloatingActionButton sendMessageBtutton, audio;
    private FloatingActionButton sendMessageBtutton;
    private EditText userMessageInput;

    private final List<MessagesGrupo> messagesList = new ArrayList<>();
    private GruposAdapter mensajesAdapter;
    private RecyclerView userMensajesrRecyclerList;

    PermisosPreguntar permisosPreguntar = new PermisosPreguntar(this);

    public String currentGroupName;
    private DatabaseReference GroupNameRef;
    private String userID, userName, userImage;
    ////
    private MediaRecorder mRecorder;
    private String mFileName = null;
    //  private View reproductor;
    private SeekBar control;
    private int realtimeLength;
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private int mediaFileLenght;
    final Handler handler = new Handler();
    public MediaPlayer mediaPlayer1;
    private RecordView recordView;
    private RecordButton recordButton;
    private Button salaudio;
    private StorageReference ChatAudioRef;
    private Long nombre_imagen;
    private DatabaseReference  ChatAudioRefDatabase;
    private String urlaudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_v_grupo);

        permisosPreguntar.validapermisos(this);
        currentGroupName = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("groupName")).toString();
        iniciarValores();
        ParametrosFireB();
        servicio();

        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                //Start Recording..
                //Log.d("RecordView", "onStart");

                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setOutputFile(mFileName);

                try {
                    mRecorder.prepare();
                    mRecorder.start();
                    userMessageInput.setHintTextColor(getResources().getColor(R.color.colorWhite));
                    userMessageInput.setEnabled(true);

                    //userMessageInput.setText("");
                } catch (IllegalStateException e) {
                    Log.e("Erroraudio1: ", e.getMessage());
                } catch (IOException e) {
                    Log.e("Erroraudio: ", e.getMessage());
                }

                //   Toast.makeText(getApplicationContext(), "Grabando audio", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancel() {
                //On Swipe To Cancel
                //   Log.d("RecordView", "onCancel");
                try {
                    mRecorder.stop();
                    mRecorder.release();
                    //  mRecorder = null;
                    userMessageInput.setHintTextColor(getResources().getColor(R.color.colorTextoMensaje));
                    userMessageInput.setEnabled(false);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Errorstop: ", e.getMessage());
                }
            }

            @Override
            public void onFinish(long recordTime) {
                //Stop Recording..
                String time = getHumanTimeText(recordTime);
                //   Log.d("RecordView", "onFinish");

                try {
                    mRecorder.stop();
                    mRecorder.release();
                    //  mRecorder = null;
                    userMessageInput.setHintTextColor(getResources().getColor(R.color.colorTextoMensaje));
                    userMessageInput.setEnabled(false);

                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    Log.e("Errorstop: ", e.getMessage());
                }
                enviaraudio(time);
              //  Toast.makeText(GrupoActivityv2.this, time, Toast.LENGTH_SHORT).show();




            }

            @Override
            public void onLessThanSecond() {
                try {
                    mRecorder.release();
                    userMessageInput.setHintTextColor(getResources().getColor(R.color.colorTextoMensaje));
                    userMessageInput.setEnabled(true);

                } catch (IllegalStateException e) {
                    //  e.printStackTrace();
                    Log.e("Errorstop: ", e.getMessage());
                } catch (NullPointerException e){
                    Log.e("Errorstop2: ", e.getMessage());
                }


            }
        });

    }

    //audio david
    private String getHumanTimeText(long milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    //audio david

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.atras:
                Intent ChatFrgment = new Intent(GrupoActivityv2.this, InicioActivity.class);
                startActivity(ChatFrgment);
                break;
            case R.id.send_message_btn:
                EnviarMensaje();
                userMessageInput.setText("");
                break;
           /* case R.id.boton_micorfono:
               // EnviarAudio();
                break;*/
            case R.id.input_imagen:
                EnviarImagen();
                break;
        }

    }
    private void enviaraudio(String duracion) {
     /*   StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("audio/3gp")
                .build();*/
        Uri url = Uri.fromFile(new File(mFileName));
        urlaudio = String.valueOf(url);
        AudioAnimacionGrupo audioAnimacionGrupo = new AudioAnimacionGrupo();
        audioAnimacionGrupo.iniciarValoresFireBase(currentGroupName,userID, urlaudio, duracion, this);
    }
/*    private void EnviarAudio() {
        Toast.makeText(this, "grabar", Toast.LENGTH_SHORT).show();
//                Intent ChatMensajeIntent = new Intent(GrupoActivityv2.this, AudioChat.class);
//                ChatMensajeIntent.putExtra("visit_user_id",visit_user_id);
//                ChatMensajeIntent.putExtra("visit_user_name",visit_user_name);
//                ChatMensajeIntent.putExtra("visit_user_image",visit_user_image);
//
//                startActivity(ChatMensajeIntent);
    }*/

    private void EnviarImagen() {
        FechaHora calendario = new FechaHora();
        calendario.getHora();
        String hora = calendario.getGuardarhora2();

        String imagenstorage = "Chat Grupos imagenes /" + currentGroupName + "/" + userID;
        String imagenNombre = "imagen_" + hora + ".jpg";

        Intent pruebaimagenes = new Intent(GrupoActivityv2.this, SelectorImagenes.class);
        pruebaimagenes.putExtra("stroageRef", imagenstorage);
        pruebaimagenes.putExtra("nombreStorageRef", imagenNombre);
        pruebaimagenes.putExtra("opcion", 3);
        pruebaimagenes.putExtra("nDatasender", userID);
        pruebaimagenes.putExtra("nombreDataRef", userName);
        pruebaimagenes.putExtra("nFotoRef", userImage);
        pruebaimagenes.putExtra("grupoRef", currentGroupName);

        pruebaimagenes.putExtra("nDatareciver", "vacio");
        pruebaimagenes.putExtra("databaseRef", "vacio");
        startActivity(pruebaimagenes);
    }

    private void iniciarValores() {

        Toolbar  mToolbar =findViewById(R.id.chat_toolbar);
        setSupportActionBar(mToolbar);

        userProfileImage = findViewById(R.id.fotoDePerfilSolicitud);
        userProfileName = findViewById(R.id.nombreamigo);
        nombreGrupo = findViewById(R.id.fecha);
        nombreGrupo.setVisibility(View.VISIBLE);
        ImageButton botonatras = findViewById(R.id.atras);
        botonatras.setOnClickListener(this);

        userMessageInput =  findViewById(R.id.input_message);
        sendMessageBtutton =  findViewById(R.id.send_message_btn);
        sendMessageBtutton.setOnClickListener(this);
        ImageButton senderimagen = findViewById(R.id.input_imagen);
        senderimagen.setOnClickListener(this);

       /* audio = findViewById(R.id.boton_micorfono);
        audio.setEnabled(false);
        audio.setOnClickListener(this);*/

       //audio
        recordView = (RecordView) findViewById(R.id.record_view);
        recordButton = (RecordButton) findViewById(R.id.record_button);
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audio_grabado.3gp";
        recordButton.setRecordView(recordView);
       // salaudio =findViewById(R.id.enviar_salida_audio);
        ChatAudioRef = FirebaseStorage.getInstance().getReference().child("Chat audios");
        ChatAudioRefDatabase = FirebaseDatabase.getInstance().getReference().child("Chat Audio pruebas");
        nombre_imagen = System.currentTimeMillis();
        //fin audio

        userMessageInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                siesvacio();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                siesvacio();
            }

            @Override
            public void afterTextChanged(Editable s) {
                siesvacio();
            }
        });

       // mensajesAdapter = new GruposAdapter(messagesList);
        mensajesAdapter = new GruposAdapter(this,messagesList);
        userMensajesrRecyclerList = findViewById(R.id.private_messages_list_of_users);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        userMensajesrRecyclerList.setLayoutManager(linearLayoutManager);
        userMensajesrRecyclerList.setAdapter(mensajesAdapter);

    }

    private void ParametrosFireB() {
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Grupos").child(currentGroupName);
        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        RootRef.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").exists()) {
                    userName = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                    userImage = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();
                    userProfileName.setText(userName);
                    nombreGrupo.setText(currentGroupName);

                    final RequestOptions opciones = new RequestOptions()
                            .error(R.drawable.profile_image)
                            .placeholder(R.drawable.profile_image).fitCenter()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL);//para que quede guardada en chache y no vuelva a descargar

                    Glide.with(getApplicationContext())
                            .load(userImage).apply(opciones)
                            .into(userProfileImage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            } else {
                permisosPreguntar.solicitarpermisomanual(this);
            }
        }
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();

    }

    private void servicio() {
        GroupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MessagesGrupo mensajes = dataSnapshot.getValue(MessagesGrupo.class);
                messagesList.add(mensajes);
                mensajesAdapter.notifyDataSetChanged();

                userMensajesrRecyclerList.smoothScrollToPosition(userMensajesrRecyclerList.getAdapter().getItemCount());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void siesvacio() {
        String mensaje = userMessageInput.getText().toString().trim();//"   hola  " => "hola"

        if (TextUtils.isEmpty(mensaje)) {

            sendMessageBtutton.setVisibility(View.GONE);
            /*audio.setVisibility(View.VISIBLE);
            audio.setEnabled(true);*/
            recordButton.setVisibility(View.VISIBLE);
            recordButton.setEnabled(true);
            sendMessageBtutton.setEnabled(false);
        } else {

            sendMessageBtutton.setVisibility(View.VISIBLE);
            /*audio.setVisibility(View.GONE);
            audio.setEnabled(false)*/;
            recordButton.setVisibility(View.GONE);
            recordButton.setEnabled(false);
            sendMessageBtutton.setEnabled(true);

        }
    }

    private void EnviarMensaje() {

        FechaHora calendario = new FechaHora();
        calendario.getHora();
        String fecha = calendario.getGuardarFecha2();
        String hora = calendario.getGuardarhora1();

        String mensajekey = GroupNameRef.push().getKey();
        DatabaseReference GroupMessageKeyRef = GroupNameRef.child(Objects.requireNonNull(mensajekey));

        String mensaje = userMessageInput.getText().toString().trim();

        HashMap<String, Object> groupMessageKey = new HashMap<>();
        GroupNameRef.updateChildren(groupMessageKey);

        HashMap<String, Object> mensajeInfoMap = new HashMap<>();
        mensajeInfoMap.put("fecha", fecha);
        mensajeInfoMap.put("hora", hora);
        mensajeInfoMap.put("from", userID);
        mensajeInfoMap.put("nombre", userName);
        mensajeInfoMap.put("mensaje", mensaje);
        mensajeInfoMap.put("tipo", "texto");
        mensajeInfoMap.put("foto", userImage);

        GroupMessageKeyRef.updateChildren(mensajeInfoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(GrupoActivityv2.this, "Mensaje Enviado", Toast.LENGTH_SHORT).show();
                } else {
                    String mensajeError = Objects.requireNonNull(task.getException()).toString();
                    Toast.makeText(GrupoActivityv2.this, "Error: " + mensajeError, Toast.LENGTH_SHORT).show();
                }
                userMessageInput.setText("");
            }
        });
    }


}
