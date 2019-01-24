package com.cardmax.base.Chat.Recursos;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cardmax.base.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.cardmax.base.Muro.Objetos.ObjetoNotificacionChat;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import uk.co.senab.photoview.PhotoViewAttacher;

public class SelectorImagenes extends AppCompatActivity {

    private static final int RC_GALLERY = 21;
    private static final int RC_CAMARA = 22;

    private static final String MY_PHOTO = "mi_foto";
    public DatabaseReference RootDataRef;
    public StorageReference RootStorageRef;
    private String mcurrentPhotoPath;
    public String mFotoDatabaseString, mNombreDataString;
    public String mFotoStorageString, mNombreStorageString;
    private FirebaseDatabase mDatabase;
    private DatabaseReference refDb;
    private Uri fotoSeleccionadaURI;
    private ImageButton eliminar;
    private ImageView imagen;
    private ConstraintLayout container;
    private ProgressDialog progreso;
    private String answer_mensagge;
    private String answer_multimedia;
    private String answer_user;


    public Uri imagenComprimida;
    IraActividades actividadesir = new IraActividades();
    private String path_Gallery;
    private BottomNavigationView navi, navi_2;
    public ProgressDialog dialog;
    //
    private DatabaseReference ChatMensajesRef, RootRef, ChatGruposRef;
    private String nsender, nreciver;
    private String nombreGrupo, fotoId;
    FechaHora calendario = new FechaHora();
    int opcione;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_galeria:
                    funcionGaelria();
                    return true;
                case R.id.navigation_tomar_foto:
                    funcionCamara();
                    return true;
                case R.id.navigation_ajustar:
                    funcionCrop();
                    return true;
                case R.id.navigation_subir:
                    subirImagen();
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocolo_prueba_imagenes);
        iniciarvalores();
        ininicarFirebase();

        answer_mensagge = getIntent().getStringExtra("answer_mensagge");
        answer_multimedia= getIntent().getStringExtra("answer_multimedia");
        answer_user= getIntent().getStringExtra("answer_user");



    }

    private void ininicarFirebase() {
        RootStorageRef = FirebaseStorage.getInstance().getReference();

        mFotoStorageString = getIntent().getExtras().get("stroageRef").toString();
        mNombreStorageString = getIntent().getExtras().get("nombreStorageRef").toString();

        opcione = (int) getIntent().getExtras().get("opcion");
        //Opcion1
        mFotoDatabaseString = getIntent().getExtras().get("databaseRef").toString();
        mNombreDataString = getIntent().getExtras().get("nombreDataRef").toString();

        //Opcion2
        RootRef = FirebaseDatabase.getInstance().getReference();
        ChatMensajesRef = FirebaseDatabase.getInstance().getReference().child("Chat Mensajes");
        nsender = getIntent().getExtras().get("nDatasender").toString();
        nreciver = getIntent().getExtras().get("nDatareciver").toString();

        //Opcion3
        ChatGruposRef = FirebaseDatabase.getInstance().getReference().child("Grupos");
        //  nsender= EL ID
        //  mNombreDataString=NOMBRE DEL ID
        nombreGrupo = getIntent().getExtras().get("grupoRef").toString();
        fotoId = getIntent().getExtras().get("nFotoRef").toString();
    }

    private void iniciarvalores() {
        container = (ConstraintLayout) findViewById(R.id.activity_container);
        navi = (BottomNavigationView) findViewById(R.id.navigation_camara);
        navi_2 = (BottomNavigationView) findViewById(R.id.navigation_camara_ajustar);
        navi.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navi_2.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        eliminar = (ImageButton) findViewById(R.id.btn_delete);
        imagen = (ImageView) findViewById(R.id.imagen_seleccioanda);
        PhotoViewAttacher photoView = new PhotoViewAttacher(imagen);
        photoView.update();

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAtras();

            }
        });


    }

    private void funcionCrop() {
        CropImage.activity(imagenComprimida)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(SelectorImagenes.this);
    }

    private void funcionGaelria() {
        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galeria, RC_GALLERY);
    }

    private void funcionCamara() {
        Intent camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (camara.resolveActivity(getPackageManager()) != null) {
            File fotofile;
            fotofile = crearImagenFile();
            if (fotofile != null) {
                Uri fotouri = FileProvider.getUriForFile(this,
                        "com.numa.cardmax.numapp", fotofile);
                camara.putExtra(MediaStore.EXTRA_OUTPUT, fotouri);
                startActivityForResult(camara, RC_CAMARA);
            }
        }
    }

    private File crearImagenFile() {
        File storage = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = null;
        try {
            imagen = File.createTempFile(MY_PHOTO, ".jpeg", storage);
            mcurrentPhotoPath = imagen.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagen;
    }

    private Uri addPickCamara() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(mcurrentPhotoPath);
        Uri uri = Uri.fromFile(file);
        mediaScanIntent.setData(uri);
        this.sendBroadcast(mediaScanIntent);
        mcurrentPhotoPath = null;
        return uri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RC_GALLERY:
                    if (data != null) {
                        fotoSeleccionadaURI = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                                    fotoSeleccionadaURI);
                            new CompressionImagenAsync().execute(bitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    break;
                case RC_CAMARA:
                    try {
                        fotoSeleccionadaURI = addPickCamara();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                                fotoSeleccionadaURI);
                        new CompressionImagenAsync().execute(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                imagenComprimida = result.getUri();
                Glide.with(SelectorImagenes.this).load(imagenComprimida).into(imagen);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                assert result != null;
                Exception error = result.getError();
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class CompressionImagenAsync extends AsyncTask<Bitmap, Void, Void> {
        //   public ProgressDialog dialog = new ProgressDialog(SelectorImagenes.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*    //  ProgressDialog dialog = new ProgressDialog(SelectorImagenes.this);
                dialog = new ProgressDialog(SelectorImagenes.this);
                dialog.setTitle("Estableciendo Imagen");
                dialog.setMessage("Por favor espera");
                dialog.show();
                dialog.setCancelable(false);*/

            if (!isFinishing() && this != null) {
                //  ProgressDialog dialog = new ProgressDialog(SelectorImagenes.this);
                dialog = new ProgressDialog(SelectorImagenes.this);
                dialog.setTitle("Estableciendo Imagen");
                dialog.setMessage("Por favor espera");
                dialog.show();
                dialog.setCancelable(false);
            }

        }


        @Override
        protected Void doInBackground(Bitmap... params) {
            Bitmap bitmap = params[0];
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            double width = bitmap.getWidth();
            double height = bitmap.getHeight();
            double ratio = 800 / width;
            int newheight = (int) (ratio * height);

            try {
                bitmap = Bitmap.createScaledBitmap(bitmap, 800, newheight, true);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                stream.flush();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Error#97", e.getMessage());
            }

            try {
                File file_path = Environment.getExternalStorageDirectory().getAbsoluteFile();
                File dir = new File(file_path + "/NumAPP/");
                if (!dir.exists())
                    dir.mkdirs();

                final String tiempo = new SimpleDateFormat("dd-MM-yyy_HHmmss", Locale.ROOT).format(new Date());
                File salida = new File(dir, "imagen_" + tiempo + ".jpeg");

                FileOutputStream fOut = new FileOutputStream(salida);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fOut);
                fOut.flush();
                fOut.close();
                imagenComprimida = Uri.fromFile(salida);

            } catch (Exception e) {
                Log.e("Error#98", e.getMessage());

            }
            return null;

           /* Bitmap bitmap = params[0];
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            double width = bitmap.getWidth();
            double height = bitmap.getHeight();
            double ratio = 2500 / width;
            int newheight = (int) (ratio * height);

            bitmap = Bitmap.createScaledBitmap(bitmap, 2500, newheight, true);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            try {
                File  file_path = Environment.getExternalStorageDirectory().getAbsoluteFile();
                File dir =new File(file_path+  "/NumAPP/");
                if (!dir.exists())
                    dir.mkdirs();
                File salida = new File(dir, "mi_foto"+".png");
                FileOutputStream fOut = new FileOutputStream(salida);

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
             //   fOut.flush();
            //    fOut.close();
                imagenComprimida=Uri.fromFile(salida);

            *//*    Toast.makeText(getApplicationContext(),
                        "Image has been saved in KidsPainting folder",
                        Toast.LENGTH_LONG).show();*//*

                //     path_Gallery = MediaStore.Images.Media.insertImage(RSelectorImagenes.this.getContentResolver(), bitmap, null, null);
                //    path_Gallery = MediaStore.Images.Media.insertImage(SelectorImagenes.this.getContentResolver(), bitmap, "Imagen", "Desde camara");
                //  imagenComprimida = Uri.parse(String.valueOf(fOut));
            }catch (Exception e){
                Log.e("Error#35",e.getMessage());

            }
        *//*    try {
                path_Gallery = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Imagen", "Desde camara");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Error#198",e.getMessage());
            }
            //    path_Gallery = MediaStore.Images.Media.insertImage(SelectorImagenes.this.getContentResolver(), bitmap, "Imagen", "Desde camara");
            imagenComprimida = Uri.parse(path_Gallery);*//*

            return null;*/

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            eliminar.setVisibility(View.VISIBLE);
            navi.setVisibility(View.INVISIBLE);
            navi.setEnabled(false);
            navi_2.setVisibility(View.VISIBLE);

            Glide.with(SelectorImagenes.this).load(imagenComprimida).into(imagen);

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }


    private void subirImagen() {

        progreso = new ProgressDialog(this);
        progreso.setTitle("Subiendo Imagen");
        progreso.show();

        UploadTask filepath = RootStorageRef.child(mFotoStorageString).child(mNombreStorageString).putFile(imagenComprimida);
        filepath.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progreso.setMessage("Cargando " + ((int) progress) + "%...");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("La carga ha sido pausada");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Snackbar.make(container, "Error al subir Imagen,intente mas tarde", Snackbar.LENGTH_LONG).show();
                progreso.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri descargarUri = taskSnapshot.getDownloadUrl();
                if (opcione == 1) {
                    opcion1(descargarUri);
                } else if (opcione == 2) {
                    opcion2(descargarUri);
                } else if (opcione == 3) {
                    opcion3(descargarUri);
                }


                eliminar.setVisibility(View.VISIBLE);


            }
        });

    }

    private void opcion1(Uri descargarUri) {

        RootDataRef = FirebaseDatabase.getInstance().getReference().child(mFotoDatabaseString).child(mNombreDataString);
        RootDataRef.setValue(descargarUri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progreso.dismiss();
                Toast.makeText(SelectorImagenes.this, "Imagen subida correctamente", Toast.LENGTH_SHORT).show();
                irAtras();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SelectorImagenes.this, "Error al subir Imagen,intente mas tarde", Toast.LENGTH_SHORT).show();
                irAtras();
            }
        });

    }

    private void opcion2(Uri descargarUri) {

        calendario.getHora();
        String fecha = calendario.getGuardarFecha2();
        String hora = calendario.getGuardarhora1();

        String mensajeSenderRef1 = "Chat Mensajes/" + nsender + "/" + nreciver;
        String mensajeReciveRef2 = "Chat Mensajes/" + nreciver + "/" + nsender;

        DatabaseReference userMensajeKeyRef = ChatMensajesRef
                .child(nsender).child(nreciver).push();

        String mensajePushID = userMensajeKeyRef.getKey();

        Map<String, String> mensajeDeTexto = new HashMap<>();
        mensajeDeTexto.put("mensaje", "");
        mensajeDeTexto.put("tipo", "imagen");
        mensajeDeTexto.put("fecha", fecha);
        mensajeDeTexto.put("hora", hora);
        mensajeDeTexto.put("from", nsender);
        mensajeDeTexto.put("imagen", descargarUri.toString());
        mensajeDeTexto.put("visto", "0");
        mensajeDeTexto.put("key", mensajePushID);
        mensajeDeTexto.put("answer_mensagge", answer_mensagge);
        mensajeDeTexto.put("answer_multimedia", answer_multimedia);
        mensajeDeTexto.put("answer_user", answer_user);






        Map<String, Object> mensajeDeTextoDetalles = new HashMap<>();
        mensajeDeTextoDetalles.put(mensajeSenderRef1 + "/" + mensajePushID, mensajeDeTexto);
        mensajeDeTextoDetalles.put(mensajeReciveRef2 + "/" + mensajePushID, mensajeDeTexto);

        RootRef.updateChildren(mensajeDeTextoDetalles).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SelectorImagenes.this, "Imagen Enviada", Toast.LENGTH_SHORT).show();
                    irAtras();
                } else {
                    String mensajeError = Objects.requireNonNull(task.getException()).toString();
                    Toast.makeText(SelectorImagenes.this, "Error: " + mensajeError, Toast.LENGTH_SHORT).show();
                    irAtras();
                }

            }
        });

        mDatabase = FirebaseDatabase.getInstance();
        refDb = mDatabase.getReference();
        String id = refDb.push().getKey();
        ObjetoNotificacionChat registro = new ObjetoNotificacionChat(nsender, "Envio una Imagen");

        refDb.child("notification_user_chat").child(nreciver).child(id).setValue(registro);




    }

    private void opcion3(Uri descargarUri) {

        calendario.getHora();
        String fecha = calendario.getGuardarFecha2();
        String hora = calendario.getGuardarhora1();

        String mensajeSenderRef1 = "Grupos/" + nombreGrupo;
        DatabaseReference userMensajeKeyRef = ChatGruposRef
                .child(nombreGrupo).push();

        String mensajePushID = userMensajeKeyRef.getKey();
        Map<String, String> mensajeDeTexto = new HashMap<>();
        mensajeDeTexto.put("mensaje", "");
        mensajeDeTexto.put("tipo", "imagen");
        mensajeDeTexto.put("fecha", fecha);
        mensajeDeTexto.put("hora", hora);
        mensajeDeTexto.put("nombre", mNombreDataString);
        mensajeDeTexto.put("from", nsender);
        mensajeDeTexto.put("foto", fotoId);
        mensajeDeTexto.put("imagen", descargarUri.toString());

        Map<String, Object> mensajeDeTextoDetalles = new HashMap<>();
        mensajeDeTextoDetalles.put(mensajeSenderRef1 + "/" + mensajePushID, mensajeDeTexto);

        RootRef.updateChildren(mensajeDeTextoDetalles).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SelectorImagenes.this, "Imagen Enviada", Toast.LENGTH_SHORT).show();
                    irAtras();
                } else {
                    String mensajeError = Objects.requireNonNull(task.getException()).toString();
                    Toast.makeText(SelectorImagenes.this, "Error: " + mensajeError, Toast.LENGTH_SHORT).show();
                    irAtras();
                }

            }
        });
    }


    public void irAtras() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onBackPressed() {
        irAtras();
    }

}
