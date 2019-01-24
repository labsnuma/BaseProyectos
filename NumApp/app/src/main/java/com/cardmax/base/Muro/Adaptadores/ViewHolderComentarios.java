package com.cardmax.base.Muro.Adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cardmax.base.R;

public class ViewHolderComentarios extends RecyclerView.ViewHolder {
    public ImageView perfil_user;
    public TextView nombre_user, contenido, tiempo;


    public ViewHolderComentarios(View itemView) {
        super(itemView);

        perfil_user = (ImageView)itemView.findViewById(R.id.image_perfil_comentarios);
        nombre_user = (TextView) itemView.findViewById(R.id.name_comentario);
        contenido = (TextView) itemView.findViewById(R.id.contenido_comentario);
        tiempo = (TextView) itemView.findViewById(R.id.time_comentario);
    }
}
