package com.cardmax.base.Muro.Adaptadores;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cardmax.base.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.cardmax.base.Muro.Comentarios;
import com.cardmax.base.Muro.MoreActivity;
import com.cardmax.base.Muro.Objetos.ObjetoMuro;
import com.cardmax.base.Muro.Objetos.ObjetoRelacion;
import com.cardmax.base.Muro.Objetos.ObjetoSaveCard;
import com.cardmax.base.Muro.Objetos.ObjetoUser;
import com.cardmax.base.Muro.ShareActivity;


import java.io.IOException;
import java.util.List;

public class AdaptadorPrincipal extends RecyclerView.Adapter<ViewHolderPrincipal>  {
    private Context cxt;
    private int cou = 0;
    private int cou2 = 0;
    List<ObjetoMuro> listaObjeto;
    private DatabaseReference mDatabase;
    public String fotoperfil, nombreuser;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public String currentUserID = mAuth.getCurrentUser().getUid();
    public String name;

    private String key_youtube="AIzaSyB8MJf557XN9aDBtwFsD7rhhRMC77yBbeM";

    public AdaptadorPrincipal(List<ObjetoMuro> listaObjeto) {
        this.listaObjeto = listaObjeto;
    }


    @NonNull
    @Override
    public ViewHolderPrincipal onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.muro_layout_publicacion_muro, parent, false);
        return new ViewHolderPrincipal(vista);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolderPrincipal holder, final int position) {
        final String[] code = {""};
        final String[] fotoimg = new String[1];

        final int[] countmegusta = {0};
        final int[] countnomegusta = {0};
        countnomegusta[0] = 0;
        countmegusta[0] = 0;
        final int[] info_like = new int[1];
        final int[] info_dislike = new int[1];
        final int[] info_comentarios = new int[1];
        final int[] info_compartido = new int[1];
        int on_like, on_dislike, on_comentarios, on_compartir;


        on_like = listaObjeto.get(position).getOn_like();
        on_dislike = listaObjeto.get(position).getOn_dislike();
        on_comentarios = listaObjeto.get(position).getOn_comentarios();
        on_compartir = listaObjeto.get(position).getOn_compartir();
        info_like[0] = listaObjeto.get(position).getLike_publicacion();
        info_dislike[0] = listaObjeto.get(position).getDislike_publicacion();
        info_comentarios[0] = listaObjeto.get(position).getComentarios_publicacion();
        info_compartido[0] = listaObjeto.get(position).getCompartido_publicacion();


        try {
            DatabaseReference refrerencia = FirebaseDatabase.getInstance().getReference("muro_relacion_like");
            Query q1 = refrerencia.orderByChild("id_user").equalTo(currentUserID);


            q1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> relacion = dataSnapshot.getChildren();
                    int count = 0;
                    String relacion_user, relacion_pub;

                    for (DataSnapshot numrelacion : relacion) {
                        relacion_user = numrelacion.child("id_user").getValue().toString();
                        relacion_pub = numrelacion.child("id_publicacion").getValue().toString();

                        if (relacion_pub.equals(listaObjeto.get(position).getKey()) && relacion_user.equals(currentUserID) && countmegusta[0] == 0) {
                            countmegusta[0] = 1;
                            holder.megusta.setTextColor(Color.parseColor("#00685E"));
                            holder.megusta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_thumb_up_black_24dp, 0, 0, 0);
                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } catch (Exception e) {
            System.out.println("error :: " + e);
        }


        try {
            DatabaseReference refrerencia = FirebaseDatabase.getInstance().getReference("muro_relacion_dislike");
            Query q2 = refrerencia.orderByChild("id_user").equalTo(currentUserID);

            q2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> relacion = dataSnapshot.getChildren();
                    int count = 0;
                    String relacion_user, relacion_pub;

                    for (DataSnapshot numrelacion : relacion) {
                        relacion_user = numrelacion.child("id_user").getValue().toString();
                        relacion_pub = numrelacion.child("id_publicacion").getValue().toString();

                        if (relacion_pub.equals(listaObjeto.get(position).getKey()) && relacion_user.equals(currentUserID)) {
                            countnomegusta[0] = 1;
                            holder.nomegusta.setTextColor(Color.parseColor("#00685E"));
                            holder.nomegusta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.nomegustacolor, 0, 0, 0);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } catch (Exception e) {
            System.out.println("error :: " + e);
        }


        mDatabase = FirebaseDatabase.getInstance().getReference();

        ///////////////////////////////Registro de Actividad///////////////////////////////////////////

      //  Query registroConsulta = mDatabase.





        ///////////////////////////////////////////////////////////////////////////////////////////////



        try {
            mDatabase.child("Users").child(listaObjeto.get(position).id_usuario.toString()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ObjetoUser userx = dataSnapshot.getValue(ObjetoUser.class);
                            name = userx.name; // "John Doe"
                            fotoimg[0] = userx.image; // "Texas"
                            holder.nameuser.setText(name);
                            Glide.with(holder.user.getContext())
                                    .load(fotoimg[0])
                                    .into(holder.user);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } catch (Exception e) {
            System.out.println(e);
        }

        holder.titulo.setText(listaObjeto.get(position).getTitulo_publicacion());
        holder.fecha.setText(listaObjeto.get(position).getFecha_publicacion());
        holder.contenido.setText(listaObjeto.get(position).getContenido_publicacion());

        if(listaObjeto.get(position).getTitulo_publicacion().equals("")){
            holder.titulo.setVisibility(View.GONE);

        }

        if(listaObjeto.get(position).getContenido_publicacion().equals("")){
            holder.contenido.setVisibility(View.GONE);

        }


        String buscar = listaObjeto.get(position).getImagen_publicacion();
        final int buscando = buscar.indexOf("video");
        final int buscandoaudio = buscar.indexOf("AudioMuro");
        final int buscandoimage = buscar.indexOf("images");
        final int buscandoyoutube = buscar.indexOf("youtube");
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
            holder.video.setVideoURI(Uri.parse(listaObjeto.get(position).getImagen_publicacion().toString()));

        } else if (buscandoimage != -1) {
            holder.media.setVisibility(View.GONE);
            holder.play.setVisibility(View.GONE);

            holder.progressBar.setVisibility(View.VISIBLE);
            Glide.with(holder.imagen.getContext())
                    .load(listaObjeto.get(position).getImagen_publicacion())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            System.out.println("nuevo error ->" + e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })

                    .into(holder.imagen);


        } else if (buscandoaudio != -1) {

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
                mplayer.setDataSource(listaObjeto.get(position).getImagen_publicacion().toString());
                mplayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            holder.playpodcast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (mplayer.isPlaying()) {
                        mplayer.pause();
                        holder.playpodcast.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_media_play_dark, 0, 0, 0);
                    } else {
                        mplayer.start();
                        holder.playpodcast.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_media_pause_dark, 0, 0, 0);

                    }


                }
            });


        }else if(buscandoyoutube != -1){
            holder.youtubePlayerView.setVisibility(View.VISIBLE);
            holder.youtubePlayerView.initialize(key_youtube, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {



                    if(listaObjeto.get(position).getContenido_publicacion().indexOf("https://www.youtube.com/watch?v=") != -1){

                        code[0] = listaObjeto.get(position).getContenido_publicacion().replace("https://www.youtube.com/watch?v=","");
                        youTubeThumbnailLoader.setVideo(code[0]);
                        holder.btn_you.setVisibility(View.VISIBLE);
                    } else if(listaObjeto.get(position).getContenido_publicacion().indexOf("https://www.youtube.com/") != -1){

                        code[0] = listaObjeto.get(position).getContenido_publicacion().replace("https://www.youtube.com/","");
                        youTubeThumbnailLoader.setVideo(code[0]);
                        holder.btn_you.setVisibility(View.VISIBLE);
                    } else if(listaObjeto.get(position).getContenido_publicacion().indexOf("https://youtu.be/") != -1){

                        code[0] = listaObjeto.get(position).getContenido_publicacion().replace("https://youtu.be/","");
                        youTubeThumbnailLoader.setVideo(code[0]);
                        holder.btn_you.setVisibility(View.VISIBLE);
                      }


                    holder.btn_you.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intento;
                            intento = new Intent(holder.imagen.getContext(), MoreActivity.class);
                            String keypulication = listaObjeto.get(position).getKey();
                            intento.putExtra("key", keypulication);
                            intento.putExtra("codeyou", code[0]);

                            holder.imagen.getContext().startActivity(intento, ActivityOptions.makeSceneTransitionAnimation((Activity) holder.imagen.getContext()).toBundle());




                        }
                    });

                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                        @Override
                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                            youTubeThumbnailLoader.release();

                        }

                        @Override
                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                        }
                    });
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                }
            });

        }


        if (on_like == 0) {
            holder.megusta.setVisibility(View.GONE);
        }


        if (on_dislike == 0) {
            holder.nomegusta.setVisibility(View.GONE);
        }

        if (on_comentarios == 0) {
            holder.comentar.setVisibility(View.GONE);
        }

        if (on_compartir == 0) {
            holder.compartir.setVisibility(View.GONE);
        }


        if (on_dislike == 0 || on_comentarios == 0 || on_compartir == 0) {

            LinearLayout.LayoutParams x = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT

            );
            x.weight = 1f;
            x.setMargins(0, 0, 0, 0);

            holder.megusta.setLayoutParams(x);
            holder.nomegusta.setLayoutParams(x);
        }

        if (listaObjeto.get(position).getKey().toString().equals("-LPDpNaiXuSYIVJ4Cz0y")) {
            System.out.println("LIKE 2" + on_like);
            System.out.println("DISLIKE 2" + on_dislike);
            System.out.println("COMEN  2" + on_comentarios);
            System.out.println("COMP 2" + on_compartir);
            System.out.println("---------------");


        }


        holder.compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, name + " publico : \n" + listaObjeto.get(position).getTitulo_publicacion() + "\n" + listaObjeto.get(position).getContenido_publicacion() + " en la  UCC");
                holder.compartir.getContext().startActivity(Intent.createChooser(intent, "Share with"));

                Query consulta_compartir = mDatabase.child("muro_publicaciones").child(listaObjeto.get(position).getKey());

                consulta_compartir.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        ObjetoMuro p = dataSnapshot.getValue(ObjetoMuro.class);
                        info_compartido[0] = p.compartido_publicacion + 1;
                        bannerInfo(info_like[0], info_dislike[0], info_comentarios[0], info_compartido[0], holder);
                        mDatabase.child("muro_publicaciones").child(listaObjeto.get(position).getKey()).child("compartido_publicacion").setValue(info_compartido[0]);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


        holder.comentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intento;
                intento = new Intent(holder.comentar.getContext(), Comentarios.class);
                intento.putExtra("id_publicacion", listaObjeto.get(position).getKey().toString());
                intento.putExtra("foto", fotoperfil);
                intento.putExtra("id_user", currentUserID);
                holder.comentar.getContext().startActivity(intento);
                final Query consulta_comentarios = mDatabase.child("muro_publicaciones").child(listaObjeto.get(position).getKey());


                consulta_comentarios.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        ObjetoMuro p = dataSnapshot.getValue(ObjetoMuro.class);


                        if (info_comentarios[0] != p.comentarios_publicacion) {
                            info_comentarios[0] = p.comentarios_publicacion;
                            bannerInfo(info_like[0], info_dislike[0], info_comentarios[0], info_compartido[0], holder);
                            consulta_comentarios.removeEventListener(this);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });


        if (listaObjeto.get(position).getTop() == 1) {
            holder.contenido.setBackgroundResource(R.drawable.cuadradofijo);
            holder.contenido.setPadding(15, 15, 15, 15);
            holder.cargo.setText("Fijo");
        }


        if (listaObjeto.get(position).getType_pub() == 1) {
            holder.contenido.setBackgroundResource(R.drawable.cuadradoverde);
            holder.contenido.setPadding(15, 15, 15, 15);
            holder.cargo.setText("Promocionado");
        }
        if (listaObjeto.get(position).getType_pub() == 2) {
            holder.contenido.setBackgroundResource(R.drawable.cuadronaraja);
            holder.contenido.setPadding(15, 15, 15, 15);
            holder.cargo.setText("Alerta");
        }

        if (listaObjeto.get(position).getType_pub() == 3) {
            holder.contenido.setBackgroundResource(R.drawable.cuadroazul);
            holder.contenido.setPadding(15, 15, 15, 15);
            holder.cargo.setText("Publicidad");
        }

        if (listaObjeto.get(position).getType_pub() == 4) {
            holder.contenido.setBackgroundResource(R.drawable.cuadrado);
            holder.contenido.setPadding(15, 15, 15, 15);
            holder.cargo.setText("Comunicado");
        }


        holder.save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(holder.save.getContext(), holder.save);
                popupMenu.getMenuInflater().inflate(R.menu.muro_menu_popup_cardview, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String valor = (String) item.getTitle();
                        switch (valor) {


                            case "Guardar":
                                final int[] count = {0};
                                Query q = mDatabase.child("muro_relacion_save")
                                        .orderByChild("id_publicacion")
                                        .equalTo(listaObjeto.get(position).getKey());

                                q.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();


                                        for (DataSnapshot murox : muroChildren) {
                                            ObjetoSaveCard p = murox.getValue(ObjetoSaveCard.class);
                                            if (p.id_user.equals(currentUserID)) {
                                                count[0] += 1;

                                            }

                                        }

                                        if (count[0] == 0) {
                                            try {
                                                DatabaseReference refDb;
                                                refDb = mDatabase.getRef();
                                                final String id = refDb.push().getKey();
                                                ObjetoSaveCard save;
                                                save = new ObjetoSaveCard(currentUserID, listaObjeto.get(position).getKey());
                                                mDatabase.child("muro_relacion_save").child(id).setValue(save);
                                                Toast.makeText(holder.save.getContext(), "Guardada", Toast.LENGTH_SHORT).show();
                                            } catch (Exception e) {
                                                System.out.println(e);
                                            }


                                        } else {
                                            Toast.makeText(holder.save.getContext(), "Ya tienes guardada esta publicacion", Toast.LENGTH_SHORT).show();


                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                                q.keepSynced(true);


                                break;
                            case "Detalles":

                                Intent intento;
                                intento = new Intent(holder.imagen.getContext(), MoreActivity.class);
                                String keypulication = listaObjeto.get(position).getKey();
                                intento.putExtra("key", keypulication);
                                intento.putExtra("codeyou", code[0]);
                                holder.imagen.getContext().startActivity(intento, ActivityOptions.makeSceneTransitionAnimation((Activity) holder.imagen.getContext()).toBundle());




                                break;

                            case "Enviar a un Contacto":

                                Intent reenviar;
                                reenviar = new Intent(holder.imagen.getContext(), ShareActivity.class);
                                String keypulication1 = listaObjeto.get(position).getKey();
                                reenviar.putExtra("key", keypulication1);
                                holder.imagen.getContext().startActivity(reenviar);



                                break;


                        }

                        return true;
                    }
                });

                popupMenu.show();


            }
        });


        final String[] cach1 = {""};
        holder.megusta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference refDb;
                refDb = mDatabase.getRef();
                final String id = refDb.push().getKey();


                if (countmegusta[0] == 0) {
                    countmegusta[0] = 1;


                    try {
                        DatabaseReference refrerencia = FirebaseDatabase.getInstance().getReference("muro_relacion_dislike");
                        Query qremovedislike = refrerencia.orderByChild("id_user").equalTo(currentUserID);
                        qremovedislike.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> relacion = dataSnapshot.getChildren();
                                int count = 0;
                                String relacion_user, relacion_pub, key;

                                for (DataSnapshot numrelacion : relacion) {
                                    relacion_user = numrelacion.child("id_user").getValue().toString();
                                    relacion_pub = numrelacion.child("id_publicacion").getValue().toString();

                                    if (relacion_pub.equals(listaObjeto.get(position).getKey()) && relacion_user.equals(currentUserID)) {
                                        key = numrelacion.child("key").getValue().toString();
                                        mDatabase.child("muro_relacion_dislike").child(key).removeValue();


                                        Query consulta_dislike = mDatabase.child("muro_publicaciones").child(listaObjeto.get(position).getKey());

                                        consulta_dislike.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                ObjetoMuro p = dataSnapshot.getValue(ObjetoMuro.class);
                                                info_dislike[0] = p.dislike_publicacion - 1;
                                                mDatabase.child("muro_publicaciones").child(listaObjeto.get(position).getKey()).child("dislike_publicacion").setValue(info_dislike[0]);


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                        countnomegusta[0] = 0;


                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });


                        qremovedislike.keepSynced(true);

                    } catch (Exception e) {
                        System.out.println("error :: " + e);
                    }

                    holder.nomegusta.setTextColor(Color.parseColor("#FF63686C"));
                    holder.nomegusta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.nomegusta, 0, 0, 0);

                    holder.megusta.setTextColor(Color.parseColor("#00685E"));
                    holder.megusta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.megustacolor, 0, 0, 0);
                    info_like[0] = info_like[0] + 1;
                    bannerInfo(info_like[0], info_dislike[0], info_comentarios[0], info_compartido[0], holder);
                    mDatabase.child("muro_publicaciones").child(listaObjeto.get(position).getKey()).child("like_publicacion").setValue(info_like[0]);
                    ObjetoRelacion insert;
                    insert = new ObjetoRelacion(listaObjeto.get(position).getKey(), currentUserID, id);
                    mDatabase.child("muro_relacion_like").child(id).setValue(insert);


                } else {


                    countmegusta[0] = 0;
                    try {
                        DatabaseReference refrerencia = FirebaseDatabase.getInstance().getReference("muro_relacion_like");
                        Query qaddlike = refrerencia.orderByChild("id_user").equalTo(currentUserID);
                        qaddlike.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> relacion = dataSnapshot.getChildren();
                                int count = 0;
                                String relacion_user, relacion_pub, key, keycard;

                                for (DataSnapshot numrelacion : relacion) {
                                    relacion_user = numrelacion.child("id_user").getValue().toString();
                                    relacion_pub = numrelacion.child("id_publicacion").getValue().toString();
                                    keycard = listaObjeto.get(position).getKey();
                                    if (relacion_pub.equals(listaObjeto.get(position).getKey()) && relacion_user.equals(currentUserID)) {
                                        key = numrelacion.child("key").getValue().toString();


                                        if (keycard.compareTo(relacion_pub) == 0) {
                                            cach1[0] = key;
                                            System.out.println("IGUAL = " + keycard + " KEY= " + relacion_pub);

                                        }
                                    }

                                }

                                if (cach1[0].equals("")) {

                                } else {

                                    mDatabase.child("muro_relacion_like").child(cach1[0]).removeValue();


                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        qaddlike.keepSynced(true);
                    } catch (Exception e) {
                        System.out.println("error :: " + e);
                    }

                    holder.megusta.setTextColor(Color.parseColor("#FF63686C"));
                    holder.megusta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.megusta, 0, 0, 0);

                    info_like[0] = info_like[0] - 1;

                    mDatabase.child("muro_publicaciones").child(listaObjeto.get(position).getKey()).child("like_publicacion").setValue(info_like[0]);


                }


            }
        });


        final String[] cach = {""};
        holder.nomegusta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference refDb;
                refDb = mDatabase.getRef();
                final String id = refDb.push().getKey();

                if (countnomegusta[0] == 0) {

                    /////////////////////////animacion boton megusta /////////////////////////////////////
                    try {
                        DatabaseReference refrerencia = FirebaseDatabase.getInstance().getReference();
                        Query qlike = refrerencia.child("muro_relacion_like")
                                .orderByChild("id_user").equalTo(currentUserID);


                        qlike.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> relacion = dataSnapshot.getChildren();
                                int count = 0;
                                String relacion_user, relacion_pub, key;

                                for (DataSnapshot numrelacion : relacion) {
                                    relacion_user = numrelacion.child("id_user").getValue().toString();
                                    relacion_pub = numrelacion.child("id_publicacion").getValue().toString();

                                    if (relacion_pub.equals(listaObjeto.get(position).getKey()) && relacion_user.equals(currentUserID)) {
                                        key = numrelacion.child("key").getValue().toString();
                                        mDatabase.child("muro_relacion_like").child(key).removeValue();


                                        Query consulta_dislike = mDatabase.child("muro_publicaciones").child(listaObjeto.get(position).getKey());

                                        consulta_dislike.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                ObjetoMuro p = dataSnapshot.getValue(ObjetoMuro.class);
                                                info_like[0] = p.like_publicacion - 1;

                                                mDatabase.child("muro_publicaciones").child(listaObjeto.get(position).getKey()).child("like_publicacion").setValue(info_like[0]);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                        countmegusta[0] = 0;


                                        holder.megusta.setTextColor(Color.parseColor("#63686c"));
                                        holder.megusta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.megusta, 0, 0, 0);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                        qlike.keepSynced(true);

                    } catch (Exception e) {
                        System.out.println("error :: " + e);
                    }
                    //////////////////////////////////////////// /////////////////////////////////////

                    holder.megusta.setTextColor(Color.parseColor("#FF63686C"));
                    holder.megusta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.megusta, 0, 0, 0);

                    countnomegusta[0] = 1;
                    holder.nomegusta.setTextColor(Color.parseColor("#00685E"));
                    holder.nomegusta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.nomegustacolor, 0, 0, 0);
                    info_dislike[0] = info_dislike[0] + 1;
                    bannerInfo(info_like[0], info_dislike[0], info_comentarios[0], info_compartido[0], holder);
                    mDatabase.child("muro_publicaciones").child(listaObjeto.get(position).getKey()).child("dislike_publicacion").setValue(info_dislike[0]);
                    ObjetoRelacion insert;
                    insert = new ObjetoRelacion(listaObjeto.get(position).getKey(), currentUserID, id);
                    mDatabase.child("muro_relacion_dislike").child(id).setValue(insert);
                    countnomegusta[0] = 1;

                } else {

                    DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
                    Query qdislike = referencia.child("muro_relacion_dislike")
                            .orderByChild("id_user").equalTo(currentUserID);


                    qdislike.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Iterable<DataSnapshot> relacion = dataSnapshot.getChildren();
                            int count = 0;
                            String relacion_user, relacion_pub, key, keycard;

                            for (DataSnapshot numrelacion : relacion) {
                                relacion_user = numrelacion.child("id_user").getValue().toString();
                                key = numrelacion.child("key").getValue().toString();
                                relacion_pub = numrelacion.child("id_publicacion").getValue().toString();
                                keycard = listaObjeto.get(position).getKey();


                                if (keycard.compareTo(relacion_pub) == 0) {
                                    cach[0] = key;
                                    System.out.println("IGUAL = " + keycard + " KEY= " + relacion_pub);

                                }
                            }


                            if (cach[0].equals("")) {

                            } else {
                                mDatabase.child("muro_relacion_dislike").child(cach[0]).removeValue();

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                    qdislike.keepSynced(true);


                    countnomegusta[0] = 0;

                    info_dislike[0] = info_dislike[0] - 1;


                    Query consulta_dislike = mDatabase.child("muro_publicaciones").child(listaObjeto.get(position).getKey());

                    consulta_dislike.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            ObjetoMuro p = dataSnapshot.getValue(ObjetoMuro.class);
                            info_dislike[0] = p.dislike_publicacion - 1;
                            bannerInfo(info_like[0], info_dislike[0], info_comentarios[0], info_compartido[0], holder);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    holder.nomegusta.setTextColor(Color.parseColor("#FF63686C"));
                    holder.nomegusta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.nomegusta, 0, 0, 0);

                    bannerInfo(info_like[0], info_dislike[0], info_comentarios[0], info_compartido[0], holder);
                    mDatabase.child("muro_publicaciones").child(listaObjeto.get(position).getKey()).child("dislike_publicacion").setValue(info_dislike[0]);

                    countnomegusta[0] = 0;

                }

                System.out.println("numero == " + countnomegusta[0]);


            }
        });

        holder.imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    final DatabaseReference stoprefer = FirebaseDatabase.getInstance().getReference("muro_relacion_like");
                    final Query q9 = stoprefer.orderByChild("id_user").equalTo(currentUserID);

                    final ValueEventListener listener = q9.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            Iterable<DataSnapshot> relacion = dataSnapshot.getChildren();
                            int count = 0;
                            String relacion_user, relacion_pub;

                            for (DataSnapshot numrelacion : relacion) {
                                relacion_user = numrelacion.child("id_user").getValue().toString();
                                relacion_pub = numrelacion.child("id_publicacion").getValue().toString();

                                if (relacion_pub.equals(listaObjeto.get(position).getKey()) && relacion_user.equals(currentUserID) && countmegusta[0] == 0) {
                                    countmegusta[0] = 1;
                                    holder.megusta.setTextColor(Color.parseColor("#00685E"));
                                    holder.megusta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_thumb_up_black_24dp, 0, 0, 0);
                                } else {
                                    countmegusta[0] = 0;
                                    holder.megusta.setTextColor(Color.parseColor("#FF63686C"));
                                    holder.megusta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.megusta, 0, 0, 0);

                                }
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            q9.removeEventListener(listener);
                        }
                    }, 25000);


                } catch (Exception e) {
                    System.out.println("error :: " + e);
                }


                Intent intento;
                intento = new Intent(holder.imagen.getContext(), MoreActivity.class);
                String keypulication = listaObjeto.get(position).getKey();
                intento.putExtra("key", keypulication);
                intento.putExtra("codeyou", code[0]);
                holder.imagen.getContext().startActivity(intento, ActivityOptions.makeSceneTransitionAnimation((Activity) holder.imagen.getContext()).toBundle());


            }
        });


        final Query consulta_comentarios = mDatabase.child("muro_publicaciones").child(listaObjeto.get(position).getKey());


        consulta_comentarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ObjetoMuro p = dataSnapshot.getValue(ObjetoMuro.class);
                info_like[0] = p.like_publicacion;
                info_dislike[0] = p.dislike_publicacion;
                info_comentarios[0] = p.comentarios_publicacion;
                info_compartido[0] = p.compartido_publicacion;

                bannerInfo(p.like_publicacion, p.dislike_publicacion, p.comentarios_publicacion, p.compartido_publicacion, holder);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return listaObjeto.size();
    }


    public void bannerInfo(int info_like, int info_dislike, int info_comentarios, int info_compartido, ViewHolderPrincipal holder) {

        if (info_like == 0 && info_dislike == 0 && info_comentarios == 0) {
            holder.info.setText("");
            if (info_compartido == 1) {
                holder.info.setText(info_compartido + " vez compartido");

            } else if (info_compartido > 1) {
                holder.info.setText(info_compartido + " veces compartido");
            }

        } else if (info_like > 0 && info_dislike == 0 && info_comentarios == 0) {
            holder.info.setText(info_like + " me gusta  ");

            if (info_compartido == 1) {
                holder.info.setText(info_like + " me gusta  • " + info_compartido + " vez compartido");

            } else if (info_compartido > 1) {
                holder.info.setText(info_like + " me gusta  • " + info_compartido + " veces compartido");
            }


        } else if (info_like == 0 && info_dislike > 0 && info_comentarios == 0) {
            holder.info.setText(+info_dislike + " no me gusta");

            if (info_compartido == 1) {
                holder.info.setText(+info_dislike + " no me gusta • " + info_compartido + " vez compartido");

            } else if (info_compartido > 1) {
                holder.info.setText(+info_dislike + " no me gusta • " + info_compartido + " veces compartido");
            }

        } else if (info_like > 0 && info_dislike > 0 && info_comentarios == 0) {
            holder.info.setText(info_like + " me gusta • " + info_dislike + " no me gusta  ");

            if (info_compartido == 1) {
                holder.info.setText(info_like + " me gusta • " + info_dislike + " no me gusta • " + info_compartido + " vez compartido");

            } else if (info_compartido > 1) {
                holder.info.setText(info_like + " me gusta • " + info_dislike + " no me gusta • " + info_compartido + " veces compartido");
            }

        } else if (info_like == 0 && info_dislike == 0 && info_comentarios > 0) {

            if (info_comentarios == 1) {
                holder.info.setText(+info_comentarios + " comentario");

                if (info_compartido == 1) {
                    holder.info.setText(+info_comentarios + " comentario • " + info_compartido + " vez compartido");

                } else if (info_compartido > 1) {
                    holder.info.setText(+info_comentarios + " comentario • " + info_compartido + " veces compartido");
                }

            } else if (info_comentarios > 1) {
                holder.info.setText(+info_comentarios + " comentarios");
                if (info_compartido == 1) {
                    holder.info.setText(+info_comentarios + " comentarios • " + info_compartido + " vez compartido");

                } else if (info_compartido > 1) {
                    holder.info.setText(+info_comentarios + " comentarios • " + info_compartido + " veces compartido");
                }

            }
        } else if (info_like > 0 && info_dislike == 0 && info_comentarios > 0) {


            if (info_comentarios == 1) {
                holder.info.setText(info_like + " me gusta • " + info_comentarios + " comentario ");
                if (info_compartido == 1) {
                    holder.info.setText(info_like + " me gusta • " + info_comentarios + " comentario • " + info_compartido + " vez compartido");

                } else if (info_compartido > 1) {
                    holder.info.setText(info_like + " me gusta • " + info_comentarios + " comentario • " + info_compartido + " veces compartido");
                }

            } else if (info_comentarios > 1) {
                holder.info.setText(info_like + " me gusta • " + info_comentarios + " comentarios ");

                if (info_compartido == 1) {
                    holder.info.setText(info_like + " me gusta • " + info_comentarios + " comentarios • " + info_compartido + " vez compartido");

                } else if (info_compartido > 1) {
                    holder.info.setText(info_like + " me gusta • " + info_comentarios + " comentarios • " + info_compartido + " veces compartido");
                }
            }
        } else if (info_like == 0 && info_dislike > 0 && info_comentarios > 0) {


            if (info_comentarios == 1) {
                holder.info.setText(+info_dislike + " no me gusta • " + info_comentarios + " comentario ");


                if (info_compartido == 1) {
                    holder.info.setText(+info_dislike + " no me gusta • " + info_comentarios + " comentario • " + info_compartido + " vez compartido");

                } else if (info_compartido > 1) {
                    holder.info.setText(+info_dislike + " no me gusta • " + info_comentarios + " comentario • " + info_compartido + " veces compartido");
                }

            } else if (info_comentarios > 1) {
                holder.info.setText(+info_dislike + " no me gusta • " + info_comentarios + " comentarios ");

                if (info_compartido == 1) {
                    holder.info.setText(+info_dislike + " no me gusta • " + info_comentarios + " comentarios • " + info_compartido + " vez compartido");

                } else if (info_compartido > 1) {
                    holder.info.setText(+info_dislike + " no me gusta • " + info_comentarios + " comentarios • " + info_compartido + " veces compartido");
                }
            }
        } else if (info_like > 0 && info_dislike > 0 && info_comentarios > 0) {

            if (info_comentarios == 1) {
                holder.info.setText(info_like + " me gusta • " + info_dislike + " no me gusta • " + info_comentarios + " comentario ");

                if (info_compartido == 1) {
                    holder.info.setText(info_like + " me gusta • " + info_dislike + " no me gusta • " + info_comentarios + " comentario • " + info_compartido + " vez compartido");

                } else if (info_compartido > 1) {
                    holder.info.setText(info_like + " me gusta • " + info_dislike + " no me gusta • " + info_comentarios + " comentario • " + info_compartido + " veces compartido");
                }
            } else if (info_comentarios > 1) {
                holder.info.setText(info_like + " me gusta • " + info_dislike + " no me gusta • " + info_comentarios + " comentarios ");

                if (info_compartido == 1) {
                    holder.info.setText(info_like + " me gusta • " + info_dislike + " no me gusta • " + info_comentarios + " comentarios • " + info_compartido + " vez compartido");

                } else if (info_compartido > 1) {
                    holder.info.setText(info_like + " me gusta • " + info_dislike + " no me gusta • " + info_comentarios + " comentarios • " + info_compartido + " veces compartido");
                }
            }

            holder.info.setTextSize(10f);


        }


    }


}
