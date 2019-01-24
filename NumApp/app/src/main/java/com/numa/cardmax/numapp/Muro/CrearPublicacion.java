package com.numa.cardmax.numapp.Muro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.numa.cardmax.numapp.Muro.Objetos.ObjetoMuro;
import com.numa.cardmax.numapp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.apptik.widget.multiselectspinner.MultiSelectSpinner;


public class CrearPublicacion extends YouTubeBaseActivity implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, YouTubePlayer.OnInitializedListener {


    static final int REQUEST_IMAGE_CAPTURE = 1;
    private MediaRecorder mRecorder;
    private MediaPlayer mediaPlayer;
    private FirebaseDatabase mDatabase;
    private DatabaseReference refDb;
    ObjetoMuro muro_publicaciones;
    private EditText edit_titulo;
    private EditText edit_contenido;
    private String imagen_publicacion;
    private Button cerrar;
    private Long nombre_imagen;
    private VideoView video;
    private String titulo_publicacion;
    private String contenido_publicacion;
    private Calendar c = Calendar.getInstance();
    private String fecha_publicacion;
    private int comentarios_publicacion = 0;
    private int likes_publicacion = 0;
    private int dislikes_publicacion = 0;
    private Button clear, btn_error;
    private FloatingActionButton addmore;
    private Button foto, addvideo, config;
    private ImageView miniatura, perfilfoto;
    String[] lisItems;
    boolean[] checkedItems;
    ArrayList<Integer> option = new ArrayList<>();
    private int activar_btn_megusta = 1;
    private int activar_btn_nomegusta = 1;
    private int activar_btn_comentario = 1;
    private int activar_btn_compartir = 1;
    private int compartido = 0;
    private Button aceptar, playpodcast, pausepodcast;
    private TextView txt_error, perfilnombre, numtxt, ciudadestxt, num_intereses, audio;
    private String id_usuario;
    private int TOP = 0;
    private Spinner    generospin,  estadocivilspin, edadinispin, edadfinspin;
    private MultiSelectSpinner ciudadespin, interesesspin, semestres, programasacd;

    private List<String> statements, ciudadeslist, intereseslist;
    private String item_genero = "", item_ciudades = "", item_programas = "", item_semestres = "", item_edadini = "", item_edadfin = "", item_estadocivil = "", item_intereses = "", num_semestre = "0";
    private Integer numero_edadini = 0, numeroedad_final = 0, style_pub = 0;
    private String mFileName = null;
    private View reproductor;
    private SeekBar control;
    private int realtimeLength;
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private int mediaFileLenght;
    private View panel, content, record;
    private ProgressBar wait;
    private String id, key_youtube = "AIzaSyB8MJf557XN9aDBtwFsD7rhhRMC77yBbeM";
    private YouTubePlayerView player_you;
    private Switch onadmin;


    final Handler handler = new Handler();

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muro_activity_crear_publicacion);


        player_you = (YouTubePlayerView) findViewById(R.id.youtube_view);
        player_you.initialize(key_youtube, this);

        ciudadespin = (MultiSelectSpinner) findViewById(R.id.spinner_ciudades);
        String fotourl = getIntent().getExtras().getString("foto");
        String perfurl = getIntent().getExtras().getString("nombreperfil");
        String rol = getIntent().getExtras().getString("rol");
        addmore = (FloatingActionButton) findViewById(R.id.btn_add);
        panel = (View) findViewById(R.id.viewpanel);
        content = (View) findViewById(R.id.viewcontent);
        record = (View) findViewById(R.id.viewrecord);
        wait = (ProgressBar) findViewById(R.id.carga);
        wait.setVisibility(View.GONE);
        control = (SeekBar) findViewById(R.id.seekBar_record);
        playpodcast = (Button) findViewById(R.id.btn_podcast__play);
        pausepodcast = (Button) findViewById(R.id.btn_podcast_pause);
        audio = (TextView) findViewById(R.id.tv);
        id_usuario = getIntent().getExtras().getString("id_user");
        final Toolbar actionbarx = (Toolbar) findViewById(R.id.toolbar2);
       /* setSupportActionBar(actionbarx);
        getSupportActionBar().setDisplayShowTitleEnabled(false);*/
        reproductor = (View) findViewById(R.id.pod_reproductor);
        perfilfoto = (ImageView) findViewById(R.id.img_perfilpub);
        aceptar = findViewById(R.id.btn_publicar);
        lisItems = getResources().getStringArray(R.array.permitir);
        checkedItems = new boolean[lisItems.length];
        addvideo = (Button) findViewById(R.id.btn_video_add);
        clear = (Button) findViewById(R.id.btn_clear);
        config = (Button) findViewById(R.id.btn_config);
        cerrar = (Button) findViewById(R.id.close);
        video = (VideoView) findViewById(R.id.videoView);
        edit_titulo = (EditText) findViewById(R.id.titulo_publicacion);
        edit_contenido = (EditText) findViewById(R.id.contenido_publicacion);
        final Button galeria = (Button) findViewById(R.id.btn_galeria);
        foto = (Button) findViewById(R.id.btn_foto);
        mDatabase = FirebaseDatabase.getInstance();
        refDb = mDatabase.getReference();
        nombre_imagen = System.currentTimeMillis();
        fecha_publicacion = DateFormat.getDateInstance().format(c.getTime());
        miniatura = findViewById(R.id.contenedor_imagen);
        perfilnombre = (TextView) findViewById(R.id.txt_nombre_perfil);
        programasacd = (MultiSelectSpinner) findViewById(R.id.spinner_programas);
        semestres = (MultiSelectSpinner) findViewById(R.id.spinner_semestres);
        numtxt = (TextView) findViewById(R.id.num_programas);
        ciudadestxt = (TextView) findViewById(R.id.ciudades_txt);

        generospin = (Spinner) findViewById(R.id.sepinner_genero);
        interesesspin = (MultiSelectSpinner) findViewById(R.id.spinner_intereses);
        intereseslist = new ArrayList<String>();
        num_intereses = (TextView) findViewById(R.id.num_intereses);
        estadocivilspin = (Spinner) findViewById(R.id.spinner_estadocivil);
        edadinispin = (Spinner) findViewById(R.id.spinner_edad_ini);
        edadfinspin = (Spinner) findViewById(R.id.spinner_edad_fin);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        RecordView recordView = (RecordView) findViewById(R.id.record_view);
        RecordButton recordButton = (RecordButton) findViewById(R.id.record_button);
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audio_grabado.3gp";
        id = refDb.push().getKey();
        onadmin = (Switch) findViewById(R.id.onadmin);
        audio.setHorizontallyScrolling(true);
        String text = "<font color=#E0E0E0><marquee>Podcast ♥· </font> <font color=#C0C0C0> ♪ ☺ ♪</font><font color=#000000> ...................</font>";
        audio.setText(Html.fromHtml(text));
        audio.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        audio.setSingleLine(true);
        audio.setMarqueeRepeatLimit(5);
        audio.setSelected(true);
        control.setMax(99);
        //IMPORTANT
        recordButton.setRecordView(recordView);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

        addmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CrearPublicacion.this, AddPublicacion.class);
                i.putExtra("id", id);
                startActivity(i);
            }
        });
        int screenWidth = this.getResources().getDisplayMetrics().widthPixels;


        playpodcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playpodcast.setVisibility(View.GONE);
                pausepodcast.setVisibility(View.VISIBLE);


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

                        }

                        updateseekbar();


                    }

                };

                mp3Play.execute(mFileName);


            }
        });


        pausepodcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playpodcast.setVisibility(View.VISIBLE);
                    pausepodcast.setVisibility(View.GONE);
                }
            }
        });


        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                //Start Recording..
                Log.d("RecordView", "onStart");

                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setOutputFile(mFileName);

                try {
                    mRecorder.prepare();
                    mRecorder.start();
                } catch (IllegalStateException e) {
                    Log.e("Erroraudio1: ", e.getMessage());
                } catch (IOException e) {
                    Log.e("Erroraudio: ", e.getMessage());
                }

                Toast.makeText(getApplicationContext(), "Grabando audio", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancel() {
                //On Swipe To Cancel
                Log.d("RecordView", "onCancel");

                try {
                    mRecorder.stop();
                    mRecorder.release();
                    //  mRecorder = null;

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Errorstop: ", e.getMessage());
                }


            }

            @Override
            public void onFinish(long recordTime) {
                //Stop Recording..
                clear.setVisibility(View.GONE);
                miniatura.setImageBitmap(null);
                miniatura.setImageURI(null);
                miniatura.setVisibility(View.GONE);
                video.setVisibility(View.GONE);
                video.stopPlayback();
                video.setVideoURI(null);

                String time = getHumanTimeText(recordTime);
                Log.d("RecordView", "onFinish");

                Log.d("RecordTime", time);

                try {
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;

                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    Log.e("Errorstop: ", e.getMessage());
                }
                Toast.makeText(getApplicationContext(), "Grabacion detenida", Toast.LENGTH_SHORT).show();
                reproductor.setVisibility(View.VISIBLE);
                aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        StorageMetadata metadata = new StorageMetadata.Builder()
                                .setContentType("audio/3gp")
                                .build();
                        Uri url = Uri.fromFile(new File(mFileName));
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        // Create a storage reference from our app
                        final StorageReference storageRef = storage.getReference();
                        // Create a reference to "mountains.jpg"
                        StorageReference mountainsRef = storageRef.child("AudioMuro");

                        UploadTask filepath = mountainsRef.child("audio_" + nombre_imagen + ".3gp").putFile(url, metadata);

                        filepath.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getApplicationContext(), "Publicado , Recarga el Muro.", Toast.LENGTH_LONG).show();

                                storageRef.child("AudioMuro/audio_" + nombre_imagen + ".3gp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        imagen_publicacion = uri.toString();
                                        titulo_publicacion = edit_titulo.getText().toString();
                                        contenido_publicacion = edit_contenido.getText().toString();

                                        muro_publicaciones = new ObjetoMuro(id, titulo_publicacion, contenido_publicacion,
                                                fecha_publicacion, imagen_publicacion, comentarios_publicacion, likes_publicacion, dislikes_publicacion,
                                                activar_btn_megusta, activar_btn_nomegusta, activar_btn_comentario, activar_btn_compartir, id_usuario, compartido, TOP, item_programas, num_semestre, item_ciudades, item_genero, item_intereses, item_estadocivil, numero_edadini, numeroedad_final, style_pub);
                                        refDb.child("muro_publicaciones").child(id).setValue(muro_publicaciones);


                                    }
                                });


                            }
                        });


                        finish();


                    }
                });


            }

            @Override
            public void onLessThanSecond() {
                //When the record time is less than One Second
                try {
                    //   mRecorder.prepare();
                    //   saber=false;
//                    mRecorder.stop();
                    mRecorder.release();
                    //  mRecorder = null;


                } catch (IllegalStateException e) {
                    //  e.printStackTrace();
                    Log.e("Errorstop: ", e.getMessage());
                } catch (NullPointerException e) {
                    Log.e("Errorstop2: ", e.getMessage());
                }
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                switch (checkedId) {
                    case R.id.radio_promocion:

                        style_pub = 1;
                        // Ninjas rule

                        break;
                    case R.id.radio_alerta:
                        style_pub = 2;
                        // Pirates are the best

                        break;
                    case R.id.radio_publicidad:
                        // Ninjas rule
                        style_pub = 3;

                        break;

                    case R.id.radio_comunicado:
                        // Ninjas rule
                        style_pub = 4;

                        break;


                }


            }
        });


        String[] edadini = {"", "10", "15", "20", "25", "30", "35"};

        ArrayAdapter<String> edadarrayini = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, edadini);
        edadinispin.setAdapter(edadarrayini);

        edadinispin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_edadini = (String) parent.getItemAtPosition(position);


                if (item_edadini.equals("")) {

                } else {

                    numero_edadini = Integer.parseInt((String) parent.getItemAtPosition(position));
                    if (numero_edadini > numeroedad_final) {
                        Toast.makeText(getApplicationContext(), "La edad final debe ser mayor", Toast.LENGTH_LONG).show();
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        String[] edadfin = {"", "20", "30", "40", "50", "60", "70"};

        ArrayAdapter<String> edadarrayfin = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, edadfin);
        edadfinspin.setAdapter(edadarrayfin);
        edadfinspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                item_edadfin = (String) parent.getItemAtPosition(position);

                if (item_edadfin.equals("")) {

                } else {
                    numeroedad_final = Integer.parseInt((String) parent.getItemAtPosition(position));
                    if (numero_edadini > numeroedad_final) {
                        Toast.makeText(getApplicationContext(), "La edad final debe ser mayor", Toast.LENGTH_LONG).show();
                    }


                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        statements = new ArrayList<String>();

        String[] estadocivil = {"", "Soltero", "Casado"};

        ArrayAdapter<String> estadoarray = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, estadocivil);
        estadocivilspin.setAdapter(estadoarray);

        estadocivilspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_estadocivil = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        DatabaseReference interesesref = FirebaseDatabase.getInstance().getReference("intereses");
        Query qintereses = interesesref.orderByChild("nombre");

        qintereses.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();
                int count = 0;
                String x;
                for (DataSnapshot murox : muroChildren) {
                    x = murox.child("nombre").getValue().toString();
                    intereseslist.add(x);
                    count = count + 1;
                }


                ArrayAdapter<String> adapter76 = new ArrayAdapter <String>(getBaseContext(), android.R.layout.simple_list_item_multiple_choice, intereseslist);

                interesesspin
                        .setListAdapter(adapter76)
                        .setSelectAll(true)
                        .setMinSelectedItems(1);






                num_intereses.setText("" + count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

   /*
        intereseslist.add(0, "");
        ArrayAdapter<String> adapterint = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, intereseslist);

        interesesspin.setAdapter(adapterint);


        interesesspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_intereses = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

*/
        DatabaseReference refrerencia = FirebaseDatabase.getInstance().getReference("ProgramasAcademicos");
        Query proacad = refrerencia.orderByChild("nombre");


        proacad.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();
                int count = 0;
                String x;
                for (DataSnapshot murox : muroChildren) {
                    x = murox.child("nombre").getValue().toString();
                    statements.add(x);
                    count = count + 1;

                    }

                ArrayAdapter<String> adapter86 = new ArrayAdapter <String>(getBaseContext(), android.R.layout.simple_list_item_multiple_choice, statements);

                programasacd
                        .setListAdapter(adapter86)
                        .setSelectAll(true)
                        .setMinSelectedItems(1);


                numtxt.setText("" + count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

/*
        statements.add(0, "");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, statements);

        programasacd.setAdapter(adapter);


        programasacd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_programas = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
        String[] letra = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};


        ArrayAdapter<String> adapter66 = new ArrayAdapter <String>(getBaseContext(), android.R.layout.simple_list_item_multiple_choice, letra);

        semestres
                .setListAdapter(adapter66)
                .setSelectAll(true)
                .setMinSelectedItems(1);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("hoooooooooola"+ semestres.getSpinnerText());
            }
        });



 /*       ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, letra);
        semestres.setAdapter(adapter2);


        semestres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                item_semestres = (String) parent.getItemAtPosition(position);

                if (item_semestres.equals("")) {

                } else {

                    num_semestre = Integer.parseInt(item_semestres);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
        String[] genero = {"", "Masculino", "Femenino"};

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, genero);
        generospin.setAdapter(adapter3);


        generospin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_genero = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






        final ArrayList<String> optionsy = new ArrayList<>();
        ciudadeslist = new ArrayList<String>();
        DatabaseReference refrerencia3 = FirebaseDatabase.getInstance().getReference("Ciudades");
        Query proacad2 = refrerencia3.orderByChild("nombre");

        proacad2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();
                int count = 0;
                String x;
                for (DataSnapshot murox : muroChildren) {
                    x = murox.child("nombre").getValue().toString();
                    ciudadeslist.add(x);

                    count = count + 1;

                }


                    ciudadestxt.setText("" + count);

               ArrayAdapter<String> adapter96 = new ArrayAdapter <String>(getBaseContext(), android.R.layout.simple_list_item_multiple_choice, ciudadeslist);

                ciudadespin
                        .setListAdapter(adapter96)
                        .setSelectAll(true)
                        .setMinSelectedItems(1);





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
/*

        ciudadeslist.add(0, "");

        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ciudadeslist);

        ciudadespin.setAdapter(adapter4);

        ciudadespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_ciudades = (String) parent.getItemAtPosition(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        */
        if (rol.equals("admin")) {
            final View card = (View) findViewById(R.id.card_admin);
            onadmin.setVisibility(View.VISIBLE);

            onadmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onadmin.isChecked()) {
                        card.setVisibility(View.VISIBLE);
                    } else {

                        card.setVisibility(View.GONE);
                    }
                }
            });
        }

        perfilnombre.setText(perfurl);
        Glide.with(getApplicationContext())
                .load(fotourl)
                .apply(RequestOptions.circleCropTransform())
                .into(perfilfoto);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Selecciona una Foto o un Video", Toast.LENGTH_SHORT).show();
            }
        });

        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBilder = new AlertDialog.Builder(CrearPublicacion.this);
                mBilder.setTitle("Ocultar");

                mBilder.setMultiChoiceItems(lisItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        try {
                            if (isChecked) {
                                if (!option.contains(which)) {
                                    option.add(which);
                                    System.out.println("agregue" + which);
                                }
                            } else {
                                for (int x : option) {
                                    if (x == which) {
                                        option.remove(which);
                                        System.out.println("remoovi" + which);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                });

                mBilder.setCancelable(false);
                mBilder.setPositiveButton("OCULTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for (int i : option) {
                            if (i == 0) {
                                activar_btn_megusta = 0;
                                System.out.println("Me Gusta OFF");
                            } else if (i == 1) {
                                activar_btn_nomegusta = 0;
                                System.out.println("NO Me Gusta OFF");
                            } else if (i == 2) {
                                activar_btn_comentario = 0;
                                System.out.println("Comentario OFF");
                            } else if (i == 3) {
                                activar_btn_compartir = 0;
                                System.out.println("compartir OFF");
                            }
                        }
                    }
                });

                mBilder.setNeutralButton("Limpiar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            option.clear();
                        }
                    }
                });
                AlertDialog mDialog = mBilder.create();
                mDialog.show();

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear.setVisibility(View.GONE);
                miniatura.setImageBitmap(null);
                miniatura.setImageURI(null);
                video.setVisibility(View.GONE);
                video.stopPlayback();
                video.setVideoURI(null);
            }
        });

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        control.setOnTouchListener(new View.OnTouchListener() {
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

        addvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reproductor.setVisibility(View.GONE);
                aceptar.setTextColor(Color.BLACK);
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("video/*");
                startActivityForResult(galleryIntent.createChooser(galleryIntent, "Seleccione un Video"), 10);

            }
        });

        galeria.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reproductor.setVisibility(View.GONE);
                aceptar.setTextColor(Color.BLACK);
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/");
                startActivityForResult(galleryIntent.createChooser(galleryIntent, "Seleccione la foto"), 10);

            }
        });

        foto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reproductor.setVisibility(View.GONE);
                aceptar.setTextColor(Color.BLACK);
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            final Bitmap imageBitmap = (Bitmap) extras.get("data");
            miniatura.setImageBitmap(imageBitmap);
            miniatura.setVisibility(View.VISIBLE);
            clear.setVisibility(View.VISIBLE);
            aceptar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                    byte[] byteArray = stream.toByteArray();
                    long lengthbmp = byteArray.length;
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    // Create a storage reference from our app
                    final StorageReference storageRef = storage.getReference();
                    // Create a reference to "mountains.jpg"
                    StorageReference mountainsRef = storageRef.child("images/" + nombre_imagen);
                    // Create a reference to 'images/mountains.jpg'
                    StorageReference mountainImagesRef = storageRef.child("images/" + nombre_imagen);
                    Toast.makeText(CrearPublicacion.this, mountainImagesRef.getDownloadUrl().toString(), Toast.LENGTH_LONG);
                    // While the file names are the same, the references point to different files
                    mountainsRef.getName().equals(mountainImagesRef.getName());    // true
                    mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false
                    UploadTask uploadTask;
                    uploadTask = mountainsRef.putBytes(byteArray);
                    panel.setVisibility(View.GONE);
                    content.setVisibility(View.GONE);
                    record.setVisibility(View.GONE);
                    wait.setVisibility(View.VISIBLE);


                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                            storageRef.child("images/" + nombre_imagen).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {


                                    // Got the download URL for 'users/me/profile.png'
                                    imagen_publicacion = uri.toString();
                                    titulo_publicacion = edit_titulo.getText().toString();
                                    contenido_publicacion = edit_contenido.getText().toString();

                                    muro_publicaciones = new ObjetoMuro(id, titulo_publicacion, contenido_publicacion,
                                            fecha_publicacion, imagen_publicacion, comentarios_publicacion, likes_publicacion, dislikes_publicacion,
                                            activar_btn_megusta, activar_btn_nomegusta, activar_btn_comentario, activar_btn_compartir, id_usuario, compartido, TOP, item_programas, num_semestre, item_ciudades, item_genero, item_intereses, item_estadocivil, numero_edadini, numeroedad_final, style_pub);
                                    refDb.child("muro_publicaciones").child(id).setValue(muro_publicaciones);


                                    Toast.makeText(CrearPublicacion.this, "Publicado Actualiza el Muro", Toast.LENGTH_LONG).show();

                                    finish();
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    System.out.println("ERROR" + exception.getMessage());
                                }
                            });

                        }
                    });

                  /*  Intent aceptarx;
                    aceptarx = new Intent(CrearPublicacion.this, MuroMainActivity.class);
                    startActivity(aceptarx);*/
                }
            });
        }

        final Uri path = data.getData();
        if (path != null) {
            clear.setVisibility(View.VISIBLE);
            String buscar = path.toString();
            final int buscando = buscar.indexOf("video");
            if (buscando != -1) {
                miniatura.setVisibility(View.INVISIBLE);
                video.setVisibility(View.VISIBLE);
                video.setVideoURI(path);
                video.setMediaController(new MediaController(this));
                video.start();

                aceptar.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {


                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        // Create a storage reference from our app
                        final StorageReference storageRef = storage.getReference();
                        // Create a reference to "mountains.jpg"

                        UploadTask uploadTask;
                        final StorageReference riversRef;
                        riversRef = storageRef.child("video/" + nombre_imagen);
                        uploadTask = riversRef.putFile(path);
                        panel.setVisibility(View.GONE);
                        content.setVisibility(View.GONE);
                        record.setVisibility(View.GONE);
                        wait.setVisibility(View.VISIBLE);

                        // Register observers to listen for when the download is done or if it fails
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                // ...

                                storageRef.child("video/" + nombre_imagen).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imagen_publicacion = uri.toString();
                                        titulo_publicacion = edit_titulo.getText().toString();
                                        contenido_publicacion = edit_contenido.getText().toString();
                                        muro_publicaciones = new ObjetoMuro(id, titulo_publicacion, contenido_publicacion,
                                                fecha_publicacion, imagen_publicacion,
                                                comentarios_publicacion, likes_publicacion, dislikes_publicacion,
                                                activar_btn_megusta, activar_btn_nomegusta, activar_btn_comentario, activar_btn_compartir, id_usuario, compartido, TOP, item_programas, num_semestre, item_ciudades, item_genero, item_intereses, item_estadocivil, numero_edadini, numeroedad_final, style_pub);
                                        refDb.child("muro_publicaciones").child(id).setValue(muro_publicaciones);

                                        Toast.makeText(CrearPublicacion.this, "Publicado Actualiza el Muro", Toast.LENGTH_LONG).show();

                                        finish();


                                    }
                                });

                            }
                        });

                    }
                });


            } else {
                video.setVisibility(View.GONE);
                miniatura.setVisibility(View.VISIBLE);
                miniatura.setImageURI(path);


                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(
                            path);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Bitmap bmp = BitmapFactory.decodeStream(imageStream);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 40, stream);
                final byte[] byteArray = stream.toByteArray();
                try {
                    stream.close();
                    stream = null;
                } catch (IOException e) {

                    e.printStackTrace();
                }


                aceptar.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {


                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        // Create a storage reference from our app
                        final StorageReference storageRef = storage.getReference();
                        // Create a reference to "mountains.jpg"

                        UploadTask uploadTask;
                        final StorageReference riversRef;
                        riversRef = storageRef.child("images/" + nombre_imagen);


                        uploadTask = riversRef.putBytes(byteArray);
                        panel.setVisibility(View.GONE);
                        content.setVisibility(View.GONE);
                        record.setVisibility(View.GONE);
                        wait.setVisibility(View.VISIBLE);

                        // Register observers to listen for when the download is done or if it fails
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                // ...

                                storageRef.child("images/" + nombre_imagen).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imagen_publicacion = uri.toString();
                                        titulo_publicacion = edit_titulo.getText().toString();
                                        contenido_publicacion = edit_contenido.getText().toString();
                                        muro_publicaciones = new ObjetoMuro(id, titulo_publicacion, contenido_publicacion,
                                                fecha_publicacion, imagen_publicacion,
                                                comentarios_publicacion, likes_publicacion, dislikes_publicacion,
                                                activar_btn_megusta, activar_btn_nomegusta, activar_btn_comentario, activar_btn_compartir, id_usuario, compartido, TOP, item_programas, num_semestre, item_ciudades, item_genero, item_intereses, item_estadocivil, numero_edadini, numeroedad_final, style_pub);
                                        refDb.child("muro_publicaciones").child(id).setValue(muro_publicaciones);

                                        Toast.makeText(CrearPublicacion.this, "Publicado Actualiza el Muro", Toast.LENGTH_LONG).show();

                                        finish();

                                    }
                                });

                            }
                        });
                        Toast.makeText(CrearPublicacion.this, "Publicado", Toast.LENGTH_SHORT).show();
                        Intent aceptarx;
                        aceptarx = new Intent(CrearPublicacion.this, MuroMainActivity.class);
                        startActivity(aceptarx);
                    }
                });


            }


        }
    }


    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.checkBox_top:
                if (checked) {
                    // Put some meat on the sandwich
                    TOP = 1;
                } else {
                    // Remove the meat
                    TOP = 0;
                    break;
                }
        }
    }


    private String getHumanTimeText(long milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        control.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    private void updateseekbar() {
        control.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLenght) * 100));
        if (mediaPlayer.isPlaying()) {
            Runnable updater = new Runnable() {
                @SuppressLint("DefaultLocale")
                @Override
                public void run() {
                    updateseekbar();
                    realtimeLength -= 1000; // declare 1 secon
                }

            };
            handler.postDelayed(updater, 1000); // 1 second
        }

        if (control.getProgress() > 70) {

            playpodcast.setVisibility(View.VISIBLE);
            pausepodcast.setVisibility(View.GONE);

        }

    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {


            edit_contenido.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (s.toString().indexOf("https://www.youtube.com/watch?v=") != -1) {
                        String code;
                        code = s.toString().replace("https://www.youtube.com/watch?v=", "");
                        youTubePlayer.cueVideo(code);
                        player_you.setVisibility(View.VISIBLE);


                        String frameVideo = "<iframe width=\'100%\' height=\'100%\'\n" +
                                "src=\'https://www.youtube.com/embed/" + code + "\'>\n" +
                                "</iframe>";

                        WebView myWebView = (WebView) findViewById(R.id.mWebView);

                        WebSettings webSettings = myWebView.getSettings();
                        webSettings.setJavaScriptEnabled(true);

                        myWebView.getSettings().setLoadWithOverviewMode(true);
                        myWebView.getSettings().setUseWideViewPort(true);
                        myWebView.loadData(frameVideo, "text/html", "utf-8");

                        myWebView.setWebChromeClient(new WebChromeClient() {
                        });


                        aceptar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                imagen_publicacion = "youtube";
                                titulo_publicacion = edit_titulo.getText().toString();
                                contenido_publicacion = edit_contenido.getText().toString();

                                muro_publicaciones = new ObjetoMuro(id, titulo_publicacion, contenido_publicacion,
                                        fecha_publicacion, imagen_publicacion, comentarios_publicacion, likes_publicacion, dislikes_publicacion,
                                        activar_btn_megusta, activar_btn_nomegusta, activar_btn_comentario, activar_btn_compartir, id_usuario, compartido, TOP, item_programas, num_semestre, item_ciudades, item_genero, item_intereses, item_estadocivil, numero_edadini, numeroedad_final, style_pub);
                                refDb.child("muro_publicaciones").child(id).setValue(muro_publicaciones);
                                Toast.makeText(CrearPublicacion.this, "Publicado Actualiza el Muro", Toast.LENGTH_LONG).show();
                                finish();

                            }
                        });


                    } else if (s.toString().indexOf("https://www.youtube.com/") != -1) {
                        String code;
                        code = s.toString().replace("https://www.youtube.com/", "");
                        youTubePlayer.cueVideo(code);
                        player_you.setVisibility(View.VISIBLE);

                        aceptar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                imagen_publicacion = "youtube";
                                titulo_publicacion = edit_titulo.getText().toString();
                                contenido_publicacion = edit_contenido.getText().toString();

                                muro_publicaciones = new ObjetoMuro(id, titulo_publicacion, contenido_publicacion,
                                        fecha_publicacion, imagen_publicacion, comentarios_publicacion, likes_publicacion, dislikes_publicacion,
                                        activar_btn_megusta, activar_btn_nomegusta, activar_btn_comentario, activar_btn_compartir, id_usuario, compartido, TOP, item_programas, num_semestre, item_ciudades, item_genero, item_intereses, item_estadocivil, numero_edadini, numeroedad_final, style_pub);
                                refDb.child("muro_publicaciones").child(id).setValue(muro_publicaciones);
                                Toast.makeText(CrearPublicacion.this, "Publicado Actualiza el Muro", Toast.LENGTH_LONG).show();
                                finish();

                            }
                        });


                    } else if (s.toString().indexOf("https://youtu.be/") != -1) {
                        String code;
                        code = s.toString().replace("https://youtu.be/", "");
                        youTubePlayer.cueVideo(code);
                        player_you.setVisibility(View.VISIBLE);

                        aceptar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                imagen_publicacion = "youtube";
                                titulo_publicacion = edit_titulo.getText().toString();
                                contenido_publicacion = edit_contenido.getText().toString();


                                muro_publicaciones = new ObjetoMuro(id, titulo_publicacion, contenido_publicacion,
                                        fecha_publicacion, imagen_publicacion, comentarios_publicacion, likes_publicacion, dislikes_publicacion,
                                        activar_btn_megusta, activar_btn_nomegusta, activar_btn_comentario, activar_btn_compartir, id_usuario, compartido, TOP, item_programas, num_semestre, item_ciudades, item_genero, item_intereses, item_estadocivil, numero_edadini, numeroedad_final, style_pub);
                                refDb.child("muro_publicaciones").child(id).setValue(muro_publicaciones);
                                Toast.makeText(CrearPublicacion.this, "Publicado Actualiza el Muro", Toast.LENGTH_LONG).show();
                                finish();

                            }
                        });

                    } else {

                        player_you.setVisibility(View.GONE);


                        aceptar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                item_ciudades = ciudadespin.getSpinnerText();
                                item_programas = programasacd.getSpinnerText();
                                item_intereses = interesesspin.getSpinnerText();
                                num_semestre = semestres.getSpinnerText();

                                imagen_publicacion = "";
                                titulo_publicacion = edit_titulo.getText().toString();
                                contenido_publicacion = edit_contenido.getText().toString();


                                muro_publicaciones = new ObjetoMuro(id, titulo_publicacion, contenido_publicacion,
                                        fecha_publicacion, imagen_publicacion, comentarios_publicacion, likes_publicacion, dislikes_publicacion,
                                        activar_btn_megusta, activar_btn_nomegusta, activar_btn_comentario, activar_btn_compartir, id_usuario, compartido, TOP, item_programas, num_semestre, item_ciudades, item_genero, item_intereses, item_estadocivil, numero_edadini, numeroedad_final, style_pub);
                                refDb.child("muro_publicaciones").child(id).setValue(muro_publicaciones);
                                Toast.makeText(CrearPublicacion.this, "Publicado Actualiza el Muro", Toast.LENGTH_LONG).show();
                                finish();

                            }
                        });


                    }


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
