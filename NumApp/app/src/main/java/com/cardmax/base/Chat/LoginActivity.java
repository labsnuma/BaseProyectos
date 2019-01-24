package com.cardmax.base.Chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cardmax.base.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.cardmax.base.Muro.MuroMainActivity;


//
public class LoginActivity extends AppCompatActivity {
    // private FirebaseUser currentUser;
    private Button LoginButton, PhoneLoginButton;
    private EditText UserEmail, UserPassword;
    private Button NeedNewAccountLink, OlvideMiContraseña;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private TextView titulo;

    //ANIMACION 1 LOGIN
    private RelativeLayout rellay1, rellay2;
    private ImageView imagen1;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
            imagen1.setVisibility(View.VISIBLE);
        }
    };
    //FANAMICACION
    private DatabaseReference UserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_login_animacion);


        //ANIMACION
        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);
        imagen1 = (ImageView) findViewById(R.id.login_image);
        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash
        imagen1.setAlpha(100);
        int opacity = 80; // from 0 to 255
        imagen1.setBackgroundColor(opacity * 0x1000000);
        //FANIMACION

        titulo = (TextView) findViewById(R.id.tv_login);
        Typeface fuente =Typeface.createFromAsset(getAssets(),"fonts/MrDafoe-Regular.ttf");
        titulo.setTypeface(fuente);

        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        //  currentUser = mAuth.getCurrentUser();
        intializeFileds();
        NeedNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToRegisterActivity();
            }
        });
     OlvideMiContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarContraseñaActivity();
            }
        });



        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToLogin();
            }
        });

    /*    PhoneLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneLoginIntent=new Intent(LoginActivity.this,PhoneLoginActivity.class);
                startActivity(phoneLoginIntent);
            }
        });*/
    }



    private void AllowUserToLogin() {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Porfavor digite el email", Toast.LENGTH_SHORT).show();

        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Porfavor digite el password", Toast.LENGTH_SHORT).show();

        } else {
            loadingBar.setTitle("Cargando sesión");
            loadingBar.setMessage("Porfavor espera mientras entras a tu cuenta");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email, password).
                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                //notificaciones por token
                                String currentUserID = mAuth.getCurrentUser().getUid();
                                String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                UserRef.child(currentUserID).child("token")
                                        .setValue(deviceToken)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    SendUserToInicioActivity();
                                                  //  Toast.makeText(LoginActivity.this, "Inocio de sesión correcto", Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();

                                                }
                                            }
                                        });

                            } else {
                                String mensaje = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Error: " + mensaje, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }

                        }
                    });
        }

    }

    private void intializeFileds() {
        LoginButton = (Button) findViewById(R.id.login_button);
        //   PhoneLoginButton = (Button) findViewById(R.id.phone_login_button);
        UserEmail = (EditText) findViewById(R.id.login_email);
        UserPassword = (EditText) findViewById(R.id.login_password);
        NeedNewAccountLink = (Button) findViewById(R.id.need_newaccount_link);
        OlvideMiContraseña = (Button) findViewById(R.id.forget_passowrd_link);
        loadingBar = new ProgressDialog(this);

    }

/*    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser != null) {
            SendUserToInicioActivity();
        }
    }*/


    private void SendUserToRegisterActivity() {
        Intent RegisterIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(RegisterIntent);
    }


 /*   private void SendUserToInicioActivity() {
        Intent IncioIntent = new Intent(LoginActivity.this, InicioActivity.class);
        IncioIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(IncioIntent);
        finish();
    }*/
 private void SendUserToInicioActivity() {
     Intent IncioIntent = new Intent(LoginActivity.this, MuroMainActivity.class);
     IncioIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
     startActivity(IncioIntent);
     finish();
 }






    private void EnviarContraseñaActivity() {
        Intent ContraseñaIntent = new Intent(LoginActivity.this, OlvidarPasswordActivity.class);
        startActivity(ContraseñaIntent);
    }

}
