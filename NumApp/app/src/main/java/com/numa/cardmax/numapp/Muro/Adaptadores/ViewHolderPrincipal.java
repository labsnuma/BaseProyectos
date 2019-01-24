package com.numa.cardmax.numapp.Muro.Adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeThumbnailView;
import com.numa.cardmax.numapp.R;

public class ViewHolderPrincipal extends RecyclerView.ViewHolder {

    ImageView imagen, user;
    VideoView video;
    TextView titulo, fecha, contenido, info, nameuser, cargo, bannerpodcast;
    Button play, pause, reset,  megusta, nomegusta, comentar, compartir, save, playpodcast, pausepodcast, btn_you;
    LinearLayout media ;
    ProgressBar progressBar;
    View layout;
    YouTubeThumbnailView youtubePlayerView ;

    public ViewHolderPrincipal(View itemView) {
        super(itemView);
        user = (ImageView)itemView.findViewById(R.id.img_foto_user);
        media =(LinearLayout)itemView.findViewById(R.id.layout_mediacont);
        megusta = (Button)itemView.findViewById(R.id.btn_like);
        nomegusta = (Button)itemView.findViewById(R.id.btn_dislike);
        comentar = (Button)itemView.findViewById(R.id.btn_comentar);
        imagen = (ImageView) itemView.findViewById(R.id.img_contenido);
        titulo = (TextView) itemView.findViewById(R.id.txt_titulo_user);
        fecha = (TextView) itemView.findViewById(R.id.txt_fecha_publicacion);
        contenido = (TextView) itemView.findViewById(R.id.txt_contenido_publicacion);
        info = (TextView)itemView.findViewById(R.id.txt_likes_comentarios);
        nameuser = (TextView)itemView.findViewById(R.id.txt_user);
        video=(VideoView)itemView.findViewById(R.id.video_publicacion);
        play=(Button)itemView.findViewById(R.id.btn_play);
        pause=(Button)itemView.findViewById(R.id.btn_pause);
        reset=(Button)itemView.findViewById(R.id.btn_reset);
        compartir=(Button)itemView.findViewById(R.id.btn_compartir);
        cargo = (TextView)itemView.findViewById(R.id.txt_cargo);
        save = (Button)itemView.findViewById(R.id.btn_save_publicacion);
        progressBar = (ProgressBar)itemView.findViewById(R.id.progressBar);
        layout = (View) itemView.findViewById(R.id.pod_prin_reproductor);
        bannerpodcast = (TextView)itemView.findViewById(R.id.txt_layout_podcast);
        playpodcast = (Button)itemView.findViewById(R.id.btn_podcast__play_pri);
        pausepodcast =(Button)itemView.findViewById(R.id.btn_podcast_pause_pri);
        youtubePlayerView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_view);
        btn_you = (Button)itemView.findViewById(R.id.btn_play_you);

    }
}
