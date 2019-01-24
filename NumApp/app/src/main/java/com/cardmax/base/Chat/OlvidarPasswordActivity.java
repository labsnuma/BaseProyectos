package com.cardmax.base.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.cardmax.base.R;

public class OlvidarPasswordActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Button enviarcontraseñaBoton;
    private EditText digiarcorreo;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_olvidar_password);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Restablecer Contraseña");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        enviarcontraseñaBoton = (Button) findViewById(R.id.enviar);
        digiarcorreo = (EditText) findViewById(R.id.olvidar);

        enviarcontraseñaBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usarioEmail = digiarcorreo.getText().toString();
                if (TextUtils.isEmpty(usarioEmail)) {
                    Toast.makeText(OlvidarPasswordActivity.this, "Pofavor digite su email primero!", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.sendPasswordResetEmail(usarioEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(OlvidarPasswordActivity.this, "Revise su corre si quieres resetar su contraseña", Toast.LENGTH_SHORT).show();

                      startActivity(new Intent(OlvidarPasswordActivity.this,LoginActivity.class));
                            }
                            else{
                                String mensaje =task.getException().getMessage();
                                Toast.makeText(OlvidarPasswordActivity.this, "Error: " +mensaje, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

}
