package com.numa.cardmax.numapp.Chat.Recursos;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class PermisosPreguntar  {

    public static final int PERMISO = 100;
    private Context context;
    IraActividades iraActividades =new IraActividades();

    public PermisosPreguntar(Context context) {
        this.context = context;
    }

    public boolean validapermisos(Context context) {
        if ((ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED)

                ) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) ||
                    (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                            Manifest.permission.RECORD_AUDIO))) {

                recomendacion(context);

            } else {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO}
                        , PERMISO);
            }
        }


        return false;
    }

    public void recomendacion(final Context context) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(context);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debes aceptar los permisos para poder continuar");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO}
                        , PERMISO);
            }
        });
        dialogo.show();
    }

    public void solicitarpermisomanual(final Context context) {
        final CharSequence[] opciones = {"Si", "No"};
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("¿Desea configurar los permisos de forma manual?");
        alert.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which].equals("Si")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri url = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(url);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    iraActividades.iraMuroActivity(context);

                }
            }
        });
        alert.show();
    }

}
