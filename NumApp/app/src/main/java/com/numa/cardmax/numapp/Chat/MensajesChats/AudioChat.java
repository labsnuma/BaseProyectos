package com.numa.cardmax.numapp.Chat.MensajesChats;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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

public class AudioChat extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {

    private MediaRecorder mRecorder;
    private MediaPlayer mediaPlayer;
    private String mFileName = null;

    private ProgressDialog progreso;
    private StorageReference ChatAudioRef;

    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;


    private DatabaseReference ChatMensajesRef, RootRef;
    private FirebaseAuth mAuth;
    private String senderUserID, visit_user_id, visit_user_name, visit_user_image;
    DatabaseReference ref1, ref2;
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_chat_audio);

        iniciarValoresFireBase();
        iniciarToolbar();

        iniciarValores();
        iniciarRecursosGrabacion();
        iniciarRecursosReproduccion();

        userProfileName.setText(visit_user_name);
        Picasso.get().load(visit_user_image).placeholder(R.drawable.profile_image).into(userProfileImage);

        grabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });

        detener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });

        btn_play_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reporducirAudio();
            }
        });


        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mediaPlayer.isPlaying()) {
                    SeekBar seekBar = (SeekBar) v;
                    int playPosition = (mediaFileLenght / 100) * seekBar.getProgress();
                    mediaPlayer.seekTo(playPosition);
                }
                return false;
            }
        });


        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseCronometro();
                resetCronometro();
                paraReproduccion();
                Intent mIntent = getIntent();
                finish();
                startActivity(mIntent);
            }
        });

        subirfire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirarchivo();
            }
        });

        botonatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAtras();
            }
        });

    }

    private void reporducirAudio() {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<String, String, String> mp3Play = new AsyncTask<String, String, String>() {

            @Override
            protected String doInBackground(String... strings) {

                try {
                    mediaPlayer.setDataSource(strings[0]);
                    mediaPlayer.prepare();
                } catch (Exception e) {

                }
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                mediaFileLenght = mediaPlayer.getDuration();
                realtimeLength = mediaFileLenght;
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    btn_play_play.setImageResource(R.drawable.ic_pause);
                } else {
                    mediaPlayer.pause();
                    btn_play_play.setImageResource(R.drawable.ic_play);
                }
                updateseekbar();

            }
        };

        mp3Play.execute(mFileName);

        vusikView.start();
        vusikView.setVisibility(View.VISIBLE);
    }

    private void iniciarToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(mToolbar);

        userProfileImage = (CircleImageView) findViewById(R.id.fotoDePerfilSolicitud);
        userProfileName = (TextView) findViewById(R.id.nombreamigo);
        grabandoAudio = (TextView) findViewById(R.id.fecha);
        botonatras = (ImageButton) findViewById(R.id.atras);

        grabandoAudio.setText("Listo para Grabar");

    }

    private void iniciarValores() {

        grabar = (FloatingActionButton) findViewById(R.id.grabar_audio);
        detener = (FloatingActionButton) findViewById(R.id.grabar_stop);
        subirfire = (FloatingActionButton) findViewById(R.id.subir);
        nuevo = (FloatingActionButton) findViewById(R.id.nuevo);

        subirfire.setVisibility(View.GONE);
        detener.setVisibility(View.GONE);

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audio_grabado.3gp";

        Calendar calendario = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDateFormat = new SimpleDateFormat("MM dd, yyyy");
        fecha = currentDateFormat.format(calendario.getTime());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm aaa");
        hora = currentTimeFormat.format(calendario.getTime());


        //streaming audio
        vusikView = (VusikView) findViewById(R.id.music_view);
        btn_play_play = (ImageButton) findViewById(R.id.btn_play_pause);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setMax(99);
        tiempo_reproduccion = (TextView) findViewById(R.id.text_timer);
        reproductor = (RelativeLayout) findViewById(R.id.layout_reproductor);
        reproductor.setVisibility(View.GONE);

        chronometer = findViewById(R.id.text_timer_grabacion);
        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime());


    }

    private void iniciarValoresFireBase() {
        mAuth = FirebaseAuth.getInstance();
        ChatMensajesRef = FirebaseDatabase.getInstance().getReference().child("Chat Mensajes");
        ChatAudioRef = FirebaseStorage.getInstance().getReference().child("Chat audios");
        RootRef = FirebaseDatabase.getInstance().getReference();

        visit_user_id = getIntent().getExtras().get("visit_user_id").toString();
        visit_user_name = getIntent().getExtras().get("visit_user_name").toString();
        visit_user_image = getIntent().getExtras().get("visit_user_image").toString();
        senderUserID = mAuth.getCurrentUser().getUid();
        ref1 = ChatMensajesRef.child(senderUserID).child(visit_user_id);
        ref2 = ChatMensajesRef.child(visit_user_id).child(senderUserID);
    }

    private void iniciarRecursosGrabacion() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(mFileName);

    }

    private void iniciarRecursosReproduccion() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    private void updateseekbar() {
        seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLenght) * 100));
        if (mediaPlayer.isPlaying()) {
            Runnable updater = new Runnable() {
                @SuppressLint("DefaultLocale")
                @Override
                public void run() {
                    updateseekbar();
                    realtimeLength -= 1000; // declare 1 second
                    tiempo_reproduccion.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(realtimeLength),
                            TimeUnit.MILLISECONDS.toSeconds(realtimeLength) -
                                    TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(realtimeLength))));

                }

            };
            handler.postDelayed(updater, 1000); // 1 second
        }

    }


    private void iniciarCronometro() {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    public void pauseCronometro() {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    private void resetCronometro() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }


    private void startRecording() {
        iniciarRecursosGrabacion();
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IllegalStateException e) {
            Log.e("Erroraudio1: ", e.getMessage());
        } catch (IOException e) {
            Log.e("Erroraudio: ", e.getMessage());
        }
        iniciarCronometro();
        grabandoAudio.setText("Grabando Audio");
        grabar.setVisibility(View.GONE);
        subirfire.setVisibility(View.GONE);
        detener.setVisibility(View.VISIBLE);
        Toast.makeText(AudioChat.this, "Grabando audio", Toast.LENGTH_SHORT).show();
    }

    private void stopRecording() {
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            grabandoAudio.setText("Audio");
            subirfire.setVisibility(View.VISIBLE);
            detener.setVisibility(View.GONE);
            grabar.setVisibility(View.GONE);
            nuevo.setVisibility(View.VISIBLE);
            reproductor.setVisibility(View.VISIBLE);
            pauseCronometro();
            chronometer.setVisibility(View.GONE);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.e("Errorstop: ", e.getMessage());
        }
        Toast.makeText(AudioChat.this, "Grabacion detenida", Toast.LENGTH_SHORT).show();
    }


    public void paraReproduccion() {
        try {
            mediaPlayer.stop();
        } catch (IllegalStateException e) {
            Log.e("Error #32: ", e.getMessage());
            //   Toast.makeText(AudioChat.this, "Error para Reproduccion: ", Toast.LENGTH_SHORT).show();
        }
    }


    private void subirarchivo() {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
        Date hora = Calendar.getInstance().getTime();
        String dataFormatada = formato.format(hora);
        //para el storage
        progreso = new ProgressDialog(this);
        progreso.setTitle("Subiendo Audio");
        progreso.show();

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("audio/3gp")
                .build();
        Uri url = Uri.fromFile(new File(mFileName));
        UploadTask filepath = ChatAudioRef.child(senderUserID).child(visit_user_id).child("audio_" + dataFormatada + ".3gp").putFile(url, metadata);


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
                subirparametros(downloadUrl);

            }



        });


    }

    private void subirparametros(String urlaudio) {
        calendario.getHora();
        String fecha = calendario.getGuardarFecha2();
        String hora = calendario.getGuardarhora1();
        String fullh = calendario.getGuardarhora3();
        String nombre = "audio" + "_" + fullh ;


        String mensajeSenderRef1 = "Chat Mensajes/" + senderUserID + "/" + visit_user_id;
        String mensajeReciveRef2 = "Chat Mensajes/" + visit_user_id + "/" + senderUserID;

        DatabaseReference userMensajeKeyRef = ChatMensajesRef
                .child(senderUserID).child(visit_user_id).push();

        String mensajePushID = userMensajeKeyRef.getKey();

       String duration = String.valueOf(mediaPlayer.getDuration());
     //  String duration = String.valueOf(chronometer);
        Map<String, String> mensajeDeTexto = new HashMap<>();
        mensajeDeTexto.put("mensaje", "");
        mensajeDeTexto.put("audio", urlaudio);
        mensajeDeTexto.put("tipo", "audio");
        mensajeDeTexto.put("fecha", fecha);
        mensajeDeTexto.put("nombre", nombre);
        mensajeDeTexto.put("hora", hora);
        mensajeDeTexto.put("from", senderUserID);
        mensajeDeTexto.put("duracion", duration);

        Map<String, Object> mensajeDeTextoDetalles = new HashMap<>();
        mensajeDeTextoDetalles.put(mensajeSenderRef1 + "/" + mensajePushID, mensajeDeTexto);
        mensajeDeTextoDetalles.put(mensajeReciveRef2 + "/" + mensajePushID, mensajeDeTexto);
        RootRef.updateChildren(mensajeDeTextoDetalles).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progreso.dismiss();
                    Toast.makeText(AudioChat.this, "Mensaje Enviado", Toast.LENGTH_SHORT).show();
                    irAtras();
                } else {
                    String mensajeError = Objects.requireNonNull(task.getException()).toString();
                    Toast.makeText(AudioChat.this, "Error: " + mensajeError, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
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
    }

    public void irAtras() {
        pauseCronometro();
        resetCronometro();
        paraReproduccion();
        Intent ChatMensajeIntent = new Intent(AudioChat.this, ChatActivity.class);
        ChatMensajeIntent.putExtra("visit_user_id", visit_user_id);
        ChatMensajeIntent.putExtra("visit_user_name", visit_user_name);
        ChatMensajeIntent.putExtra("visit_user_image", visit_user_image);
        startActivity(ChatMensajeIntent);

    }

    @Override
    public void onBackPressed() {
        irAtras();
    }


}
