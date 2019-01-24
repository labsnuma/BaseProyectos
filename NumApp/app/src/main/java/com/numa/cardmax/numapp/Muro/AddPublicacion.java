package com.numa.cardmax.numapp.Muro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.numa.cardmax.numapp.Muro.Adaptadores.AdaptadorShowMore;
import com.numa.cardmax.numapp.Muro.Objetos.ObjetoAddMore;
import com.numa.cardmax.numapp.Muro.Objetos.ObjetoMuro;
import com.numa.cardmax.numapp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AddPublicacion extends Activity implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {
    private RecordView recordView;
    private RecordButton recordButton;
    private MediaRecorder mRecorder;
    private MediaPlayer mediaPlayer;
    private String mFileName = null, id_publicacion, imagen_publicacion, contenido_txt;
    private int mediaFileLenght;
    private int realtimeLength;
    private Button clear, aceptar, playpodcast, pausepodcast, addvideo, galeria, foto, cerrar;
    private ImageView miniatura;
    private VideoView video;
    private View reproductor;
    private TextView audio;
    private SeekBar control;
    final Handler handler = new Handler();
    private Long nombre_imagen;
    private FirebaseDatabase mDatabase;
    private DatabaseReference refDb;
    ObjetoAddMore add_more_publicaciones;
    private EditText contenidotxt;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private RecyclerView lista;
    private ArrayList<ObjetoAddMore> listobject;
    private AdaptadorShowMore calladapter;
    private DatabaseReference xDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muro_activity_add_publicacion);
        mDatabase = FirebaseDatabase.getInstance();
        refDb = mDatabase.getReference();
        cerrar=(Button)findViewById(R.id.close);
        id_publicacion = getIntent().getExtras().getString("id");
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audio_grabado.3gp";
        recordView = (RecordView) findViewById(R.id.record_view);
        recordButton = (RecordButton) findViewById(R.id.record_button);
        addvideo = (Button) findViewById(R.id.btn_video_add);
        galeria = (Button) findViewById(R.id.btn_galeria);
        foto = (Button) findViewById(R.id.btn_foto);
        recordButton.setRecordView(recordView);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
        playpodcast = (Button) findViewById(R.id.btn_podcast__play);
        pausepodcast = (Button) findViewById(R.id.btn_podcast_pause);
        clear = (Button) findViewById(R.id.btn_clear);
        aceptar = findViewById(R.id.btn_publicar);
        miniatura = findViewById(R.id.contenedor_imagen);
        video = (VideoView) findViewById(R.id.videoView);
        reproductor = (View) findViewById(R.id.pod_reproductor);
        audio = (TextView) findViewById(R.id.tv);
        control = (SeekBar) findViewById(R.id.seekBar_record);
        nombre_imagen = System.currentTimeMillis();
        contenidotxt = (EditText) findViewById(R.id.contenido_publicacion);
        audio.setHorizontallyScrolling(true);
        String text = "<font color=#E0E0E0><marquee>Podcast ♥· </font> <font color=#C0C0C0> ♪ ☺ ♪</font><font color=#000000> ...................</font>";
        audio.setText(Html.fromHtml(text));
        audio.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        audio.setSingleLine(true);
        audio.setMarqueeRepeatLimit(5);
        audio.setSelected(true);
        control.setMax(99);



        //////////////
        lista = (RecyclerView)findViewById(R.id.recycler_more);
        listobject =new ArrayList<ObjetoAddMore>();
        calladapter =  new AdaptadorShowMore(listobject);
        lista.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        lista.setAdapter(calladapter);
        lista.setLayoutManager(layout);
        ///////////
        xDatabase = FirebaseDatabase.getInstance().getReference();
        Query detalles = xDatabase.child("publicaciones_add_more").orderByChild("id_publicacion").equalTo(id_publicacion);

        detalles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();
                listobject.removeAll(listobject);
                for (DataSnapshot murox : muroChildren) {
                    try {
                        ObjetoAddMore p = murox.getValue(ObjetoAddMore.class);
                        listobject.add(0, p);

                    }catch (Exception e){
                        System.out.println(e);
                    }




                }
                calladapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                recordView.setVisibility(View.VISIBLE);
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
                recordView.setVisibility(View.GONE);

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
                recordView.setVisibility(View.GONE);
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
                        final String id = refDb.push().getKey();
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
                                        contenido_txt = contenidotxt.getText().toString();

                                        add_more_publicaciones = new ObjetoAddMore(id_publicacion, id, contenido_txt, imagen_publicacion);

                                        refDb.child("publicaciones_add_more").child(id).setValue(add_more_publicaciones);

                                        Toast.makeText(AddPublicacion.this, "Agregado", Toast.LENGTH_LONG).show();



                                    }
                                });


                            }
                        });


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
                    recordView.setVisibility(View.GONE);

                } catch (IllegalStateException e) {
                    //  e.printStackTrace();
                    Log.e("Errorstop: ", e.getMessage());
                } catch (NullPointerException e) {
                    Log.e("Errorstop2: ", e.getMessage());
                }
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

    private String getHumanTimeText(long milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
                    final String id = refDb.push().getKey();
                    // While the file names are the same, the references point to different files
                    mountainsRef.getName().equals(mountainImagesRef.getName());    // true
                    mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false
                    UploadTask uploadTask;
                    uploadTask = mountainsRef.putBytes(byteArray);
                /*    panel.setVisibility(View.GONE);
                    content.setVisibility(View.GONE);
                    record.setVisibility(View.GONE);
                    wait.setVisibility(View.VISIBLE);*/


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
                                    contenido_txt = contenidotxt.getText().toString();

                                    add_more_publicaciones = new ObjetoAddMore(id_publicacion, id, contenido_txt, imagen_publicacion);

                                    refDb.child("publicaciones_add_more").child(id).setValue(add_more_publicaciones);



                                    Toast.makeText(AddPublicacion.this, "Agregado", Toast.LENGTH_LONG).show();

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
                     /*   panel.setVisibility(View.GONE);
                        content.setVisibility(View.GONE);
                        record.setVisibility(View.GONE);
                        wait.setVisibility(View.VISIBLE);*/
                        final String id = refDb.push().getKey();
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
                                        contenido_txt = contenidotxt.getText().toString();

                                        add_more_publicaciones = new ObjetoAddMore(id_publicacion, id, contenido_txt, imagen_publicacion);

                                        refDb.child("publicaciones_add_more").child(id).setValue(add_more_publicaciones);


                                        Toast.makeText(AddPublicacion.this, "Agregado", Toast.LENGTH_LONG).show();




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


                        final String id = refDb.push().getKey();
                        uploadTask = riversRef.putBytes(byteArray);
                     /*   panel.setVisibility(View.GONE);
                        content.setVisibility(View.GONE);
                        record.setVisibility(View.GONE);
                        wait.setVisibility(View.VISIBLE);*/

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
                                        contenido_txt = contenidotxt.getText().toString();

                                        add_more_publicaciones = new ObjetoAddMore(id_publicacion, id, contenido_txt, imagen_publicacion);

                                        refDb.child("publicaciones_add_more").child(id).setValue(add_more_publicaciones);


                                        Toast.makeText(AddPublicacion.this, "Agregado", Toast.LENGTH_LONG).show();




                                    }
                                });

                            }
                        });

                    }
                });











            }


        }





    }
}
