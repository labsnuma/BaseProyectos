package com.numa.cardmax.numapp.Muro;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.numa.cardmax.numapp.MainActivity;
import com.numa.cardmax.numapp.Muro.Adaptadores.AdaptadorPrincipal;
import com.numa.cardmax.numapp.Muro.Adaptadores.AdaptadorShowMore;
import com.numa.cardmax.numapp.Muro.Objetos.ObjetoAddMore;
import com.numa.cardmax.numapp.Muro.Objetos.ObjetoMuro;
import com.numa.cardmax.numapp.Muro.Objetos.ObjetoRelacion;
import com.numa.cardmax.numapp.Perfil.AdaptadorServ.AdaptadorServicios;
import com.numa.cardmax.numapp.Perfil.AdaptadorServ.ObjetoServicio;
import com.numa.cardmax.numapp.R;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


public class MoreActivity extends YouTubeBaseActivity implements PhotoViewAttacher.OnViewTapListener , YouTubePlayer.OnInitializedListener, YouTubePlayer.PlaybackEventListener{

    private String foto, id_user, fecha, like, dislike, comentarios, compartir, onlike, ondislike, oncomentarios, oncompartir, txttitulo, txtcontenido, nombreuser, fotouser;
    private ImageView user;
    private PhotoView imagen;
    private VideoView video;
    private String keyget;
    private TextView titulo, txtfecha, contenido, info, nameuser, cargo, bannerpodcast, detallesx;
    private Button play, pause, reset, megusta, nomegusta, comentar, btncompartir, save, playpodcast, pausepodcast, closebtn, more;
    private LinearLayout media;
    private ProgressBar progressBar;
    private View layout, contenidox;
    private View up, down, layoutmore;
    private Animation slide_upin;
    private Animation slide_up;
    private Animation slide_downin;
    private Animation slide_down;
    private DatabaseReference mDatabase;
    int info_dislike ;
    int info_like;
    private RecyclerView lista;
    private ArrayList<ObjetoAddMore> listobject;
    private AdaptadorShowMore calladapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public String currentUserID = mAuth.getCurrentUser().getUid();
    int countx = 0;
    private String key_youtube="AIzaSyB8MJf557XN9aDBtwFsD7rhhRMC77yBbeM";
    private YouTubePlayerView player_you;
    private String codeyou;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muro_activity_more);
        more = (Button)findViewById(R.id.btnmas);
        closebtn =(Button)findViewById(R.id.btnclose_more);
        user = (ImageView) findViewById(R.id.img_foto_user);
        media = (LinearLayout) findViewById(R.id.layout_mediacont);
        megusta = (Button) findViewById(R.id.btn_like);
        nomegusta = (Button) findViewById(R.id.btn_dislike);
        comentar = (Button) findViewById(R.id.btn_comentar);
        imagen = (PhotoView) findViewById(R.id.img_contenido);
        titulo = (TextView) findViewById(R.id.txt_titulo_user);
        txtfecha = (TextView) findViewById(R.id.txt_fecha_publicacion);
        contenido = (TextView) findViewById(R.id.txt_contenido_publicacion);
        info = (TextView) findViewById(R.id.txt_likes_comentarios);
        nameuser = (TextView) findViewById(R.id.txt_user);
        video = (VideoView) findViewById(R.id.video_publicacion);
        play = (Button) findViewById(R.id.btn_play);
        pause = (Button) findViewById(R.id.btn_pause);
        reset = (Button) findViewById(R.id.btn_reset);
        btncompartir = (Button) findViewById(R.id.btn_compartir);
        cargo = (TextView) findViewById(R.id.txt_cargo);
        save = (Button) findViewById(R.id.btn_save_publicacion);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        layout = (View) findViewById(R.id.pod_prin_reproductor);
        bannerpodcast = (TextView) findViewById(R.id.txt_layout_podcast);
        playpodcast = (Button) findViewById(R.id.btn_podcast__play_pri);
        pausepodcast = (Button) findViewById(R.id.btn_podcast_pause_pri);
        up = findViewById(R.id.viewUp);
        down = findViewById(R.id.viewDown);
        keyget = getIntent().getStringExtra("key");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        layoutmore =(View)findViewById(R.id.morelayout);
        contenidox = (View)findViewById(R.id.contenido);
        detallesx=(TextView)findViewById(R.id.detalle_S);
        final int[] x = {0};
        player_you = (YouTubePlayerView)findViewById(R.id.youtube_view);
        player_you.initialize(key_youtube, this);
        codeyou = getIntent().getExtras().getString("codeyou");






        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //////////////
        lista = (RecyclerView)findViewById(R.id.recycler_more);
        listobject =new ArrayList<ObjetoAddMore>();
        calladapter =  new AdaptadorShowMore(listobject);
        lista.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        lista.setAdapter(calladapter);
        lista.setLayoutManager(layout);
        ///////////

        final int[] count = {0};
        final Query detalles = mDatabase.child("publicaciones_add_more").orderByChild("id_publicacion").equalTo(keyget);
        detalles.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();

                for (DataSnapshot murox : muroChildren) {
                    try {
                        ObjetoAddMore p = murox.getValue(ObjetoAddMore.class);
                        listobject.add(0, p);

                    }catch (Exception e){
                        System.out.println(e);
                    }


                    count[0] +=1;

                }



                calladapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });













        Query consulta = mDatabase.child("muro_publicaciones").child(keyget);

        consulta.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                txttitulo = dataSnapshot.child("titulo_publicacion").getValue().toString();
                txtcontenido = dataSnapshot.child("contenido_publicacion").getValue().toString();
                foto = dataSnapshot.child("imagen_publicacion").getValue().toString();
                id_user = dataSnapshot.child("id_usuario").getValue().toString();
                fecha = dataSnapshot.child("fecha_publicacion").getValue().toString();
                like = dataSnapshot.child("like_publicacion").getValue().toString();
                dislike = dataSnapshot.child("dislike_publicacion").getValue().toString();
                comentarios = dataSnapshot.child("comentarios_publicacion").getValue().toString();
                compartir = dataSnapshot.child("compartido_publicacion").getValue().toString();
                onlike = dataSnapshot.child("on_like").getValue().toString();
                ondislike = dataSnapshot.child("on_dislike").getValue().toString();
                oncomentarios = dataSnapshot.child("on_comentarios").getValue().toString();
                oncompartir = dataSnapshot.child("on_compartir").getValue().toString();

                titulo.setText(txttitulo);
                txtfecha.setText(fecha);
                contenido.setText(txtcontenido);
                Glide.with(getApplicationContext())
                        .load(foto)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                lista.setVisibility(View.VISIBLE);
                                imagen.setVisibility(View.GONE);
                                more.setVisibility(View.GONE);
                                if (count[0] == 0 )
                                {
                                    detallesx.setVisibility(View.VISIBLE);

                                }

/*
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {

                                        //----------------------------
                                        up.setVisibility(View.VISIBLE);
                                        down.setVisibility(View.VISIBLE);

                                        //----------------------------

                                    }
                                }, 2000);

*/



                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(imagen);



                Query consultauser = mDatabase.child("Users").child(id_user);

                consultauser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        fotouser = dataSnapshot.child("image").getValue().toString();
                        nombreuser = dataSnapshot.child("name").getValue().toString();
                        nameuser.setText(nombreuser);


                        Glide.with(getApplicationContext())
                                .load(fotouser)
                                .into(user);


                               }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


                comentar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intento;
                        intento = new Intent(comentar.getContext(), Comentarios.class);
                        intento.putExtra("id_publicacion", keyget);
                        intento.putExtra("id_user", currentUserID);
                        startActivity(intento);



                    }
                });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        consulta.keepSynced(true);


        btncompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, nombreuser + " publico : \n" + titulo + "\n" + contenido + " en la  UCC");
                startActivity(Intent.createChooser(intent, "Share with"));

                Query consulta_compartir = mDatabase.child("muro_publicaciones").child(keyget);

                consulta_compartir.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        ObjetoMuro p = dataSnapshot.getValue(ObjetoMuro.class);
                        int x ;
                        x = p.compartido_publicacion + 1;

                        mDatabase.child("muro_publicaciones").child(keyget).child("compartido_publicacion").setValue(x);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });




        getWindow().setStatusBarColor(Color.BLACK);

        slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        slide_downin = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.test);

        slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        slide_upin = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.goupmore);


        new Handler().postDelayed(new Runnable() {
            public void run() {

                //----------------------------
                up.startAnimation(slide_up);
                up.setVisibility(View.GONE);

                down.startAnimation(slide_down);
                down.setVisibility(View.GONE);

                //----------------------------

            }
        }, 1000);


        PhotoViewAttacher mAttacher = new PhotoViewAttacher(imagen);
        mAttacher.setOnViewTapListener(this);


        setupWindowAnimations();



        final Query consulta_comentarios = mDatabase.child("muro_publicaciones").child(keyget);


        consulta_comentarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ObjetoMuro p = dataSnapshot.getValue(ObjetoMuro.class);
                info_like = p.like_publicacion;
                info_dislike =p.dislike_publicacion;
                bannerInfo(p.like_publicacion, p.dislike_publicacion, p.comentarios_publicacion, p.compartido_publicacion);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final int[] countmegusta = {0};
        try {
            DatabaseReference refrerencia = FirebaseDatabase.getInstance().getReference("muro_relacion_like");
            Query q = refrerencia.orderByChild("id_user").equalTo(currentUserID);

            q.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> relacion = dataSnapshot.getChildren();
                    int count = 0;
                    String relacion_user, relacion_pub;

                    for (DataSnapshot numrelacion : relacion) {
                        relacion_user = numrelacion.child("id_user").getValue().toString();
                        relacion_pub = numrelacion.child("id_publicacion").getValue().toString();

                        if (relacion_pub.equals(keyget) && relacion_user.equals(currentUserID)) {
                            countmegusta[0] = 1;
                            megusta.setTextColor(Color.parseColor("#00B4AA"));
                            megusta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_thumb_up_lightgreen_24dp, 0, 0, 0);
                        }else{
                            countmegusta[0] = 0;
                            megusta.setTextColor(Color.parseColor("#FFF0F1F4"));
                            megusta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_thumb_up_white_24dp, 0, 0, 0);

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



        final int[] countnomegusta = {0};
        try {
            DatabaseReference refrerencia = FirebaseDatabase.getInstance().getReference("muro_relacion_dislike");
            Query q = refrerencia.orderByChild("id_user").equalTo(currentUserID);

            q.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> relacion = dataSnapshot.getChildren();
                    int count = 0;
                    String relacion_user, relacion_pub;

                    for (DataSnapshot numrelacion : relacion) {
                        relacion_user = numrelacion.child("id_user").getValue().toString();
                        relacion_pub = numrelacion.child("id_publicacion").getValue().toString();

                        if (relacion_pub.equals(keyget) && relacion_user.equals(currentUserID)) {
                            countnomegusta[0] = 1;
                            nomegusta.setTextColor(Color.parseColor("#00B4AA"));
                            nomegusta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_thumb_down_ligtgreen_24dp, 0, 0, 0);
                        } else{
                            countnomegusta[0] = 0;
                            nomegusta.setTextColor(Color.parseColor("#FFF0F1F4"));
                            nomegusta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_thumb_down_white_24dp, 0, 0, 0);


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

        final String[] cach1 = {""};
        megusta.setOnClickListener(new View.OnClickListener() {
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

                                    if (relacion_pub.equals(keyget) && relacion_user.equals(currentUserID)) {
                                        key = numrelacion.child("key").getValue().toString();
                                        mDatabase.child("muro_relacion_dislike").child(key).removeValue();


                                        countnomegusta[0] = 0;
                                        info_dislike = info_dislike - 1;
                                        mDatabase.child("muro_publicaciones").child(keyget).child("dislike_publicacion").setValue(info_dislike);

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



                    info_like = info_like + 1;

                    mDatabase.child("muro_publicaciones").child(keyget).child("like_publicacion").setValue(info_like);
                    ObjetoRelacion insert;
                    insert = new ObjetoRelacion(keyget, currentUserID, id);
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
                                    keycard = keyget;
                                    if (relacion_pub.equals(keyget) && relacion_user.equals(currentUserID)) {
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


                    info_like = info_like - 1;

                    mDatabase.child("muro_publicaciones").child(keyget).child("like_publicacion").setValue(info_like);


                }


            }
        });





        final String[] cach = {""};
        nomegusta.setOnClickListener(new View.OnClickListener() {
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

                                    if (relacion_pub.equals(keyget) && relacion_user.equals(currentUserID)) {
                                        key = numrelacion.child("key").getValue().toString();
                                        mDatabase.child("muro_relacion_like").child(key).removeValue();

                                        info_like = info_like -1;
                                        mDatabase.child("muro_publicaciones").child(keyget).child("like_publicacion").setValue(info_like);
                                        countmegusta[0] = 0;


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


                    countnomegusta[0] = 1;

                    info_dislike = info_dislike + 1;

                    mDatabase.child("muro_publicaciones").child(keyget).child("dislike_publicacion").setValue(info_dislike);
                    ObjetoRelacion insert;
                    insert = new ObjetoRelacion(keyget, currentUserID, id);
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
                                keycard = keyget;


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

                    info_dislike= info_dislike - 1;


                    mDatabase.child("muro_publicaciones").child(keyget).child("dislike_publicacion").setValue(info_dislike);

                }

                System.out.println("numero == " + countnomegusta[0]);


            }
        });




        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(x[0] == 0){
                    x[0] = 1;
                    contenidox.setVisibility(View.GONE);
                    lista.setVisibility(View.VISIBLE);


                    up.startAnimation(slide_up);
                    up.setVisibility(View.GONE);

                    down.startAnimation(slide_down);
                    down.setVisibility(View.GONE);


                    countx=1;

                    if (count[0] == 0 )
                    {
                    detallesx.setVisibility(View.VISIBLE);

                    }



                }else{


                    up.setVisibility(View.VISIBLE);
                    down.setVisibility(View.VISIBLE);
                    up.startAnimation(slide_downin);
                    down.startAnimation(slide_upin);


                    detallesx.setVisibility(View.GONE);
                    countx=0;
                    x[0] = 0;
                    contenidox.setVisibility(View.VISIBLE);
                    lista.setVisibility(View.GONE);

                }


             /*
                layoutmore.setVisibility(View.VISIBLE);
                layoutmore.startAnimation(slide_upin);
               */




            }
        });









        if(!codeyou.equals("")){
            player_you.setVisibility(View.VISIBLE);
            contenidox.setVisibility(View.GONE);
            player_you.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    down.setVisibility(View.VISIBLE);
                    up.setVisibility(View.VISIBLE);

                    return false;
                }
            });




            lista.setVisibility(View.GONE);


            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(x[0] == 0){
                        x[0] = 1;

                        lista.setVisibility(View.VISIBLE);
                        countx=1;
                        player_you.setVisibility(View.GONE);
                        if (count[0] == 0 )
                        {
                            detallesx.setVisibility(View.VISIBLE);

                        }



                    }else{

                        player_you.setVisibility(View.VISIBLE);
                        detallesx.setVisibility(View.GONE);
                        countx=0;
                        x[0] = 0;

                        lista.setVisibility(View.GONE);

                    }


             /*
                layoutmore.setVisibility(View.VISIBLE);
                layoutmore.startAnimation(slide_upin);
               */




                }
            });





        }









    }

    public void onViewTap(View view, float x, float y) {
        // your code here
        if (countx == 0) {
            up.setVisibility(View.VISIBLE);
            down.setVisibility(View.VISIBLE);
            up.startAnimation(slide_downin);
            down.startAnimation(slide_upin);
            countx = 1;
        } else {
            up.startAnimation(slide_up);
            up.setVisibility(View.GONE);
            down.startAnimation(slide_down);
            down.setVisibility(View.GONE);
            countx = 0;

        }


    }

    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(100);
        getWindow().setEnterTransition(fade);


    }

    public void bannerInfo(int info_like, int info_dislike, int info_comentarios, int info_compartido) {

        if (info_like == 0 && info_dislike == 0 && info_comentarios == 0) {
            info.setText("");
            if (info_compartido == 1) {
                info.setText(info_compartido + " vez compartido");

            } else if (info_compartido > 1) {
                info.setText(info_compartido + " veces compartido");
            }

        } else if (info_like > 0 && info_dislike == 0 && info_comentarios == 0) {
            info.setText(info_like + " me gusta  ");

            if (info_compartido == 1) {
                info.setText(info_like + " me gusta  • " + info_compartido + " vez compartido");

            } else if (info_compartido > 1) {
                info.setText(info_like + " me gusta  • " + info_compartido + " veces compartido");
            }


        } else if (info_like == 0 && info_dislike > 0 && info_comentarios == 0) {
            info.setText(+info_dislike + " no me gusta");

            if (info_compartido == 1) {
                info.setText(+info_dislike + " no me gusta • " + info_compartido + " vez compartido");

            } else if (info_compartido > 1) {
                info.setText(+info_dislike + " no me gusta • " + info_compartido + " veces compartido");
            }

        } else if (info_like > 0 && info_dislike > 0 && info_comentarios == 0) {
            info.setText(info_like + " me gusta • " + info_dislike + " no me gusta  ");

            if (info_compartido == 1) {
                info.setText(info_like + " me gusta • " + info_dislike + " no me gusta • " + info_compartido + " vez compartido");

            } else if (info_compartido > 1) {
                info.setText(info_like + " me gusta • " + info_dislike + " no me gusta • " + info_compartido + " veces compartido");
            }

        } else if (info_like == 0 && info_dislike == 0 && info_comentarios > 0) {

            if (info_comentarios == 1) {
                info.setText(+info_comentarios + " comentario");

                if (info_compartido == 1) {
                    info.setText(+info_comentarios + " comentario • " + info_compartido + " vez compartido");

                } else if (info_compartido > 1) {
                    info.setText(+info_comentarios + " comentario • " + info_compartido + " veces compartido");
                }

            } else if (info_comentarios > 1) {
                info.setText(+info_comentarios + " comentarios");
                if (info_compartido == 1) {
                    info.setText(+info_comentarios + " comentarios • " + info_compartido + " vez compartido");

                } else if (info_compartido > 1) {
                    info.setText(+info_comentarios + " comentarios • " + info_compartido + " veces compartido");
                }

            }
        } else if (info_like > 0 && info_dislike == 0 && info_comentarios > 0) {


            if (info_comentarios == 1) {
                info.setText(info_like + " me gusta • " + info_comentarios + " comentario ");
                if (info_compartido == 1) {
                    info.setText(info_like + " me gusta • " + info_comentarios + " comentario • " + info_compartido + " vez compartido");

                } else if (info_compartido > 1) {
                    info.setText(info_like + " me gusta • " + info_comentarios + " comentario • " + info_compartido + " veces compartido");
                }

            } else if (info_comentarios > 1) {
                info.setText(info_like + " me gusta • " + info_comentarios + " comentarios ");

                if (info_compartido == 1) {
                    info.setText(info_like + " me gusta • " + info_comentarios + " comentarios • " + info_compartido + " vez compartido");

                } else if (info_compartido > 1) {
                    info.setText(info_like + " me gusta • " + info_comentarios + " comentarios • " + info_compartido + " veces compartido");
                }
            }
        } else if (info_like == 0 && info_dislike > 0 && info_comentarios > 0) {


            if (info_comentarios == 1) {
                info.setText(+info_dislike + " no me gusta • " + info_comentarios + " comentario ");


                if (info_compartido == 1) {
                    info.setText(+info_dislike + " no me gusta • " + info_comentarios + " comentario • " + info_compartido + " vez compartido");

                } else if (info_compartido > 1) {
                    info.setText(+info_dislike + " no me gusta • " + info_comentarios + " comentario • " + info_compartido + " veces compartido");
                }

            } else if (info_comentarios > 1) {
                info.setText(+info_dislike + " no me gusta • " + info_comentarios + " comentarios ");

                if (info_compartido == 1) {
                    info.setText(+info_dislike + " no me gusta • " + info_comentarios + " comentarios • " + info_compartido + " vez compartido");

                } else if (info_compartido > 1) {
                    info.setText(+info_dislike + " no me gusta • " + info_comentarios + " comentarios • " + info_compartido + " veces compartido");
                }
            }
        } else if (info_like > 0 && info_dislike > 0 && info_comentarios > 0) {

            if (info_comentarios == 1) {
                info.setText(info_like + " me gusta • " + info_dislike + " no me gusta • " + info_comentarios + " comentario ");

                if (info_compartido == 1) {
                    info.setText(info_like + " me gusta • " + info_dislike + " no me gusta • " + info_comentarios + " comentario • " + info_compartido + " vez compartido");

                } else if (info_compartido > 1) {
                    info.setText(info_like + " me gusta • " + info_dislike + " no me gusta • " + info_comentarios + " comentario • " + info_compartido + " veces compartido");
                }
            } else if (info_comentarios > 1) {
                info.setText(info_like + " me gusta • " + info_dislike + " no me gusta • " + info_comentarios + " comentarios ");

                if (info_compartido == 1) {
                    info.setText(info_like + " me gusta • " + info_dislike + " no me gusta • " + info_comentarios + " comentarios • " + info_compartido + " vez compartido");

                } else if (info_compartido > 1) {
                    info.setText(info_like + " me gusta • " + info_dislike + " no me gusta • " + info_comentarios + " comentarios • " + info_compartido + " veces compartido");
                }
            }

            info.setTextSize(10f);


        }


    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if(!b){
            youTubePlayer.cueVideo(codeyou);

        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }


    @Override
    public void onPlaying() {

    }

    @Override
    public void onPaused() {

    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onBuffering(boolean b) {

    }

    @Override
    public void onSeekTo(int i) {

    }
}