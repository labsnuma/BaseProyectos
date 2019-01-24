package com.numa.cardmax.numapp.Chat.Recursos;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;

public class LimpiarMemoria extends AppCompatActivity {

    public void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
          //  texto_titulo(context);
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {

            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {

                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {

                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile())
            return dir.delete();
        else {
            return false;
        }
    }


 /*   public void texto_titulo(Context context) {

        try {
            Toast toast3 = new Toast(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.msg_limpiar, null);
            TextView txtMsg = layout.findViewById(R.id.txtMensaje);
            txtMsg.setText(context.getResources().getString(R.string.texto_limpiar));
            toast3.setDuration(Toast.LENGTH_SHORT);
            toast3.setView(layout);
            toast3.show();

        } catch (Exception ex) {
            Toast.makeText(context, "Error:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }*/

}
