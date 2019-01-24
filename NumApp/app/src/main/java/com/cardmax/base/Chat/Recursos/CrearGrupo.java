package com.cardmax.base.Chat.Recursos;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.cardmax.base.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CrearGrupo extends AppCompatActivity {

    private AlertDialog dialogo;
    private DatabaseReference RootRef;

    public void grupo(final Context context) {

        RootRef = FirebaseDatabase.getInstance().getReference();
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlerDialog);
        builder.setTitle("Crear Grupo: ");
        final EditText groupNameField = new EditText(context);
        groupNameField.setHint("Digite el nombre del Grupo");
        builder.setView(groupNameField);

        builder.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = groupNameField.getText().toString();
                if (TextUtils.isEmpty(groupName)) {
                    Toast.makeText(context, "Porfavor digite el nombre del Grupo", Toast.LENGTH_SHORT).show();
                } else {
                    CrearNuevoGrupo(groupName, context);
                }

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.cancel();
            }
        });

        dialogo = builder.create();
        dialogo.show();

    }

    public void CrearNuevoGrupo(final String groupName, final Context context) {
        RootRef.child("Grupos").child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, groupName + " ha sido creado", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
