package com.cardmax.base.Perfil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cardmax.base.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.cardmax.base.Chat.Recursos.SelectorImagenes;
import com.cardmax.base.Muro.CrearPublicacion;
import com.cardmax.base.Perfil.AdaptadorServ.ObjetoServicio;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;

public class AddServ extends AppCompatActivity {
    private Button ButtonName, cancelar, guardar, closex;
    private EditText nombreserv, precioserv, descripcionserv;
    private String nombre,  descripcion, itemhoraini, itemhorafin, itemhoraextra, urlimagen="";
    private int chekmin = 0, onserv = 0, ontime = 0, precio=0, onimage=0;
    private Switch duracion, showserv, imagen;
    private ImageView imageserv;
    private View layoutduracion;
    private Spinner horaini, horafin, horaextraspin;
    private int type;
    private CheckBox comominimo;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public String currentUserID = mAuth.getCurrentUser().getUid();
    private Calendar c = Calendar.getInstance();
    private String fecha_publicacion;
    private FirebaseDatabase mDatabase;
    private DatabaseReference refDb;
    private Long nombre_imagen;
    private ProgressBar updata;
    private View main;
    private  String key;
    private static final int RC_GALLERY = 21;
    private Uri fotoSeleccionadaURI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_add_serv);

        main = (View)findViewById(R.id.main_layout);
        updata = (ProgressBar)findViewById(R.id.upload);
        nombre_imagen = System.currentTimeMillis();
        mDatabase = FirebaseDatabase.getInstance();
        refDb = mDatabase.getReference();

        imagen=(Switch)findViewById(R.id.sw_img);
        duracion = (Switch)findViewById(R.id.sw_duracion);
        showserv =(Switch)findViewById(R.id.sw_show);

        nombreserv = (EditText)findViewById(R.id.edit_name_serv);
        precioserv =(EditText)findViewById(R.id.edit_precio_serv);
        descripcionserv = (EditText)findViewById(R.id.edit_descripcion_serv);

        horaini = (Spinner)findViewById(R.id.spin_serv_ini);
        horafin =(Spinner)findViewById(R.id.spin_serv_fin);
        horaextraspin = (Spinner)findViewById(R.id.spin_serv_extra);
        closex =(Button)findViewById(R.id.close_addserv);
        cancelar = (Button)findViewById(R.id.btn_cancelar_addserv);
        guardar = (Button)findViewById(R.id.btn_guardar_Serv);
        imageserv=(ImageView)findViewById(R.id.image_serv_add);
        ButtonName = (Button)findViewById(R.id.ButtonName);
        layoutduracion = (View)findViewById(R.id.duracion_layout);
        type = Integer.parseInt(getIntent().getStringExtra("tipo"));
        comominimo =(CheckBox)findViewById(R.id.check_serv_min);

        closex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       try {
           fecha_publicacion = DateFormat.getDateInstance().format(c.getTime());

       }catch (Exception e){
           System.out.println(e);
       }

       cancelar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
             finish();
           }
       });



        imageserv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galeria, RC_GALLERY);

            }
        });





        showserv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showserv.isChecked()){
                    onserv = 1;
                }else{

                    onserv = 0;
                }
            }
        });


        comominimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comominimo.isChecked()){

                    chekmin = 1;
                }else{
                    chekmin = 0;
                }
            }
        });



        duracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(duracion.isChecked()){
                    layoutduracion.setVisibility(View.VISIBLE);
                    ontime = 1;
                } else{
                    ontime = 0;
                    layoutduracion.setVisibility(View.GONE);
                }

            }
        });


        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagen.isChecked()){
                    imageserv.setVisibility(View.VISIBLE);
                    onimage = 1;
                } else{
                    onimage = 0;
                    imageserv.setVisibility(View.GONE);

                }


            }
        });



        String[] hora = {"", "00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00"
                        , "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};

        final ArrayAdapter<String> adapter_ini = new ArrayAdapter<String>(this,
                R.layout.spinner_item, hora);
        horaini.setAdapter(adapter_ini);


        horaini.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                itemhoraini = (String) parent.getItemAtPosition(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        final ArrayAdapter<String> adapter_fin = new ArrayAdapter<String>(this,
                R.layout.spinner_item, hora);
        horafin.setAdapter(adapter_fin);


        horafin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                itemhorafin  = (String) parent.getItemAtPosition(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final String[] horaextra = {"", "5 min","10 min","15 min","25 min", "30 min","1 hora","2 horas"};

        final ArrayAdapter<String> adapter_extra = new ArrayAdapter<String>(this,
                R.layout.spinner_item, horaextra);
        horaextraspin.setAdapter(adapter_extra);


        horaextraspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                itemhoraextra = (String) parent.getItemAtPosition(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });








        if (type == 1){
            ButtonName.setText("Editar");
            key = getIntent().getStringExtra("key");
            DatabaseReference mDatabasecon = FirebaseDatabase.getInstance().getReference();

            Query consulta = mDatabasecon.child("perfil_servicios").child(key);

            consulta.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        nombreserv.setText(dataSnapshot.child("nombre_serv").getValue().toString());
                        precioserv.setText(dataSnapshot.child("precio_serv").getValue().toString());
                        descripcionserv.setText(dataSnapshot.child("descripcion_serv").getValue().toString());
                        fecha_publicacion = dataSnapshot.child("fechapublicacion_serv").getValue().toString();
                        if(Integer.parseInt(dataSnapshot.child("onduracion_serv").getValue().toString())==1 ){
                            duracion.setChecked(true);
                            ontime=1;
                            layoutduracion.setVisibility(View.VISIBLE);
                            horaini.setSelection(adapter_ini.getPosition(dataSnapshot.child("horaini_serv").getValue().toString()));
                            horafin.setSelection(adapter_fin.getPosition(dataSnapshot.child("horafin_serv").getValue().toString()));
                            horaextraspin.setSelection(adapter_extra.getPosition(dataSnapshot.child("tiempo_extra").getValue().toString()));
                            if(Integer.parseInt(dataSnapshot.child("check_minimo_Serv").getValue().toString()) == 1){
                                comominimo.setChecked(true);
                                chekmin=1;

                            }


                        }
                        if(Integer.parseInt(dataSnapshot.child("onservicio_Serv").getValue().toString())==1){
                            showserv.setChecked(true);
                            onserv = 1;

                        }
                        if(Integer.parseInt(dataSnapshot.child("onimage_Serv").getValue().toString())==1){
                            imagen.setChecked(true);
                            onimage=1;
                            urlimagen =dataSnapshot.child("urlimage_serv").getValue().toString();
                            Glide.with(getApplicationContext())
                                    .load(urlimagen)
                                    .into(imageserv);
                            imageserv.setVisibility(View.VISIBLE);
                        }








                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    nombre = nombreserv.getText().toString();
                    precio = Integer.parseInt(precioserv.getText().toString());
                    descripcion = descripcionserv.getText().toString();


                    ObjetoServicio datos = new ObjetoServicio(nombre,precio,descripcion,itemhoraini,ontime,itemhorafin,chekmin,itemhoraextra,onserv,onimage,urlimagen,fecha_publicacion,
                            key,currentUserID);
                    refDb.child("perfil_servicios").child(key).setValue(datos);
                    Toast.makeText(getApplicationContext(), "Publicado!",Toast.LENGTH_LONG).show();
                    finish();

                }
            });






        }else{

            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String id = refDb.push().getKey();
                    nombre = nombreserv.getText().toString();
                    precio = Integer.parseInt(precioserv.getText().toString());
                    descripcion = descripcionserv.getText().toString();


                    ObjetoServicio datos = new ObjetoServicio(nombre,precio,descripcion,itemhoraini,ontime,itemhorafin,chekmin,itemhoraextra,onserv,onimage,urlimagen,fecha_publicacion,
                            id,currentUserID);
                    refDb.child("perfil_servicios").child(id).setValue(datos);
                    Toast.makeText(getApplicationContext(), "Publicado!",Toast.LENGTH_LONG).show();
                    finish();

                }
            });




       }







    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

           if (type == 1){

                final Uri selectedImage = data.getData();

                imageserv.setImageURI(selectedImage);
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(
                            selectedImage);
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


                FirebaseStorage storage = FirebaseStorage.getInstance();
                // Create a storage reference from our app
                final StorageReference storageRef = storage.getReference();
                // Create a reference to "mountains.jpg"
                final StorageReference mountainsRef = storageRef.child("images/" + nombre_imagen);
                // Create a reference to 'images/mountains.jpg'
                StorageReference mountainImagesRef = storageRef.child("images/" + nombre_imagen);

                // While the file names are the same, the references point to different files
                mountainsRef.getName().equals(mountainImagesRef.getName());    // true
                mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false
                final UploadTask[] uploadTask = new UploadTask[1];


                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updata.setVisibility(View.VISIBLE);
                        main.setVisibility(View.GONE);
                        uploadTask[0] = mountainsRef.putBytes(byteArray);
                        uploadTask[0].addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageRef.child("images/" + nombre_imagen).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        nombre = nombreserv.getText().toString();
                                        precio = Integer.parseInt(precioserv.getText().toString());
                                        descripcion = descripcionserv.getText().toString();


                                        ObjetoServicio datos = new ObjetoServicio(nombre,precio,descripcion,itemhoraini,ontime,itemhorafin,chekmin,itemhoraextra,onserv,onimage,uri.toString(),fecha_publicacion,
                                                key,currentUserID);
                                        refDb.child("perfil_servicios").child(key).setValue(datos);
                                        Toast.makeText(getApplicationContext(), "Publicado!",Toast.LENGTH_LONG).show();
                                        finish();

                                    }
                                });

                            }
                        });




                    }
                });




            }else{




                final String id = refDb.push().getKey();

                final Uri selectedImage = data.getData();
                try{
                    imageserv.setImageURI(selectedImage);
                }catch (Exception e){
                    System.out.println("error == "+e);
                }




                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(
                            selectedImage);
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


                FirebaseStorage storage = FirebaseStorage.getInstance();
                // Create a storage reference from our app
                final StorageReference storageRef = storage.getReference();
                // Create a reference to "mountains.jpg"
                final StorageReference mountainsRef = storageRef.child("images/" + nombre_imagen);
                // Create a reference to 'images/mountains.jpg'
                StorageReference mountainImagesRef = storageRef.child("images/" + nombre_imagen);

                // While the file names are the same, the references point to different files
                mountainsRef.getName().equals(mountainImagesRef.getName());    // true
                mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false
                final UploadTask[] uploadTask = new UploadTask[1];


                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updata.setVisibility(View.VISIBLE);
                        main.setVisibility(View.GONE);
                        uploadTask[0] = mountainsRef.putBytes(byteArray);
                        uploadTask[0].addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageRef.child("images/" + nombre_imagen).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        nombre = nombreserv.getText().toString();
                                        precio = Integer.parseInt(precioserv.getText().toString());
                                        descripcion = descripcionserv.getText().toString();


                                        ObjetoServicio datos = new ObjetoServicio(nombre,precio,descripcion,itemhoraini,ontime,itemhorafin,chekmin,itemhoraextra,onserv,onimage,uri.toString(),fecha_publicacion,
                                                id,currentUserID);
                                        refDb.child("perfil_servicios").child(id).setValue(datos);
                                        Toast.makeText(getApplicationContext(), "Publicado!",Toast.LENGTH_LONG).show();
                                        finish();

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
