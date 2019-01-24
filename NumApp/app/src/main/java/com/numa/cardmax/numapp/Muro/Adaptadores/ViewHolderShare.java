package com.numa.cardmax.numapp.Muro.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.numa.cardmax.numapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolderShare extends RecyclerView.ViewHolder  {


    CircleImageView perfil;
    TextView nombre, estado;
    Button enviar;
    View layout, layout2;



    public ViewHolderShare(View itemView) {
        super(itemView);

        perfil = (CircleImageView)itemView.findViewById(R.id.img_perfil_enviar);
        nombre = (TextView)itemView.findViewById(R.id.name_eenviar);
        estado = (TextView)itemView.findViewById(R.id.estado_enviar);
        enviar = (Button)itemView.findViewById(R.id.btn_enviar_chat);
        layout = (View)itemView.findViewById(R.id.layout_x);
        layout2 = (View)itemView.findViewById(R.id.layout_y);

    }
}
