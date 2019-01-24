package com.numa.cardmax.numapp.Chat.MensajesChats;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.numa.cardmax.numapp.Chat.InicioActivity;
import com.numa.cardmax.numapp.Chat.Recursos.AudioAnimacion;
import com.numa.cardmax.numapp.Chat.Recursos.EstadoOnline;
import com.numa.cardmax.numapp.Chat.Recursos.FechaHora;
import com.numa.cardmax.numapp.Chat.Recursos.IraActividades;
import com.numa.cardmax.numapp.Chat.Recursos.PermisosPreguntar;
import com.numa.cardmax.numapp.Chat.Recursos.SelectorImagenes;
import com.numa.cardmax.numapp.Muro.Objetos.ObjetoNotificacionChat;
import com.numa.cardmax.numapp.Muro.Objetos.ObjetoUser;
import com.numa.cardmax.numapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;

//public class ChatActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener,MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {
public class ChatActivity extends YouTubeBaseActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, YouTubePlayer.OnInitializedListener {
//public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    private String senderUserID, visit_user_id, visit_user_name, visit_user_image, urlaudio;
    private String obtenerFotoSender;
    private ChildEventListener x;
    private TextView userLasTSeen;
    private FirebaseDatabase mDatabase;
    private DatabaseReference refDb;
    private DatabaseReference ChatMensajesRef, RootRef;
    private View layout;
    private FloatingActionButton sendMessageBtutton, audio;
    private EditText userMessageInput;
    DatabaseReference ref1, ref2;
    String code;
    private DatabaseReference nuevaref = FirebaseDatabase.getInstance().getReference();
    private final List<Messages> messagesList = new ArrayList<>();
    private MensajesAdapter mensajesAdapter;
    private RecyclerView userMensajesrRecyclerList;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String currentUserID = mAuth.getCurrentUser().getUid();
    PermisosPreguntar permisosPreguntar = new PermisosPreguntar(this);
    IraActividades iraActividades = new IraActividades();
    public MediaPlayer mediaPlayer;
    EstadoOnline estadoOnline = new EstadoOnline();
    FechaHora calendario = new FechaHora();
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
    private Button salaudio, test;
    private StorageReference ChatAudioRef;
    private Long nombre_imagen;
    private DatabaseReference ChatAudioRefDatabase;
    private TextView answer_user, color_answer, answer_messsage;
    private ImageView previous_image;
    private String a_user = "", a_multimedia = "", a_menssage = "";
    private View layout_answer;
    private String key_youtube = "AIzaSyB8MJf557XN9aDBtwFsD7rhhRMC77yBbeM";
    private YouTubePlayerView player_you;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_v);
        layout = (View) findViewById(R.id.record_layout);
        answer_user = (TextView) findViewById(R.id.answer_user_message);
        answer_messsage = (TextView) findViewById(R.id.answer_messsage);
        previous_image = (ImageView) findViewById(R.id.previous_image);
        layout_answer = (View) findViewById(R.id.visualizacion);


        player_you = (YouTubePlayerView) findViewById(R.id.youtube_view);




        permisosPreguntar.validapermisos(this);
        color_answer = (TextView) findViewById(R.id.color_answer);
        variablesFirebase();
        ObtenerFoto();
        iniciarValores();
        Ultimavez();
        servicio();
        miraEstadoOnline();

        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_enviar_mensaje);

        //audio
        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                //Start Recording..
                //Log.d("RecordView", "onStart");

                layout.setVisibility(View.VISIBLE);
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setOutputFile(mFileName);

                try {
                    mRecorder.prepare();
                    mRecorder.start();
                    userMessageInput.setHintTextColor(getResources().getColor(R.color.colorWhite));
                    userMessageInput.setEnabled(false);

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
                    layout.setVisibility(GONE);

                    mRecorder.stop();
                    mRecorder.release();
                    //  mRecorder = null;
                    userMessageInput.setHintTextColor(getResources().getColor(R.color.colorTextoMensaje));
                    userMessageInput.setEnabled(true);

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
                layout.setVisibility(GONE);

                try {
                    mRecorder.stop();
                    mRecorder.release();
                    //  mRecorder = null;
                    userMessageInput.setHintTextColor(getResources().getColor(R.color.colorTextoMensaje));
                    userMessageInput.setEnabled(true);

                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    Log.e("Errorstop: ", e.getMessage());
                }
                //  Toast.makeText(getApplicationContext(), "Grabacion detenida", Toast.LENGTH_SHORT).show();
                //reproductor.setVisibility(View.VISIBLE);
                enviaraudio(time);

                layout_answer.setVisibility(GONE);
                a_user = "";
                a_multimedia = "";
                a_menssage = "";
            }

            @Override
            public void onLessThanSecond() {
                try {
                    layout.setVisibility(GONE);

                    //   mRecorder.prepare();
                    //   saber=false;
//                    mRecorder.stop();
                    mRecorder.release();
                    //  mRecorder = null;
                    userMessageInput.setHintTextColor(getResources().getColor(R.color.colorTextoMensaje));
                    userMessageInput.setEnabled(true);

                } catch (IllegalStateException e) {
                    //  e.printStackTrace();
                    Log.e("Errorstop: ", e.getMessage());
                } catch (NullPointerException e) {
                    Log.e("Errorstop2: ", e.getMessage());
                }


            }
        });


        // audio
    }

    private void enviaraudio(String duracion) {
     /*   StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("audio/3gp")
                .build();*/
        Uri url = Uri.fromFile(new File(mFileName));
        urlaudio = String.valueOf(url);
        AudioAnimacion audioAnimacion = new AudioAnimacion();
        audioAnimacion.iniciarValoresFireBase(visit_user_id, urlaudio, duracion, this, a_user, a_multimedia,a_menssage);


/*        Intent ChatMensajeIntent = new Intent(ChatActivity.this, AudioAnimacion.class);
        ChatMensajeIntent.putExtra("visit_user_id", visit_user_id);
        ChatMensajeIntent.putExtra("visit_user_name", visit_user_name);
        ChatMensajeIntent.putExtra("visit_user_image", visit_user_image);
        ChatMensajeIntent.putExtra("visit_user_url", urlaudio);
        startActivity(ChatMensajeIntent);*/

    /*    salaudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {*/

           /*     StorageMetadata metadata = new StorageMetadata.Builder()
                        .setContentType("audio/3gp")
                        .build();
                Uri url = Uri.fromFile(new File(mFileName));
                FirebaseStorage storage = FirebaseStorage.getInstance();
                final StorageReference storageRef = storage.getReference();
                StorageReference mountainsRef = storageRef.child("Pruebas");

                UploadTask filepath = mountainsRef.child("audio_" + nombre_imagen + ".3gp").putFile(url, metadata);
                filepath.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "AUDIO ARRIBA",Toast.LENGTH_LONG).show();
                        storageRef.child("Pruebas/audio_" + nombre_imagen+ ".3gp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                            }
                        });



                    }
                });*/







      /*      }
        });*/


        mDatabase = FirebaseDatabase.getInstance();
        refDb = mDatabase.getReference();
        String id = refDb.push().getKey();
        ObjetoNotificacionChat registro = new ObjetoNotificacionChat(senderUserID, "Envio un audio");

        refDb.child("notification_user_chat").child(visit_user_id).child(id).setValue(registro);


    }

    //audio david
    private String getHumanTimeText(long milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    //audio david

    private void ObtenerFoto() {
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        UserRef.child(senderUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    obtenerFotoSender = dataSnapshot.child("image").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void variablesFirebase() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();
        ChatMensajesRef = FirebaseDatabase.getInstance().getReference().child("Chat Mensajes");

        visit_user_id = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("visit_user_id")).toString();
        visit_user_name = Objects.requireNonNull(getIntent().getExtras().get("visit_user_name")).toString();
        visit_user_image = Objects.requireNonNull(getIntent().getExtras().get("visit_user_image")).toString();
        senderUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        ref1 = ChatMensajesRef.child(senderUserID).child(visit_user_id);
        ref2 = ChatMensajesRef.child(visit_user_id).child(senderUserID);


    }

    private void iniciarValores() {

        Toolbar mToolbar = findViewById(R.id.chat_toolbar);


        CircleImageView userProfileImage = findViewById(R.id.fotoDePerfilSolicitud);
        Picasso.get().load(visit_user_image).placeholder(R.drawable.profile_image).into(userProfileImage);

        TextView userProfileName = findViewById(R.id.nombreamigo);
        userProfileName.setText(visit_user_name);

        userLasTSeen = findViewById(R.id.fecha);
        ImageButton botonatras = findViewById(R.id.atras);
        botonatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref1.removeEventListener(x);
                finish();
            }
        });

        userMessageInput = findViewById(R.id.input_message);
        sendMessageBtutton = findViewById(R.id.send_message_btn);
        sendMessageBtutton.setOnClickListener(this);
        ImageButton sendImage = findViewById(R.id.input_imagen);
        sendImage.setOnClickListener(this);

        // audio = findViewById(R.id.boton_micorfono);
        // audio.setOnClickListener(this);
        recordView = (RecordView) findViewById(R.id.record_view);
        recordButton = (RecordButton) findViewById(R.id.record_button);
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audio_grabado.3gp";
        recordButton.setRecordView(recordView);
        salaudio = findViewById(R.id.enviar_salida_audio);
        ChatAudioRef = FirebaseStorage.getInstance().getReference().child("Chat audios");
        ChatAudioRefDatabase = FirebaseDatabase.getInstance().getReference().child("Chat Audio pruebas");
        nombre_imagen = System.currentTimeMillis();
  /*      mediaPlayer1 = new MediaPlayer();
        mediaPlayer1.setOnBufferingUpdateListener(this);
        mediaPlayer1.setOnCompletionListener(this);*/

        userMessageInput.setHintTextColor(getResources().getColor(R.color.colorTextoMensaje));
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

        mensajesAdapter = new MensajesAdapter(this, messagesList, visit_user_image, obtenerFotoSender);
        userMensajesrRecyclerList = findViewById(R.id.private_messages_list_of_users);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ViewCompat.setNestedScrollingEnabled(userMensajesrRecyclerList, false);
        userMensajesrRecyclerList.setHasFixedSize(true);
        userMensajesrRecyclerList.setItemViewCacheSize(30);
        userMensajesrRecyclerList.setDrawingCacheEnabled(true);
        userMensajesrRecyclerList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        userMensajesrRecyclerList.setLayoutManager(linearLayoutManager);
        userMensajesrRecyclerList.setAdapter(mensajesAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.atras:
                Intent ChatFrgment = new Intent(ChatActivity.this, InicioActivity.class);
                startActivity(ChatFrgment);
                break;
            case R.id.send_message_btn:
                EnviarMensaje();
                userMessageInput.setText("");
                ReproducirAudiomsg();
               /* saveMessageInfoToDatabase();
//                mScroolView.fullScroll(ScrollView.FOCUS_DOWN);
                scrollView = (ScrollView) findViewById(R.id.scrollView);
*/
                break;
            case R.id.input_imagen:
                EnviarImagen();
                break;
        /*    case R.id.boton_micorfono:
                Intent ChatMensajeIntent = new Intent(ChatActivity.this, AudioChat.class);
                ChatMensajeIntent.putExtra("visit_user_id", visit_user_id);
                ChatMensajeIntent.putExtra("visit_user_name", visit_user_name);
                ChatMensajeIntent.putExtra("visit_user_image", visit_user_image);
                startActivity(ChatMensajeIntent);
                break;*/
        }


    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    private void ReproducirAudiomsg() {
        //   mediaPlayer = MediaPlayer.create(this, R.raw.sonido_enviar_mensaje);
        mediaPlayer.setOnPreparedListener(ChatActivity.this);
    }

    private void Ultimavez() {

        //    RootRef.child("Contactos").child(visit_user_id).child(senderUserID).addListenerForSingleValueEvent(new ValueEventListener() {
        RootRef.child("Contactos").child(visit_user_id).child(senderUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("estadoUsuario").hasChild("estado")) {
                    String estado = dataSnapshot.child("estadoUsuario").child("estado").getValue().toString();
                    String fecha = dataSnapshot.child("estadoUsuario").child("fecha").getValue().toString();
                    String hora = dataSnapshot.child("estadoUsuario").child("hora").getValue().toString();

                    if (estado.equals("Online")) {
                        userLasTSeen.setText("Online");
                    } else if (estado.equals("Offline")) {
                        userLasTSeen.setText(fecha + " " + hora);
                    } else if (estado.equals("Escribiendo")) {
                        userLasTSeen.setText("Escribiendo mensaje...");
                    }
                } else {
                    userLasTSeen.setText("Offline");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        RootRef.child("Users").child(visit_user_id).addValueEventListener(new ValueEventListener() {
            //  RootRef.child("Users").child(visit_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("estadoUsuario").hasChild("estado")) {
                    String estado = dataSnapshot.child("estadoUsuario").child("estado").getValue().toString();
                    String fecha = dataSnapshot.child("estadoUsuario").child("fecha").getValue().toString();
                    String hora = dataSnapshot.child("estadoUsuario").child("hora").getValue().toString();

                    if (estado.equals("Online")) {
                        userLasTSeen.setText("Online");
                    } else if (estado.equals("Offline")) {
                        userLasTSeen.setText(fecha + " " + hora);
                    } else if (estado.equals("Escribiendo")) {
                        userLasTSeen.setText("Escribiendo mensaje...");
                    }
                } else {
                    userLasTSeen.setText("Offline");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void miraEstadoOnline() {
        estadoOnline.actualizarStatus("Online", senderUserID);
        estadoOnline.actualizarStatusAmigos("Online", senderUserID, visit_user_id);
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

        x = ref1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Messages mensajes = dataSnapshot.getValue(Messages.class);
                messagesList.add(mensajes);
                mensajesAdapter.notifyDataSetChanged();
                userMensajesrRecyclerList.smoothScrollToPosition(userMensajesrRecyclerList.getAdapter().getItemCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //    Messages mensajes = dataSnapshot.getValue(Messages.class);
             /*  messagesList.clear();
               messagesList.add(mensajes);*/
                String nuevo = dataSnapshot.getKey();
                Toast.makeText(ChatActivity.this, nuevo, Toast.LENGTH_SHORT).show();
               /* servicio();
                messagesList.clear();*/
                mensajesAdapter.notifyDataSetChanged(); //si es asi pero solo mirar y obtener el key
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // messagesList.clear();
                mensajesAdapter.notifyDataSetChanged();
                //   Toast.makeText(ChatActivity.this, "Contacto Eliminado", Toast.LENGTH_SHORT).show();
                iraActividades.iraChatActivity(ChatActivity.this);
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
        String mensaje = userMessageInput.getText().toString().trim();

        if (TextUtils.isEmpty(mensaje)) {

            sendMessageBtutton.setVisibility(GONE);
            // audio.setVisibility(View.VISIBLE);
            //   audio.setEnabled(true);
            recordButton.setVisibility(View.VISIBLE);
            recordButton.setEnabled(true);
            sendMessageBtutton.setEnabled(false);
            estadoOnline.actualizarStatus("Online", senderUserID);
            estadoOnline.actualizarStatusAmigos("Online", senderUserID, visit_user_id);
        } else {

            sendMessageBtutton.setVisibility(View.VISIBLE);
            //    audio.setVisibility(View.GONE);
            //    audio.setEnabled(false);
            recordButton.setVisibility(GONE);
            recordButton.setEnabled(false);
            sendMessageBtutton.setEnabled(true);
            estadoOnline.actualizarStatusAmigos("Escribiendo", senderUserID, visit_user_id);

        }
    }

    private void EnviarMensaje() {

        calendario.getHora();
        String fecha = calendario.getGuardarFecha2();
        String hora = calendario.getGuardarhora1();

        String mensaje = userMessageInput.getText().toString().trim();
        String mensajeSenderRef1 = "Chat Mensajes/" + senderUserID + "/" + visit_user_id;
        String mensajeReciveRef2 = "Chat Mensajes/" + visit_user_id + "/" + senderUserID;

        DatabaseReference userMensajeKeyRef = ChatMensajesRef
                .child(senderUserID).child(visit_user_id).push();

        String mensajePushID = userMensajeKeyRef.getKey();

        Map<String, String> mensajeDeTexto = new HashMap<>();
        mensajeDeTexto.put("mensaje", mensaje);
        mensajeDeTexto.put("tipo", "texto");
        mensajeDeTexto.put("fecha", fecha);
        mensajeDeTexto.put("hora", hora);
        mensajeDeTexto.put("from", senderUserID);
        mensajeDeTexto.put("visto", "0");
        mensajeDeTexto.put("key", mensajePushID);
        mensajeDeTexto.put("answer_multimedia", a_multimedia);
        mensajeDeTexto.put("answer_user", a_user);
        mensajeDeTexto.put("answer_mensagge", a_menssage);



        Map<String, Object> mensajeDeTextoDetalles = new HashMap<>();
        mensajeDeTextoDetalles.put(mensajeSenderRef1 + "/" + mensajePushID, mensajeDeTexto);
        mensajeDeTextoDetalles.put(mensajeReciveRef2 + "/" + mensajePushID, mensajeDeTexto);

        RootRef.updateChildren(mensajeDeTextoDetalles).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //     Toast.makeText(ChatActivity.this, "Mensaje Enviado", Toast.LENGTH_SHORT).show();
                    //     userMessageInput.setText("");
                } else {
                    String mensajeError = Objects.requireNonNull(task.getException()).toString();
                    Toast.makeText(ChatActivity.this, "Error: " + mensajeError, Toast.LENGTH_SHORT).show();
                }


            }
        });


        mDatabase = FirebaseDatabase.getInstance();
        refDb = mDatabase.getReference();
        String id = refDb.push().getKey();
        ObjetoNotificacionChat registro = new ObjetoNotificacionChat(senderUserID, mensaje);

        refDb.child("notification_user_chat").child(visit_user_id).child(id).setValue(registro);



        layout_answer.setVisibility(GONE);
        a_user = "";
        a_multimedia = "";
        a_menssage = "";

    }

    public void EnviarImagen() {
        calendario.getHora();
        String hora = calendario.getGuardarhora2();

        String imagenstorage = "Chat imagenes/" + senderUserID + "/" + visit_user_id;
        String imagenNombre = "imagen_" + hora + ".jpg";
        int opcion = 2;

        Intent pruebaimagenes = new Intent(ChatActivity.this, SelectorImagenes.class);
        pruebaimagenes.putExtra("stroageRef", imagenstorage);
        pruebaimagenes.putExtra("nombreStorageRef", imagenNombre);
        pruebaimagenes.putExtra("opcion", opcion);
        pruebaimagenes.putExtra("nDatasender", senderUserID);
        pruebaimagenes.putExtra("nDatareciver", visit_user_id);

        pruebaimagenes.putExtra("databaseRef", "vacio");
        pruebaimagenes.putExtra("nombreDataRef", "vacio");
        pruebaimagenes.putExtra("grupoRef", "vacio");
        pruebaimagenes.putExtra("nFotoRef", "vacio");
        pruebaimagenes.putExtra("answer_multimedia", a_multimedia);
        pruebaimagenes.putExtra("answer_user", a_user);
        pruebaimagenes.putExtra("answer_mensagge", a_menssage);
        startActivity(pruebaimagenes);
        layout_answer.setVisibility(GONE);
        a_user = "";
        a_multimedia = "";
        a_menssage = "";


    }

   /* public void paraReproduccion() {
        try {
            mediaPlayer = new MediaPlayer();
        //    mediaPlayer.setOnBufferingUpdateListener((MediaPlayer.OnBufferingUpdateListener) this);
       //     mediaPlayer.setOnCompletionListener((MediaPlayer.OnCompletionListener) this);
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        } catch (IllegalStateException e) {
            Log.e("Error #32: ", e.getMessage());
        }

    }*/


    @Override
    public void onBackPressed() {
        mensajesAdapter.onBackPressed();
        // MensajesAdapter mensajesAdapter=new MensajesAdapter();
        Intent ChatMensajeIntent = new Intent(ChatActivity.this, InicioActivity.class);
        startActivity(ChatMensajeIntent);

    }

    @Override
    protected void onRestart() {
        super.onRestart();


        estadoOnline.actualizarStatus("Online", senderUserID);
        estadoOnline.actualizarStatusAmigos("Online", senderUserID, visit_user_id);

    }

    @Override
    protected void onResume() {
        super.onResume();


        estadoOnline.actualizarStatus("Online", senderUserID);
        estadoOnline.actualizarStatusAmigos("Online", senderUserID, visit_user_id);

    }

    @Override
    protected void onPause() {
        super.onPause();

        // mensajesAdapter.EstaSonando();
        estadoOnline.actualizarStatus("Offline", senderUserID);
        estadoOnline.actualizarStatusAmigos("Offline", senderUserID, visit_user_id);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //  mensajesAdapter.PararAudio();
        mensajesAdapter.onBackPressed();
        estadoOnline.actualizarStatus("Offline", senderUserID);
        estadoOnline.actualizarStatusAmigos("Offline", senderUserID, visit_user_id);

    }

    public void ocultar(String from, String message, String multimedia) {

        Button closed = (Button) findViewById(R.id.closed_visualizacion);


        a_user = from;
        a_multimedia = multimedia;
        a_menssage = message;


        if (senderUserID.equals(from)) {

            answer_user.setText("Tú");
            answer_messsage.setText(message);
            answer_user.setTextColor(Color.parseColor("#0e84d8"));
            color_answer.setBackgroundColor(Color.parseColor("#0e84d8"));

            if (message.equals("Mensaje de voz")) {
                previous_image.setVisibility(GONE);

                answer_messsage.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_mic_black_18dp, 0, 0, 0);
            } else if (message.equals("Imagen")) {

                answer_messsage.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_photo_camera_black_18dp, 0, 0, 0);

                Glide.with(getApplicationContext())
                        .load(multimedia)
                        .into(previous_image);
                previous_image.setVisibility(View.VISIBLE);

            } else if (message.equals("Publicacion Compartida")) {


                previous_image.setVisibility(GONE);

                answer_messsage.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_share_black_18dp, 0, 0, 0);


            } else {
                previous_image.setVisibility(GONE);
                answer_messsage.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            }


        } else {


            DatabaseReference xDatabase = FirebaseDatabase.getInstance().getReference();

            xDatabase.child("Users").child(from).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name;
                            ObjetoUser userx = dataSnapshot.getValue(ObjetoUser.class);
                            name = userx.name; // "John Doe"
                            answer_user.setText(name);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


            answer_messsage.setText(message);
            answer_user.setTextColor(Color.parseColor("#8bd80f"));
            color_answer.setBackgroundColor(Color.parseColor("#8bd80f"));


            if (message.equals("Mensaje de voz")) {
                previous_image.setVisibility(GONE);

                answer_messsage.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_mic_black_18dp, 0, 0, 0);
            } else if (message.equals("Imagen")) {

                answer_messsage.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_photo_camera_black_18dp, 0, 0, 0);

                Glide.with(getApplicationContext())
                        .load(multimedia)
                        .into(previous_image);
                previous_image.setVisibility(View.VISIBLE);

            } else if (message.equals("Publicacion Compartida")) {


                previous_image.setVisibility(GONE);

                answer_messsage.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_share_black_18dp, 0, 0, 0);


            } else {
                previous_image.setVisibility(GONE);
                answer_messsage.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            }


        }


        closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layout_answer.setVisibility(GONE);
                a_user = "";
                a_multimedia = "";
                a_menssage = "";

            }
        });
        layout_answer.setVisibility(View.VISIBLE);


    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
        if(!b) {



            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);




            Query listening = nuevaref.child("tmp").child(currentUserID);

            listening.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    youTubePlayer.loadVideo(code);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });






        }


    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }


    public void showyoutube(String x){


         String id = nuevaref.push().getKey();

        nuevaref.child("tmp").child(currentUserID).child(id).setValue("1");


        final View youtube_view = (View)findViewById(R.id.youtube_view);
        final View chat_toolbar= (View)findViewById(R.id.chat_toolbar);
        final Button btn_hide_youtube = (Button)findViewById(R.id.btn_hide_youtube);
        code = x;
        youtube_view.setVisibility(View.VISIBLE);
        chat_toolbar.setVisibility(View.GONE);
        btn_hide_youtube.setVisibility(View.VISIBLE);


        player_you = (YouTubePlayerView) findViewById(R.id.youtube_view);
        player_you.clearFocus();
        player_you.initialize(key_youtube, this);





        btn_hide_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youtube_view.setVisibility(View.GONE);
                btn_hide_youtube.setVisibility(View.GONE);
                chat_toolbar.setVisibility(View.VISIBLE);



                player_you.destroyDrawingCache();

            }
        });




    }


}
