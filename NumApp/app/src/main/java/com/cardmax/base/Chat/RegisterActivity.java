package com.cardmax.base.Chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.cardmax.base.Muro.MuroMainActivity;
import com.cardmax.base.Perfil.OpcionesActivity;
import com.cardmax.base.Perfil.PerfilMainActivity;
import com.cardmax.base.R;

public class RegisterActivity extends AppCompatActivity {

    private Button CrateAaccountButton;
    private EditText UserEmail, UserPassword;
    private TextView AlereadyHaveaAccountLink;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_registro);

        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();
        intializeFileds();
        AlereadyHaveaAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToLoginActivity();
            }
        });

        CrateAaccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });


    }

    private void CreateNewAccount() {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Porfavor digite el email", Toast.LENGTH_SHORT).show();

        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Porfavor digite el passord", Toast.LENGTH_SHORT).show();

        } else {
            loadingBar.setTitle("Creando Nueva Cuenta");
            loadingBar.setMessage("Porfavor espera mientras se crea tu cuenta");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //SendUserToLoginActivity();
                                final String CurrentUserID =mAuth.getCurrentUser().getUid();
                                RootRef.child("Users").child(CurrentUserID).setValue("");

                                String deviceToken = FirebaseInstanceId.getInstance().getToken();

                                RootRef.child("Users").child(CurrentUserID).child("token")
                                        .setValue(deviceToken).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            RootRef.child("Users").child(CurrentUserID).child("rol").setValue("user");
                                            Intent intento;
                                            intento = new Intent(RegisterActivity.this, OpcionesActivity.class);
                                            intento.putExtra("inicio", 1);
                                            startActivity(intento);
                                            Toast.makeText(RegisterActivity.this, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }

                                    }
                                });
                              /*  SendUserToIncioActivity();
                                Toast.makeText(RegisterActivity.this, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();*/
                            } else {
                                String mensaje = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Error: " + mensaje, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });

        }
    }

    private void intializeFileds() {
        CrateAaccountButton = (Button) findViewById(R.id.register_button);
        UserEmail = (EditText) findViewById(R.id.register_email);
        UserPassword = (EditText) findViewById(R.id.register_password);
        AlereadyHaveaAccountLink = (TextView) findViewById(R.id.already_have_account_link);
        loadingBar = new ProgressDialog(this);

    }

    private void SendUserToLoginActivity() {
        Intent LoginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(LoginIntent);
    }

    private void SendUserToIncioActivity() {
        Intent IncioIntent = new Intent(RegisterActivity.this, OpcionesActivity.class);
        IncioIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(IncioIntent);
        finish();
    }

}
