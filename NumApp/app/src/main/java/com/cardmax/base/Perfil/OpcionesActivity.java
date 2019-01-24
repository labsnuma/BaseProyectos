package com.cardmax.base.Perfil;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.cardmax.base.Chat.InicioActivity;
import com.cardmax.base.Chat.Recursos.IraActividades;
import com.cardmax.base.Chat.Recursos.SelectorImagenes;
import com.cardmax.base.Chat.RegisterActivity;
import com.cardmax.base.MainActivity;
import com.cardmax.base.Muro.CrearPublicacion;
import com.cardmax.base.Muro.MuroMainActivity;
import com.cardmax.base.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class OpcionesActivity extends AppCompatActivity {

    private Button updateAccountSettings, close;
    private EditText username, userstatus, carrera, semestre, ciudad, direccion, telefono, fecha, intereses, estado_civil;
    private RadioGroup genero;
    private String fechapick;
    private ImageView userprofileimage;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private StorageReference UserProfileImageReference;
    private ProgressDialog loadingbar;
    private String generoR = null;
    private Toolbar settingsToolbar;
    private RadioButton radio1, radio2;
    public String retriveImagenPerfil;
    public String retriveUsername;
    IraActividades actividadesir = new IraActividades();
    private Spinner spin_carrera, spin_semestre, spin_ciudad, spin_fecha, spin_civil, spin_intereses;
    private List<String> list_carrera, ciudadeslist, intereseslist;
    private String item_genero = "", item_ciudades = "", item_programas = "", item_semestres = "", item_fecha = "", item_estadocivil = "", item_intereses;
    private Integer numero_edadini = 0, numeroedad_final = 0, style_pub = 0, num_semestre = 0;
    private int inicio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_activity_opciones);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        UserProfileImageReference = FirebaseStorage.getInstance().getReference().child("Imagenes perfil");
        spin_carrera = (Spinner) findViewById(R.id.list_carrera_spin);
        spin_semestre = (Spinner) findViewById(R.id.list_semestre_spin);
        spin_ciudad = (Spinner) findViewById(R.id.list_ciudad_spin);
        spin_fecha = (Spinner) findViewById(R.id.list_year_spin);
        spin_civil = (Spinner) findViewById(R.id.list_civil_spin);
        spin_intereses = (Spinner) findViewById(R.id.list_intereses_spin);


        list_carrera = new ArrayList<String>();
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
                    list_carrera.add(x);
                    count = count + 1;
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        list_carrera.add(0, "");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, list_carrera);

        spin_carrera.setAdapter(adapter);


        spin_carrera.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_programas = (String) parent.getItemAtPosition(position);
                if (item_programas.equals("")) {
                    String verificacion;
                    verificacion = carrera.getText().toString();
                    if (!verificacion.equals("")) {
                        spin_carrera.setVisibility(View.INVISIBLE);
                    }
                } else {
                    spin_carrera.setVisibility(View.GONE);
                    carrera.setText(item_programas);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        String[] letra = {"", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, letra);
        spin_semestre.setAdapter(adapter2);


        spin_semestre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                item_semestres = (String) parent.getItemAtPosition(position);

                if (item_semestres.equals("")) {

                    String verificacion;
                    verificacion = semestre.getText().toString();
                    if (!verificacion.equals("")) {

                    }


                } else {
                    spin_semestre.setVisibility(View.GONE);
                    semestre.setText(item_semestres);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ciudadeslist.add(0, "");
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, ciudadeslist);

        spin_ciudad.setAdapter(adapter4);

        spin_ciudad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_ciudades = (String) parent.getItemAtPosition(position);

                if (item_ciudades.equals("")) {

                    String verificacion;
                    verificacion = ciudad.getText().toString();
                    if (!verificacion.equals("")) {

                    }


                } else {
                    spin_ciudad.setVisibility(View.GONE);
                    ciudad.setText(item_ciudades);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        intereseslist = new ArrayList<String>();
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        intereseslist.add(0, "");
        ArrayAdapter<String> adapterint = new ArrayAdapter<String>(this,
                R.layout.spinner_item, intereseslist);

        spin_intereses.setAdapter(adapterint);


        spin_intereses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_intereses = (String) parent.getItemAtPosition(position);

                if (item_intereses.equals("")) {

                    String verificacion;
                    verificacion = intereses.getText().toString();
                    if (!verificacion.equals("")) {

                    }


                } else {
                    spin_intereses.setVisibility(View.GONE);
                    intereses.setText(item_intereses);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        String[] year = {"", "1950", "1951", "1952", "1953", "1954", "1955", "1956", "1957", "1958", "1959", "1960", "1961", "1962", "1963", "1964", "1965",
                "1966", "1967", "1968", "1969", "1970", "1971", "1972", "1973", "1974", "1975", "1976", "1977", "1978", "1979", "1980", "1981",
                "1982", "1983", "1984", "1985", "1986", "1987", "1988", "1989", "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997",
                "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005"};


        ArrayAdapter<String> adapteryear = new ArrayAdapter<String>(this,
                R.layout.spinner_item, year);
        spin_fecha.setAdapter(adapteryear);


        spin_fecha.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                item_fecha = (String) parent.getItemAtPosition(position);

                if (item_fecha.equals("")) {

                    String verificacion;
                    verificacion = fecha.getText().toString();
                    if (!verificacion.equals("")) {

                    }


                } else {
                    spin_fecha.setVisibility(View.GONE);
                    fecha.setText(item_fecha);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        String[] civil = {"", "Casado", "Soltero"};

        ArrayAdapter<String> adapter_civil = new ArrayAdapter<String>(this,
                R.layout.spinner_item, civil);
        spin_civil.setAdapter(adapter_civil);


        spin_civil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                item_estadocivil = (String) parent.getItemAtPosition(position);

                if (item_estadocivil.equals("")) {

                    String verificacion;
                    verificacion = estado_civil.getText().toString();
                    if (!verificacion.equals("")) {

                    }


                } else {
                    spin_civil.setVisibility(View.GONE);
                    estado_civil.setText(item_estadocivil);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        initializeFileds();
        inicio = getIntent().getExtras().getInt("inicio");
        if (inicio == 1) {
            close.setVisibility(View.INVISIBLE);
        } else {
            close.setVisibility(View.VISIBLE);
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        updateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActaulizarOpciones();
            }
        });
        RetriveUserInfo();
        userprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GalariaPick);*/
                String mFotoStorageString = "Imagenes perfil/";
                String mNombreStorageString = currentUserID + ".jpg";
                String mFotoDatabaseString = "Users/" + currentUserID;
                String mNombreDataString = "image";

                Intent pruebaimagenes = new Intent(OpcionesActivity.this, SelectorImagenes.class);
                pruebaimagenes.putExtra("stroageRef", mFotoStorageString);
                pruebaimagenes.putExtra("nombreStorageRef", mNombreStorageString);
                pruebaimagenes.putExtra("opcion", 1);
                pruebaimagenes.putExtra("databaseRef", mFotoDatabaseString);
                pruebaimagenes.putExtra("nombreDataRef", mNombreDataString);
                pruebaimagenes.putExtra("nDatasender", "vacio");
                pruebaimagenes.putExtra("nDatareciver", "vacio");
                pruebaimagenes.putExtra("grupoRef", "vacio");
                pruebaimagenes.putExtra("nFotoRef", "vacio");
                startActivity(pruebaimagenes);

            }
        });


    }


    private void initializeFileds() {
        updateAccountSettings = (Button) findViewById(R.id.update_settings_button);
        username = (EditText) findViewById(R.id.set_user_name);
        intereses = (EditText) findViewById(R.id.reg_intereses);
        estado_civil = (EditText) findViewById(R.id.reg_estado_civil);

        userstatus = (EditText) findViewById(R.id.set_profile_status);
        close = (Button) findViewById(R.id.btn_close_perfil);
        carrera = (EditText) findViewById(R.id.reg_carrera);
        semestre = (EditText) findViewById(R.id.reg_semestre);
        ciudad = (EditText) findViewById(R.id.reg_ciudad);
        direccion = (EditText) findViewById(R.id.reg_direccion);
        telefono = (EditText) findViewById(R.id.reg_telefono);
        fecha = (EditText) findViewById(R.id.reg_fecha);


        genero = (RadioGroup) findViewById(R.id.genero_radio);
        radio1 = (RadioButton) findViewById(R.id.RDhombre);
        radio2 = (RadioButton) findViewById(R.id.RDmujer);


        userprofileimage = (ImageView) findViewById(R.id.set_profile_image);


        loadingbar = new ProgressDialog(this);


        //  settingsToolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        //  setSupportActionBar(settingsToolbar);
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  getSupportActionBar().setDisplayShowCustomEnabled(true);
        //  getSupportActionBar().setDisplayShowHomeEnabled(true);
        //  getSupportActionBar().setTitle(R.string.configuracion_perfil);
        /*   getSupportActionBar().setDisplayShowHomeEnabled(false);*/


    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.RDhombre:
                if (checked)
                    generoR = "Masculino";
                radio1.setError(null);
                radio2.setError(null);
                break;
            case R.id.RDmujer:
                if (checked)
                    generoR = "Femenino";
                radio1.setError(null);
                radio2.setError(null);
                break;
        }
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalariaPick && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                loadingbar.setTitle("Estableciendo imagen de perfil");
                loadingbar.setMessage("Porfavor espera, estamos actualizando tu imagen  ");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.setProgressNumberFormat("Subiendo");
                loadingbar.show();

                Uri resultUri = result.getUri();
                StorageReference filepath = UserProfileImageReference.child(currentUserID + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {


                            Toast.makeText(OpcionesActivity.this, "Imagen Cargada correctamente", Toast.LENGTH_SHORT).show();
                            final String downloadUrl = task.getResult().getDownloadUrl().toString();

                            RootRef.child("Users").child(currentUserID).child("image")
                                    .setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(OpcionesActivity.this, "Imagen guardada  en Firebase correctamente ", Toast.LENGTH_SHORT).show();
                                                loadingbar.dismiss();
                                            } else {
                                                String mensaje = task.getException().toString();
                                                Toast.makeText(OpcionesActivity.this, "Error: " + mensaje, Toast.LENGTH_SHORT).show();
                                                loadingbar.dismiss();
                                            }
                                        }
                                    });

                        } else {
                            String mensaje = task.getException().toString();
                            Toast.makeText(OpcionesActivity.this, "Error: " + mensaje, Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }
                    }
                });


            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();

            }


        }


    }
*/

    private void ActaulizarOpciones() {

        String setUserName = username.getText().toString().trim();
        String setUserStatus = userstatus.getText().toString().trim();
        String setCarrera = carrera.getText().toString().trim();
        Integer setSemestre = Integer.parseInt(semestre.getText().toString());
        String setCiudad = ciudad.getText().toString().trim();
        String setDireccion = direccion.getText().toString().trim();
        String setTelefono = telefono.getText().toString().trim();
        Integer setFecha = Integer.parseInt(fecha.getText().toString());
        String setGenero = generoR;
        String setIntereses = intereses.getText().toString().trim();
        String setEstadoCivil = estado_civil.getText().toString().trim();


        if (TextUtils.isEmpty(setUserName)) {
            username.setError(getString(R.string.verificar_nombre));
            username.requestFocus();
        } else if (TextUtils.isEmpty(setUserStatus)) {
            userstatus.setError(getString(R.string.verificar_estado));
            userstatus.requestFocus();
        }
        if (TextUtils.isEmpty(setCarrera)) {
            carrera.setError(getString(R.string.verificar_carrera));
            carrera.requestFocus();
        }

        if (TextUtils.isEmpty(setCiudad)) {
            ciudad.setError(getString(R.string.verificar_ciudad));
            ciudad.requestFocus();
        }
        if (TextUtils.isEmpty(setDireccion)) {
            direccion.setError(getString(R.string.verificar_direccion));
            direccion.requestFocus();
        }
        if (TextUtils.isEmpty(setTelefono)) {
            telefono.setError(getString(R.string.verificar_telefono));
            telefono.requestFocus();
        }

        if (TextUtils.isEmpty(setGenero)) {
            radio1.setError(getString(R.string.verificar_genero));
            radio2.setError(getString(R.string.verificar_genero));
            radio1.requestFocus();
            Toast.makeText(this, getString(R.string.verificar_genero), Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> profileMap = new HashMap<>();
            profileMap.put("uid", currentUserID);
            profileMap.put("name", setUserName);
            profileMap.put("status", setUserStatus);
            profileMap.put("carrera", setCarrera);
            profileMap.put("semestre", setSemestre);
            profileMap.put("ciudad", setCiudad);
            profileMap.put("direccion", setDireccion);
            profileMap.put("telefono", setTelefono);
            profileMap.put("fecha", setFecha);
            profileMap.put("genero", setGenero);
            profileMap.put("intereses", setIntereses);
            profileMap.put("estado_civil", setEstadoCivil);
            //  profileMap.put("image", R.drawable.profile_image);


            RootRef.child("Users").child(currentUserID).updateChildren(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //SendUserToLoginActivity();
                                //  SendUserToInicioActivity();
                                Toast.makeText(OpcionesActivity.this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();


                                ////////////////////7DESDEEE

                                RootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild("image")) {
                                            //Toast.makeText(OpcionesActivity.this, "No hace nada", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.profile_image);
                                            StorageReference filepath = UserProfileImageReference.child(currentUserID + ".jpg");
                                            filepath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                                    if (task.isSuccessful()) {


                                                        Toast.makeText(OpcionesActivity.this, "Imagen Cargada correctamente", Toast.LENGTH_SHORT).show();
                                                        final String downloadUrl = task.getResult().getDownloadUrl().toString();

                                                        RootRef.child("Users").child(currentUserID).child("image")
                                                                .setValue(downloadUrl)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {

                                                                            Toast.makeText(OpcionesActivity.this, "Imagen guardada  en Firebase correctamente ", Toast.LENGTH_SHORT).show();
                                                                            loadingbar.dismiss();
                                                                            SendUserToInicioActivity();

                                                                        } else {
                                                                            String mensaje = task.getException().toString();
                                                                            Toast.makeText(OpcionesActivity.this, "Error: " + mensaje, Toast.LENGTH_SHORT).show();
                                                                            loadingbar.dismiss();
                                                                            SendUserToInicioActivity();
                                                                        }
                                                                    }
                                                                });

                                                    } else {
                                                        String mensaje = task.getException().toString();
                                                        Toast.makeText(OpcionesActivity.this, "Error: " + mensaje, Toast.LENGTH_SHORT).show();
                                                        loadingbar.dismiss();
                                                    }
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                                //////////HASTA ACA
                               /* finish();
                                startActivity(getIntent());*/

                                if (inicio == 1) {

                                    Intent intento;
                                    intento = new Intent(OpcionesActivity.this, MuroMainActivity.class);
                                    startActivity(intento);


                                }

                            } else {
                                String mensaje = task.getException().toString();
                                Toast.makeText(OpcionesActivity.this, "Error: " + mensaje, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }

    }

    private void RetriveUserInfo() {


        RootRef.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image")))) {
                            retriveUsername = dataSnapshot.child("name").getValue().toString();
                            String retriveStatus = dataSnapshot.child("status").getValue().toString();
                            String retriveCarrera = dataSnapshot.child("carrera").getValue().toString();
                            String retriveSemestre = dataSnapshot.child("semestre").getValue().toString();
                            String retriveCiudad = dataSnapshot.child("ciudad").getValue().toString();
                            String retriveDireccion = dataSnapshot.child("direccion").getValue().toString();
                            String retriveTelefono = dataSnapshot.child("telefono").getValue().toString();
                            String retriveFecha = dataSnapshot.child("fecha").getValue().toString();
                            String retriveGenero = dataSnapshot.child("genero").getValue().toString();
                            String retriveIntereses = dataSnapshot.child("intereses").getValue().toString();
                            String retriveCivil = dataSnapshot.child("estado_civil").getValue().toString();


                            retriveImagenPerfil = dataSnapshot.child("image").getValue().toString();


                            username.setText(retriveUsername);
                            userstatus.setText(retriveStatus);
                            carrera.setText(retriveCarrera);
                            semestre.setText(retriveSemestre);
                            intereses.setText(retriveIntereses);
                            estado_civil.setText(retriveCivil);
                            ciudad.setText(retriveCiudad);
                            direccion.setText(retriveDireccion);
                            telefono.setText(retriveTelefono);
                            fecha.setText(retriveFecha);

                            username.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                            userstatus.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                            carrera.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                            semestre.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

                            ciudad.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                            direccion.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                            telefono.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                            fecha.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);


                            if (retriveGenero.equals("Hombre")) {
                                radio1.setChecked(true);
                                generoR = "Hombre";
                            }
                            if (retriveGenero.equals("Mujer")) {
                                radio2.setChecked(true);
                                generoR = "Mujer";
                            }

                            Picasso.get().load(retriveImagenPerfil).into(userprofileimage);

                        } else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))) {
                            String retriveUsername = dataSnapshot.child("name").getValue().toString();
                            String retriveStatus = dataSnapshot.child("status").getValue().toString();
                            String retriveCarrera = dataSnapshot.child("carrera").getValue().toString();
                            String retriveSemestre = dataSnapshot.child("semestre").getValue().toString();
                            String retriveCiudad = dataSnapshot.child("ciudad").getValue().toString();
                            String retriveDireccion = dataSnapshot.child("direccion").getValue().toString();
                            String retriveTelefono = dataSnapshot.child("telefono").getValue().toString();
                            String retriveFecha = dataSnapshot.child("fecha").getValue().toString();
                            String retriveGenero = dataSnapshot.child("genero").getValue().toString();
//                            String retriveImagenPerfil = dataSnapshot.child("image").getValue().toString();

                            username.setText(retriveUsername);
                            userstatus.setText(retriveStatus);
                            carrera.setText(retriveCarrera);
                            semestre.setText(retriveSemestre);
                            ciudad.setText(retriveCiudad);
                            direccion.setText(retriveDireccion);
                            telefono.setText(retriveTelefono);
                            fecha.setText(retriveFecha);

                            if (retriveGenero.equals("Hombre")) {
                                radio1.setChecked(true);
                                generoR = "Hombre";
                            }
                            if (retriveGenero.equals("Mujer")) {
                                radio2.setChecked(true);
                                generoR = "Mujer";
                            }


                        } else {
                            username.setVisibility(View.VISIBLE);

                            Toast.makeText(OpcionesActivity.this, "Porfavor actualiza tu informacion de perfil", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    private void SendUserToInicioActivity() {
        Intent IncioIntent = new Intent(OpcionesActivity.this, MuroMainActivity.class);
        IncioIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(IncioIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        actividadesir.iraChatActivity(OpcionesActivity.this);
    }

}
