package com.numa.cardmax.numapp.Chat.Recursos;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class SalirAplicacion {

    public static long lastClickTime;

    public void ahora(Context context, Activity ctx, String mensaje, long tiempo) {
        if (ctx != null && !mensaje.isEmpty() && tiempo != 0) {
            if (lastClickTime + tiempo > System.currentTimeMillis()) {
                salir(context, ctx);

            } else {
                Toast.makeText(ctx, mensaje, Toast.LENGTH_SHORT).show();
                lastClickTime = System.currentTimeMillis();
            }
        }
    }

    private void salir(Context ctx1, Activity ctx) {

        ActivityCompat.finishAffinity(ctx);
        System.exit(1);
    }
}
