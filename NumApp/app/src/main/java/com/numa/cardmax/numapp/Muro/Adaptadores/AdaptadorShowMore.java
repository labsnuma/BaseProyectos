package com.numa.cardmax.numapp.Muro.Adaptadores;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.numa.cardmax.numapp.Muro.Objetos.ObjetoAddMore;
import com.numa.cardmax.numapp.R;

import java.io.IOException;
import java.util.List;

public class AdaptadorShowMore extends RecyclerView.Adapter<ViewHolderShowMore> {


    List<ObjetoAddMore> listaObjeto;
    private int cou2 = 0;
    private int cou = 0;


    public AdaptadorShowMore(List<ObjetoAddMore> listaObjeto) {
        this.listaObjeto = listaObjeto;
    }


    @NonNull
    @Override
    public ViewHolderShowMore onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.muro_layout_addmore, parent, false);
        return new ViewHolderShowMore(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderShowMore holder, int position) {


        holder.contenido.setText(listaObjeto.get(position).getContenidotxt());

        String buscar = listaObjeto.get(position).getContenidovisual();


        final int buscando = buscar.indexOf("video");
        final int buscandoaudio = buscar.indexOf("AudioMuro");
        final int buscandoimage = buscar.indexOf("images");

        if (buscando != -1) {
            holder.imagen.setVisibility(View.GONE);
            holder.video.start();
            final AudioManager audioManager = (AudioManager) holder.video.getContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cou2 == 0) {
                        cou2 = 1;
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
                        holder.play.setBackground(holder.play.getContext().getDrawable(R.drawable.soundred));
                    } else {
                        cou2 = 0;
                        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                        holder.play.setBackground(holder.play.getContext().getDrawable(R.drawable.sound));
                    }
                }
            });

            holder.video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    holder.video.seekTo(1);
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                    holder.play.setBackground(holder.play.getContext().getDrawable(R.drawable.sound));
                    cou = 1;
                    holder.video.pause();
                    holder.pause.setBackground(holder.pause.getContext().getDrawable(R.drawable.play));
                }
            });

            holder.reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.video.seekTo(1);
                    holder.video.start();
                }
            });

            holder.pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cou == 0) {
                        cou = 1;
                        holder.video.pause();
                        holder.pause.setBackground(holder.pause.getContext().getDrawable(R.drawable.play));
                    } else {
                        cou = 0;
                        holder.video.start();
                        holder.pause.setBackground(holder.pause.getContext().getDrawable(R.drawable.pause));
                    }
                }
            });

            holder.video.setVisibility(View.VISIBLE);
            holder.media.setVisibility(View.VISIBLE);
            holder.video.setVideoURI(Uri.parse(listaObjeto.get(position).getContenidovisual().toString()));

        } else if(buscandoimage !=-1){
            holder.media.setVisibility(View.GONE);
            holder.play.setVisibility(View.GONE);

            holder.progressBar.setVisibility(View.VISIBLE);
            Glide.with(holder.imagen.getContext())
                    .load(listaObjeto.get(position).getContenidovisual())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            System.out.println("nuevo error ->"+e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })

                    .into(holder.imagen);


        }else if(buscandoaudio!=-1){

            holder.layout.setVisibility(View.VISIBLE);

            holder.bannerpodcast.setHorizontallyScrolling(true);
            String text = "<font color=#E0E0E0><marquee>Podcast ♥· </font> <font color=#C0C0C0> ♪ ☺ ♪</font><font color=#000000> ...................</font>";
            holder.bannerpodcast.setText(Html.fromHtml(text));
            holder.bannerpodcast.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            holder.bannerpodcast.setSingleLine(true);
            holder.bannerpodcast.setMarqueeRepeatLimit(20);
            holder.bannerpodcast.setSelected(true);
            final MediaPlayer mplayer = new MediaPlayer();
            try {
                mplayer.setDataSource(listaObjeto.get(position).getContenidovisual().toString());
                mplayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            holder.playpodcast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(mplayer.isPlaying()){
                        mplayer.pause();
                        holder.playpodcast.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_media_play_dark,0,0,0);
                    } else {
                        mplayer.start();
                        holder.playpodcast.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_media_pause_dark,0,0,0);

                    }


                }
            });








        }




        }

    @Override
    public int getItemCount() {
        return listaObjeto.size();
    }
}
