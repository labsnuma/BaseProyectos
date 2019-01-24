package com.cardmax.base.Chat.Recursos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.cardmax.base.Chat.LoginActivity;
import com.cardmax.base.Chat.Services.ConfigShared;

public class CerrarSesion extends AppCompatActivity {

    public FirebaseAuth mAuth;

    public void logout(final Context context) {

        mAuth = FirebaseAuth.getInstance();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(" ¿Seguro que quieres cerrar la Sesión? ");
        alertDialogBuilder.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            ConfigShared configShared =new ConfigShared();
                            SharedPreferences sharedPreferences = getSharedPreferences(configShared.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(configShared.LOGGEDIN_SHARED_PREF, false);
                            editor.putString(configShared.SHARED_NAME_CARGADO, "");
                            editor.putString(configShared.CURRENTUSERID_SHARED_PREF, "");
                            editor.apply();
                            ConfigGuardarShared configGuardarShared=new ConfigGuardarShared();
                            SharedPreferences sharedPreferences1 = getSharedPreferences(configGuardarShared.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                            editor1.putString(configGuardarShared.FOTO_PREF, "");
                            editor1.putString(configGuardarShared.NOMBRE_PREF, "");
                            editor1.putString(configGuardarShared.ID_PREF, "");
                            editor1.apply();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        LimpiarMemoria limpiarMemoria=new LimpiarMemoria();
                        limpiarMemoria.deleteCache(context);
                        mAuth.signOut();
                        Intent LoginIntent = new Intent(context, LoginActivity.class);
                        LoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(LoginIntent);
                        finish();

                    }
                });
        alertDialogBuilder.setNegativeButton("No ",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
