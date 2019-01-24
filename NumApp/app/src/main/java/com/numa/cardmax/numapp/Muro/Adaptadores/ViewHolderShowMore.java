package com.numa.cardmax.numapp.Muro.Adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.numa.cardmax.numapp.R;

public class ViewHolderShowMore extends RecyclerView.ViewHolder  {

    ImageView imagen;
    VideoView video;
    LinearLayout media ;
    TextView contenido, bannerpodcast;
    ProgressBar progressBar;
    View layout;
    Button play, pause, reset, playpodcast, pausepodcast;



    public ViewHolderShowMore(View itemView) {
        super(itemView);

        imagen = (ImageView) itemView.findViewById(R.id.img_contenido);
        contenido = (TextView) itemView.findViewById(R.id.txt_contenido_publicacion);
        video=(VideoView)itemView.findViewById(R.id.video_publicacion);
        media =(LinearLayout)itemView.findViewById(R.id.layout_mediacont);
        layout = (View) itemView.findViewById(R.id.pod_prin_reproductor);
        bannerpodcast = (TextView)itemView.findViewById(R.id.txt_layout_podcast);
        playpodcast = (Button)itemView.findViewById(R.id.btn_podcast__play_pri);
        pausepodcast =(Button)itemView.findViewById(R.id.btn_podcast_pause_pri);
        progressBar = (ProgressBar)itemView.findViewById(R.id.progressBar);
        play=(Button)itemView.findViewById(R.id.btn_play);
        pause=(Button)itemView.findViewById(R.id.btn_pause);
        reset=(Button)itemView.findViewById(R.id.btn_reset);

    }
}
