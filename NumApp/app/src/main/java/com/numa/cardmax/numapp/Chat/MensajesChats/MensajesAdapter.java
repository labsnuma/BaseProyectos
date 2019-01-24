package com.numa.cardmax.numapp.Chat.MensajesChats;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
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
import com.numa.cardmax.numapp.Chat.Recursos.DAudios;
import com.numa.cardmax.numapp.Muro.BusquedaMuro;
import com.numa.cardmax.numapp.Muro.MoreActivity;
import com.numa.cardmax.numapp.Muro.Objetos.ObjetoUser;
import com.numa.cardmax.numapp.Muro.ShareActivity;
import com.numa.cardmax.numapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import org.json.JSONObject;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;

public class MensajesAdapter extends RecyclerView.Adapter<MensajesAdapter.MensajesViewHolder>{

    private List<Messages> userMessagesList;
    //  private FirebaseAuth mAuth;
    //
    public MediaPlayer mediaPlayer, mediaPlayerDescarga;
    private Context context;
    private int mediaFileLenght;
    private int realtimeLength;
    final Handler handler = new Handler();
    //hasta ca sonido
    private ProgressDialog prgDialog;
    private String ImagenReciver, ImagenSender;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public String currentUserID = mAuth.getCurrentUser().getUid();


    /*   public MensajesAdapter(List<Messages> userMessagesList) {
           this.userMessagesList = userMessagesList;
       }*/
    public MensajesAdapter(Context context, List<Messages> userMessagesList, String idimagenR, String idimagenS) {
        this.userMessagesList = userMessagesList;
        this.context = context;
        this.ImagenReciver = idimagenR;
        this.ImagenSender = idimagenS;
    }

    DAudios dAudios;


    public class MensajesViewHolder extends RecyclerView.ViewHolder {

        private TextView senderMensajeText, reciverMensajeText;
        private TextView senderhora, reciverhora;
        // private RelativeLayout principal;
        private ConstraintLayout salidalinearlayout, entradalinearlayout;
        private ConstraintLayout salidaaudiolinearlayout, entradaaudiolinearlayout;
        private ConstraintLayout salidaImagenLinerLayout, entradaImagenLinerLayout;
        private ConstraintLayout salidaurlLinerLayout, entradaurlLinerLayout;
        private ConstraintLayout salidayoutubeLinearLayout, entradayoutubeLinerLayout;
        //  private Button salidaaudio, entradaaudio;
        private TextView senderhoraaudio, reciverhoraaudio;
        //
        private FloatingActionButton reproducir_sender, reproducir_pause;
        private FloatingActionButton reproducir_receiver, reproducir_pause_r;
        private SeekBar ver_seekBar, ver_seekbar2;
        //
        private ImageView imagenSender, imagenReciver;
        private CircleImageView imagenSenderAudio, imagenReciverAudio;
        private TextView senderhoraimagen, reciverhoraimagen;

        private TextView urlsender, urlreceiver;
        private TextView urlhorasender, urlhorareceiver;
        private View answer_layout_salidalinear;

        private WebView myWebViewSender, myWebViewReceiver;
        private TextView youthorasender, youthorareceiver;
        private View salida_publicacion, entrada_publicacion;
        private ImageView img_foto_user2, img_foto_userin;
        private Button publicacion, publicacionin, btn_shared_publicacion, btn_answer_publicacion, btn_shared_publicacionin, btn_answer_publicacionin;
        private Button btn_shared_message, btn_answer_message, btn_shared_audio, btn_answer_audio, btn_shared_imagen, btn_answer_imagen;
        private Button btn_shared_entradamessage, btn_answer_entradamessage, btn_shared_audioentrada, btn_answer_audioentrada, btn_shared_imagenentrada, btn_answer_imagenentrada;

        private TextView txt_userpubin, txt_cargopubin, txt_fechapubin, txt_titulopubin, txt_horapubin;
        private TextView txt_userpub, txt_cargopub, txt_fechapub, txt_titulopub, txt_horapub;

        private TextView color_answer, answer_user, answer_messsage;
        private TextView color_answer_salida_pub, answer_user_salida_pub, answer_messsage_salida_pub;
        private TextView color_answer_entrada_imagen, answer_user_entrada_imagen, answer_messsage_entrada_imagen;
        private TextView color_answer_salida_imagen, answer_user_salida_imagen, answer_messsage_salida_imagen;
        private TextView color_answer_audio_salida, answer_user_audio_salida, answer_messsage_audio_salida;
        private TextView color_answer_audio_entrada, answer_user_audio_entrada, answer_messsage_audio_entrada;

        private View visualizacion_publicacionsalida, visualizacion_entrada_imagen, visualizacion_salida_imagen,visualizacion_audio_salida,visualizacion_audio_entrada;

        private ImageView previous_image, previous_image_salida_pub, previous_image_entrada_imagen, previous_image_salida_imagen, previous_image_audio_salida, previous_image_audio_entrada;
        private YouTubeThumbnailView youtubePlayerView, youtubePlayerView_salida ;
        private Button btn_youtubeplayer, btn_youtubeplayer_salida;
        private View view_youtube, view_youtube_salida;

        private MensajesViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMensajeText = (TextView) itemView.findViewById(R.id.sender_message_text);
            reciverMensajeText = (TextView) itemView.findViewById(R.id.reciver_message_text);
            // principal = (RelativeLayout) itemView.findViewById(R.id.principal_view);

            salidalinearlayout = (ConstraintLayout) itemView.findViewById(R.id.salida_linear_layout);
            entradalinearlayout = (ConstraintLayout) itemView.findViewById(R.id.entrada_linear_layout);
            salidaaudiolinearlayout = (ConstraintLayout) itemView.findViewById(R.id.salidaaudio_linear_layout);
            entradaaudiolinearlayout = (ConstraintLayout) itemView.findViewById(R.id.entradaaudio_linear_layout);
            salidaImagenLinerLayout = (ConstraintLayout) itemView.findViewById(R.id.salida_imagen_linear_layout);
            entradaImagenLinerLayout = (ConstraintLayout) itemView.findViewById(R.id.entrada_imagen_linear_layout);
            salidaurlLinerLayout = (ConstraintLayout) itemView.findViewById(R.id.salida_url_linear_layout);
            entradaurlLinerLayout = (ConstraintLayout) itemView.findViewById(R.id.entrada_url_linear_layout);
            salidayoutubeLinearLayout = (ConstraintLayout) itemView.findViewById(R.id.salida_youtube_linear_layout);
            entradayoutubeLinerLayout = (ConstraintLayout) itemView.findViewById(R.id.entrada_youtube_linear_layout);
            salida_publicacion = (View) itemView.findViewById(R.id.salida_publicacion);
            img_foto_user2 = (ImageView) itemView.findViewById(R.id.img_foto_user2);
            senderhora = (TextView) itemView.findViewById(R.id.sender_hora_text);
            reciverhora = (TextView) itemView.findViewById(R.id.reciver_hora_text);
            /*     salidaaudio = (Button) itemView.findViewById(R.id.sender_message_audio);*/
            // entradaaudio = (Button) itemView.findViewById(R.id.reciver_message_audio);
            senderhoraaudio = (TextView) itemView.findViewById(R.id.sender_hora_audio);
            reciverhoraaudio = (TextView) itemView.findViewById(R.id.receiver_hora_audio);
            publicacion = (Button) itemView.findViewById(R.id.btn_publicacion);
            publicacionin = (Button) itemView.findViewById(R.id.btn_publicacionin);
            imagenSender = (ImageView) itemView.findViewById(R.id.sender_imagen);
            imagenReciver = (ImageView) itemView.findViewById(R.id.reciver_imagen);

            senderhoraimagen = (TextView) itemView.findViewById(R.id.sender_hora_imagen);
            reciverhoraimagen = (TextView) itemView.findViewById(R.id.reciver_hora_imagen);
            btn_youtubeplayer = (Button)itemView.findViewById(R.id.btn_youtubeplayer);
            btn_youtubeplayer_salida = (Button)itemView.findViewById(R.id.btn_youtubeplayer2);


            reproducir_sender = (FloatingActionButton) itemView.findViewById(R.id.sender_message_audio1);
            reproducir_pause = (FloatingActionButton) itemView.findViewById(R.id.sender_message_audio2);

            reproducir_receiver = (FloatingActionButton) itemView.findViewById(R.id.receiver_message_audio1);
            reproducir_pause_r = (FloatingActionButton) itemView.findViewById(R.id.receiver_message_audio2);

            imagenSenderAudio = (CircleImageView) itemView.findViewById(R.id.audio_profile_image_send);
            imagenReciverAudio = (CircleImageView) itemView.findViewById(R.id.audio_profile_image_receiver);

            view_youtube=(View)itemView.findViewById(R.id.view_youtube_entrada) ;
            view_youtube_salida=(View)itemView.findViewById(R.id.view_youtube_salida) ;


            ver_seekBar = (SeekBar) itemView.findViewById(R.id.seekbar_sender);
            ver_seekbar2 = (SeekBar) itemView.findViewById(R.id.seekbar_receiver);
            ver_seekBar.setMax(99);
            ver_seekbar2.setMax(99);
            dAudios = new DAudios(context);
            answer_layout_salidalinear = (View) itemView.findViewById(R.id.visualizacion_linear_layout);
            urlsender = (TextView) itemView.findViewById(R.id.sender_message_url_ir);
            urlhorasender = (TextView) itemView.findViewById(R.id.sender_hora_url);

            urlreceiver = (TextView) itemView.findViewById(R.id.receiver_message_url_ir);
            urlhorareceiver = (TextView) itemView.findViewById(R.id.receiver_hora_url);

            txt_userpub = (TextView) itemView.findViewById(R.id.txt_user_chat);
            txt_cargopub = (TextView) itemView.findViewById(R.id.txt_cargo_chat);
            txt_fechapub = (TextView) itemView.findViewById(R.id.txt_fecha_chat);
            txt_titulopub = (TextView) itemView.findViewById(R.id.txt_titulo_chat);
            txt_horapub = (TextView) itemView.findViewById(R.id.sender_hora_publicacion);
            txt_userpubin = (TextView) itemView.findViewById(R.id.txt_user_chatin);
            txt_cargopubin = (TextView) itemView.findViewById(R.id.txt_cargo_chatin);
            txt_fechapubin = (TextView) itemView.findViewById(R.id.txt_fecha_chatin);
            txt_titulopubin = (TextView) itemView.findViewById(R.id.txt_titulo_chatin);
            txt_horapubin = (TextView) itemView.findViewById(R.id.sender_hora_publicacionin);
            img_foto_userin = (ImageView) itemView.findViewById(R.id.img_foto_userin);
            entrada_publicacion = (View) itemView.findViewById(R.id.entrada_publicacion);
            btn_shared_publicacion = (Button) itemView.findViewById(R.id.shared_in_publicacion);
            btn_answer_publicacion = (Button) itemView.findViewById(R.id.answer_in_publicacion);
            btn_shared_publicacionin = (Button) itemView.findViewById(R.id.shared_publicacion);
            btn_answer_publicacionin = (Button) itemView.findViewById(R.id.answer_publicacion);
            btn_shared_message = (Button) itemView.findViewById(R.id.shared_in_menssage);
            btn_answer_message = (Button) itemView.findViewById(R.id.answer_in_menssage);
            btn_shared_audio = (Button) itemView.findViewById(R.id.shared_audio);
            btn_answer_audio = (Button) itemView.findViewById(R.id.answer_audio);
            btn_shared_imagen = (Button) itemView.findViewById(R.id.shared_imagen);
            btn_answer_imagen = (Button) itemView.findViewById(R.id.answer_imagen);
            btn_shared_imagenentrada = (Button) itemView.findViewById(R.id.shared_imageentrada);
            btn_answer_imagenentrada = (Button) itemView.findViewById(R.id.answer_imagenentrada);
            btn_shared_entradamessage = (Button) itemView.findViewById(R.id.shared_entradamenssage);
            btn_answer_entradamessage = (Button) itemView.findViewById(R.id.answer_entradamenssage);
            btn_shared_audioentrada = (Button) itemView.findViewById(R.id.shared_audioentrada);
            btn_answer_audioentrada = (Button) itemView.findViewById(R.id.answer_audioentrada);
            previous_image = (ImageView) itemView.findViewById(R.id.previous_image);
            color_answer = (TextView) itemView.findViewById(R.id.color_answer);
            answer_user = (TextView) itemView.findViewById(R.id.answer_user_message);
            answer_messsage = (TextView) itemView.findViewById(R.id.answer_messsage);
            color_answer_salida_pub = (TextView) itemView.findViewById(R.id.color_answer_publicacionsalida);
            answer_user_salida_pub = (TextView) itemView.findViewById(R.id.answer_user_publicacionsalida);
            answer_messsage_salida_pub = (TextView) itemView.findViewById(R.id.answer_publicacionsalida);
            visualizacion_publicacionsalida = (View)itemView.findViewById(R.id.visualizacion_publicacionsalida);
            previous_image_salida_pub=(ImageView)itemView.findViewById(R.id.previous_image_publicacionsalida);

            color_answer_entrada_imagen = (TextView) itemView.findViewById(R.id.color_answer_entrada_imagen);
            answer_user_entrada_imagen =(TextView) itemView.findViewById(R.id.answer_user_entrada_imagen);
            answer_messsage_entrada_imagen =(TextView) itemView.findViewById(R.id.answer_messsage_entrada_imagen);
            visualizacion_entrada_imagen =(View) itemView.findViewById(R.id.visualizacion_entrada_imagen);
            previous_image_entrada_imagen = (ImageView) itemView.findViewById(R.id.previous_image_entrada_imagen);

            color_answer_salida_imagen = (TextView) itemView.findViewById(R.id.color_answer_salida_imagen);
            answer_user_salida_imagen =(TextView) itemView.findViewById(R.id.answer_user_salida_imagen);
            answer_messsage_salida_imagen =(TextView) itemView.findViewById(R.id.answer_messsage_salida_imagen);
            visualizacion_salida_imagen =(View) itemView.findViewById(R.id.visualizacion_salida_imagen);
            previous_image_salida_imagen = (ImageView) itemView.findViewById(R.id.previous_image_salida_imagen);


            color_answer_audio_salida = (TextView) itemView.findViewById(R.id.color_answer_audio_salida);
            answer_user_audio_salida =(TextView) itemView.findViewById(R.id.answer_user_audio_salida);
            answer_messsage_audio_salida =(TextView) itemView.findViewById(R.id.answer_audio_salida);
            visualizacion_audio_salida =(View) itemView.findViewById(R.id.visualizacion_audio_salida);
            previous_image_audio_salida = (ImageView) itemView.findViewById(R.id.previous_image_audio_salida);

            color_answer_audio_entrada = (TextView) itemView.findViewById(R.id.color_answer_audio_entrada);
            answer_user_audio_entrada =(TextView) itemView.findViewById(R.id.answer_user_audio_entrada);
            answer_messsage_audio_entrada =(TextView) itemView.findViewById(R.id.answer_audio_entrada);
            visualizacion_audio_entrada =(View) itemView.findViewById(R.id.visualizacion_audio_entrada);
            previous_image_audio_entrada = (ImageView) itemView.findViewById(R.id.previous_image_audio_entrada);

            youtubePlayerView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_previus);
            youtubePlayerView_salida = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_previus_salida);

            //youtube david
            myWebViewSender = (WebView) itemView.findViewById(R.id.mWebView1);
            myWebViewReceiver = (WebView) itemView.findViewById(R.id.mWebView2);
            youthorasender = (TextView) itemView.findViewById(R.id.sender_hora_youtube);
            youthorareceiver = (TextView) itemView.findViewById(R.id.receiver_hora_youtube);
        }
    }


    @NonNull
    @Override
    public MensajesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_activity_v_card, parent, false);
        //  mAuth = FirebaseAuth.getInstance();

        return new MensajesViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final MensajesViewHolder messagesViewHolder, final int position) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final String mensajeEnviadoID = mAuth.getCurrentUser().getUid();
        final Messages messages = userMessagesList.get(position);
        final String key_youtube="AIzaSyB8MJf557XN9aDBtwFsD7rhhRMC77yBbeM";
        final String fromUserID = messages.getFrom();
        String fromMensajeTipo = messages.getTipo();

        messagesViewHolder.salidalinearlayout.setVisibility(View.GONE);
        messagesViewHolder.entradalinearlayout.setVisibility(View.GONE);
        messagesViewHolder.salidaaudiolinearlayout.setVisibility(View.GONE);
        messagesViewHolder.entradaaudiolinearlayout.setVisibility(View.GONE);
        messagesViewHolder.salidaImagenLinerLayout.setVisibility(View.GONE);
        messagesViewHolder.entradaImagenLinerLayout.setVisibility(View.GONE);
        messagesViewHolder.salidaurlLinerLayout.setVisibility(View.GONE);
        messagesViewHolder.entradaurlLinerLayout.setVisibility(View.GONE);
        messagesViewHolder.salidayoutubeLinearLayout.setVisibility(View.GONE);
        messagesViewHolder.entradaurlLinerLayout.setVisibility(View.GONE);
        messagesViewHolder.entrada_publicacion.setVisibility(View.GONE);
        messagesViewHolder.salida_publicacion.setVisibility(View.GONE);
        messagesViewHolder.btn_shared_publicacion.setVisibility(View.GONE);
        messagesViewHolder.btn_answer_publicacion.setVisibility(View.GONE);
        messagesViewHolder.btn_shared_publicacionin.setVisibility(View.GONE);
        messagesViewHolder.btn_answer_publicacionin.setVisibility(View.GONE);
        messagesViewHolder.btn_shared_message.setVisibility(View.GONE);
        messagesViewHolder.btn_answer_message.setVisibility(View.GONE);
        messagesViewHolder.btn_shared_audio.setVisibility(View.GONE);
        messagesViewHolder.btn_answer_audio.setVisibility(View.GONE);
        messagesViewHolder.btn_shared_imagen.setVisibility(View.GONE);
        messagesViewHolder.btn_answer_imagen.setVisibility(View.GONE);
        messagesViewHolder.btn_shared_imagenentrada.setVisibility(View.GONE);
        messagesViewHolder.btn_answer_imagenentrada.setVisibility(View.GONE);
        messagesViewHolder.btn_shared_entradamessage.setVisibility(View.GONE);
        messagesViewHolder.btn_answer_entradamessage.setVisibility(View.GONE);
        messagesViewHolder.btn_shared_audioentrada.setVisibility(View.GONE);
        messagesViewHolder.btn_answer_audioentrada.setVisibility(View.GONE);


        if (messages.getVisto().equals("0") && !messages.getFrom().equals(currentUserID)) {
            mDatabase.child("Chat Mensajes").child(currentUserID).child(messages.getFrom()).child(messages.getKey()).child("visto").setValue("1");

        }


        if (fromMensajeTipo.equals("publicacion")) {



            messagesViewHolder.salida_publicacion.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Get instance of Vibrator from current Context
                    Vibrator test = (Vibrator) messagesViewHolder.salida_publicacion.getContext().getSystemService(Context.VIBRATOR_SERVICE);

                    // Vibrate for 400 milliseconds
                    test.vibrate(50);
                    messagesViewHolder.btn_shared_publicacionin.setVisibility(View.VISIBLE);
                    messagesViewHolder.btn_answer_publicacionin.setVisibility(View.VISIBLE);


                    messagesViewHolder.btn_answer_publicacionin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(messagesViewHolder.btn_shared_publicacion.getContext(), "hoola", Toast.LENGTH_SHORT).show();
                            messagesViewHolder.btn_shared_publicacionin.setVisibility(View.GONE);
                            messagesViewHolder.btn_answer_publicacionin.setVisibility(View.GONE);

                            ((ChatActivity) messagesViewHolder.btn_shared_publicacionin.getContext()).ocultar(messages.getFrom(), "Publicacion Compartida", "");

                        }
                    });



                    messagesViewHolder.btn_shared_publicacionin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent reenviar;
                            reenviar = new Intent(messagesViewHolder.btn_shared_publicacion.getContext(), ShareActivity.class);
                            String keypulication1 = "Reenviado:publicacion "+messages.getMensaje();
                            reenviar.putExtra("key", keypulication1);
                            messagesViewHolder.btn_shared_publicacion.getContext().startActivity(reenviar);

                        }
                    });


                    return false;
                }
            });


            messagesViewHolder.publicacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intento;
                    intento = new Intent(messagesViewHolder.publicacion.getContext(), BusquedaMuro.class);
                    intento.putExtra("key", messages.getMensaje());
                    messagesViewHolder.publicacion.getContext().startActivity(intento);

                }
            });


            if (messages.getFrom().equals(currentUserID)) {

                messagesViewHolder.salidalinearlayout.setVisibility(View.GONE);
                messagesViewHolder.entradalinearlayout.setVisibility(View.GONE);
                messagesViewHolder.salidaaudiolinearlayout.setVisibility(View.GONE);
                messagesViewHolder.entradaaudiolinearlayout.setVisibility(View.GONE);


                messagesViewHolder.salida_publicacion.setVisibility(View.VISIBLE);
                messagesViewHolder.entrada_publicacion.setVisibility(View.GONE);
                messagesViewHolder.txt_horapub.setText(messages.getHora());
                Query q = mDatabase.child("muro_publicaciones")
                        .orderByChild("key")
                        .equalTo(messages.getMensaje());

                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();


                        for (DataSnapshot murox : muroChildren) {
                            String id, titulo, fecha;
                            titulo = murox.child("titulo_publicacion").getValue().toString();
                            fecha = murox.child("fecha_publicacion").getValue().toString();
                            id = murox.child("id_usuario").getValue().toString();
                            messagesViewHolder.txt_fechapub.setText(fecha);
                            messagesViewHolder.txt_titulopub.setText(titulo);
                            Query datouser = mDatabase.child("Users")
                                    .orderByChild("uid")
                                    .equalTo(id);

                            datouser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    Iterable<DataSnapshot> userx = dataSnapshot.getChildren();

                                    String name, fotoimg, carrera;
                                    for (DataSnapshot muroxx : userx) {

                                        name = muroxx.child("name").getValue().toString();
                                        fotoimg = muroxx.child("image").getValue().toString();
                                        carrera = muroxx.child("carrera").getValue().toString();

                                        messagesViewHolder.txt_userpub.setText(name + "\n・¡Publicacion Interesante!");
                                        messagesViewHolder.txt_cargopub.setText(carrera);

                                        Glide.with(messagesViewHolder.img_foto_user2.getContext())
                                                .load(fotoimg)
                                                .into(messagesViewHolder.img_foto_user2);


                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            } else {


                messagesViewHolder.entrada_publicacion.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // Get instance of Vibrator from current Context
                        Vibrator test = (Vibrator) messagesViewHolder.publicacionin.getContext().getSystemService(Context.VIBRATOR_SERVICE);

                        // Vibrate for 400 milliseconds
                        test.vibrate(50);
                        messagesViewHolder.btn_shared_publicacion.setVisibility(View.VISIBLE);
                        messagesViewHolder.btn_answer_publicacion.setVisibility(View.VISIBLE);

                        messagesViewHolder.btn_answer_publicacion.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(messagesViewHolder.btn_answer_publicacion.getContext(), "hoola", Toast.LENGTH_SHORT).show();
                                messagesViewHolder.btn_shared_publicacion.setVisibility(View.GONE);
                                messagesViewHolder.btn_answer_publicacion.setVisibility(View.GONE);

                                ((ChatActivity) messagesViewHolder.btn_shared_publicacionin.getContext()).ocultar(messages.getFrom(), "Publicacion Compartida", "");

                            }
                        });


                        messagesViewHolder.btn_shared_publicacion.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent reenviar;
                                reenviar = new Intent(messagesViewHolder.btn_shared_publicacion.getContext(), ShareActivity.class);
                                String keypulication1 = "Reenviado:publicacion "+messages.getMensaje();
                                reenviar.putExtra("key", keypulication1);
                                messagesViewHolder.btn_shared_publicacion.getContext().startActivity(reenviar);

                            }
                        });



                        return false;
                    }
                });

                messagesViewHolder.publicacionin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intento;
                        intento = new Intent(messagesViewHolder.publicacionin.getContext(), BusquedaMuro.class);
                        intento.putExtra("key", messages.getMensaje());
                        messagesViewHolder.publicacion.getContext().startActivity(intento);

                    }
                });


                messagesViewHolder.salidalinearlayout.setVisibility(View.GONE);
                messagesViewHolder.entradalinearlayout.setVisibility(View.GONE);
                messagesViewHolder.salidaaudiolinearlayout.setVisibility(View.GONE);
                messagesViewHolder.entradaaudiolinearlayout.setVisibility(View.GONE);

                            /*
            private ImageView img_foto_user2, img_foto_userin;
            private Button publicacion;
            private TextView txt_userpubin, txt_cargopubin, txt_fechapubin, txt_titulopubin, txt_horapubin;
            private TextView txt_userpub, txt_cargopub, txt_fechapub, txt_titulopub, txt_horapub;
            */

                messagesViewHolder.salida_publicacion.setVisibility(View.GONE);
                messagesViewHolder.entrada_publicacion.setVisibility(View.VISIBLE);

                messagesViewHolder.txt_horapubin.setText(messages.getHora());
                Query q = mDatabase.child("muro_publicaciones")
                        .orderByChild("key")
                        .equalTo(messages.getMensaje());

                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();


                        for (DataSnapshot murox : muroChildren) {
                            String id, titulo, fecha;
                            titulo = murox.child("titulo_publicacion").getValue().toString();
                            fecha = murox.child("fecha_publicacion").getValue().toString();
                            id = murox.child("id_usuario").getValue().toString();
                            messagesViewHolder.txt_fechapubin.setText(fecha);
                            messagesViewHolder.txt_titulopubin.setText(titulo);
                            Query datouser = mDatabase.child("Users")
                                    .orderByChild("uid")
                                    .equalTo(id);

                            datouser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    Iterable<DataSnapshot> userx = dataSnapshot.getChildren();

                                    String name, fotoimg, carrera;
                                    for (DataSnapshot muroxx : userx) {

                                        name = muroxx.child("name").getValue().toString();
                                        fotoimg = muroxx.child("image").getValue().toString();
                                        carrera = muroxx.child("carrera").getValue().toString();

                                        messagesViewHolder.txt_userpubin.setText(name + "\n・¡Publicacion Interesante!");
                                        messagesViewHolder.txt_cargopubin.setText(carrera);

                                        Glide.with(messagesViewHolder.img_foto_userin.getContext())
                                                .load(fotoimg)
                                                .into(messagesViewHolder.img_foto_userin);


                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }





           /*


            messagesViewHolder.senderMensajeText.setText(messages.getMensaje());
            messagesViewHolder.senderhora.setText(messages.getHora());

*/


        } else if (fromMensajeTipo.equals("texto")) {

            Boolean isValid = esUrl(messages.getMensaje());
            Boolean esframe = esFrameYou(messages.getMensaje());

            if (fromUserID.equals(mensajeEnviadoID)) {
                if (esframe) {
                    messagesViewHolder.salidayoutubeLinearLayout.setVisibility(View.VISIBLE);
                    messagesViewHolder.youthorasender.setText(messages.getHora());
                    String frameVideo = messages.getMensaje();
                    WebSettings webSettings = messagesViewHolder.myWebViewSender.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    webSettings.setDomStorageEnabled(true);
                    messagesViewHolder.myWebViewSender.getSettings().setJavaScriptEnabled(true);
                    messagesViewHolder.myWebViewSender.getSettings().setLoadWithOverviewMode(true);
                    messagesViewHolder.myWebViewSender.getSettings().setUseWideViewPort(true);
                    messagesViewHolder.myWebViewSender.loadData(frameVideo, "text/html", "utf-8");
                    messagesViewHolder.myWebViewSender.setWebChromeClient(new WebChromeClient() {


                    });
                } else if (!isValid) {
                    messagesViewHolder.salidalinearlayout.setVisibility(View.VISIBLE);
                    messagesViewHolder.senderMensajeText.setText(messages.getMensaje());
                    messagesViewHolder.senderhora.setText(messages.getHora());
                    messagesViewHolder.senderMensajeText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                        }
                    });

                } else if (isValid) {//es url


                    messagesViewHolder.youtubePlayerView_salida.initialize(key_youtube, new YouTubeThumbnailView.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {


                            if (messages.getMensaje().toString().indexOf("https://www.youtube.com/watch?v=") != -1) {
                                final String code;
                                code = messages.getMensaje().toString().replace("https://www.youtube.com/watch?v=", "");

                                youTubeThumbnailLoader.setVideo(code);
                                messagesViewHolder.btn_youtubeplayer_salida.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((ChatActivity) messagesViewHolder.btn_answer_audio.getContext()).showyoutube(code);
                                    }
                                });
                                messagesViewHolder.btn_youtubeplayer_salida.setVisibility(View.VISIBLE);
                                messagesViewHolder.view_youtube_salida.setVisibility(View.VISIBLE);

                                messagesViewHolder.salidaurlLinerLayout.setVisibility(View.VISIBLE);
                                messagesViewHolder.urlsender.setText(messages.getMensaje());
                                Linkify.addLinks(messagesViewHolder.urlsender, Linkify.WEB_URLS);
                                messagesViewHolder.urlhorasender.setText(messages.getHora());


                            } else if (messages.getMensaje().toString().indexOf("https://www.youtube.com/") != -1) {
                                final String code;
                                code = messages.getMensaje().toString().replace("https://www.youtube.com/", "");
                                youTubeThumbnailLoader.setVideo(code);

                                messagesViewHolder.btn_youtubeplayer_salida.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((ChatActivity) messagesViewHolder.btn_answer_audio.getContext()).showyoutube(code);
                                    }
                                });
                                messagesViewHolder.view_youtube_salida.setVisibility(View.VISIBLE);
                                messagesViewHolder.btn_youtubeplayer_salida.setVisibility(View.VISIBLE);
                                messagesViewHolder.salidaurlLinerLayout.setVisibility(View.VISIBLE);
                                messagesViewHolder.urlsender.setText(messages.getMensaje());
                                Linkify.addLinks(messagesViewHolder.urlsender, Linkify.WEB_URLS);
                                messagesViewHolder.urlhorasender.setText(messages.getHora());


                            } else if (messages.getMensaje().toString().indexOf("https://youtu.be/") != -1) {
                                final String code;
                                code = messages.getMensaje().toString().replace("https://youtu.be/", "");
                                youTubeThumbnailLoader.setVideo(code);

                                messagesViewHolder.btn_youtubeplayer_salida.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((ChatActivity) messagesViewHolder.btn_answer_audio.getContext()).showyoutube(code);
                                    }
                                });

                                messagesViewHolder.view_youtube_salida.setVisibility(View.VISIBLE);
                                messagesViewHolder.btn_youtubeplayer_salida.setVisibility(View.VISIBLE);
                                messagesViewHolder.salidaurlLinerLayout.setVisibility(View.VISIBLE);
                                messagesViewHolder.urlsender.setText(messages.getMensaje());
                                Linkify.addLinks(messagesViewHolder.urlsender, Linkify.WEB_URLS);
                                messagesViewHolder.urlhorasender.setText(messages.getHora());


                            } else {

                                messagesViewHolder.salidaurlLinerLayout.setVisibility(View.VISIBLE);
                                messagesViewHolder.urlsender.setText(messages.getMensaje());
                                Linkify.addLinks(messagesViewHolder.urlsender, Linkify.WEB_URLS);
                                messagesViewHolder.urlhorasender.setText(messages.getHora());

                            }



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

            } else {
                if (esframe) {
                    messagesViewHolder.entradayoutubeLinerLayout.setVisibility(View.VISIBLE);
                    messagesViewHolder.youthorareceiver.setText(messages.getHora());
                    String frameVideo = messages.getMensaje();
                    WebSettings webSettings = messagesViewHolder.myWebViewReceiver.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    webSettings.setDomStorageEnabled(true);
                    messagesViewHolder.myWebViewReceiver.getSettings().setJavaScriptEnabled(true);
                    messagesViewHolder.myWebViewReceiver.getSettings().setLoadWithOverviewMode(true);
                    messagesViewHolder.myWebViewReceiver.getSettings().setUseWideViewPort(true);
                    messagesViewHolder.myWebViewReceiver.loadData(frameVideo, "text/html", "utf-8");
                    messagesViewHolder.myWebViewReceiver.setWebChromeClient(new WebChromeClient() {
                    });
                } else if (!isValid) {
                    messagesViewHolder.entradalinearlayout.setVisibility(View.VISIBLE);
                    messagesViewHolder.reciverMensajeText.setText(messages.getMensaje());
                    messagesViewHolder.reciverhora.setText(messages.getHora());
                } else if (isValid) {//es url



                    messagesViewHolder.youtubePlayerView.initialize(key_youtube, new YouTubeThumbnailView.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {


                            if (messages.getMensaje().toString().indexOf("https://www.youtube.com/watch?v=") != -1) {
                                final String code;
                                code = messages.getMensaje().toString().replace("https://www.youtube.com/watch?v=", "");

                                youTubeThumbnailLoader.setVideo(code);
                                messagesViewHolder.btn_youtubeplayer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((ChatActivity) messagesViewHolder.btn_answer_audio.getContext()).showyoutube(code);
                                    }
                                });
                                messagesViewHolder.btn_youtubeplayer.setVisibility(View.VISIBLE);
                                messagesViewHolder.view_youtube.setVisibility(View.VISIBLE);
                                messagesViewHolder.entradaurlLinerLayout.setVisibility(View.VISIBLE);
                                messagesViewHolder.urlreceiver.setText(messages.getMensaje());
                                Linkify.addLinks(messagesViewHolder.urlreceiver, Linkify.WEB_URLS);
                                messagesViewHolder.urlhorareceiver.setText(messages.getHora());



                            } else if (messages.getMensaje().toString().indexOf("https://www.youtube.com/") != -1) {
                                final String code;
                                code = messages.getMensaje().toString().replace("https://www.youtube.com/", "");
                                youTubeThumbnailLoader.setVideo(code);

                                messagesViewHolder.btn_youtubeplayer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((ChatActivity) messagesViewHolder.btn_answer_audio.getContext()).showyoutube(code);
                                    }
                                });
                                messagesViewHolder.btn_youtubeplayer.setVisibility(View.VISIBLE);
                                messagesViewHolder.view_youtube.setVisibility(View.VISIBLE);
                                messagesViewHolder.entradaurlLinerLayout.setVisibility(View.VISIBLE);
                                messagesViewHolder.urlreceiver.setText(messages.getMensaje());
                                Linkify.addLinks(messagesViewHolder.urlreceiver, Linkify.WEB_URLS);
                                messagesViewHolder.urlhorareceiver.setText(messages.getHora());


                            } else if (messages.getMensaje().toString().indexOf("https://youtu.be/") != -1) {
                                final String code;
                                code = messages.getMensaje().toString().replace("https://youtu.be/", "");
                                youTubeThumbnailLoader.setVideo(code);

                                messagesViewHolder.btn_youtubeplayer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((ChatActivity) messagesViewHolder.btn_answer_audio.getContext()).showyoutube(code);
                                    }
                                });
                                messagesViewHolder.view_youtube.setVisibility(View.VISIBLE);
                                messagesViewHolder.btn_youtubeplayer.setVisibility(View.VISIBLE);
                                messagesViewHolder.entradaurlLinerLayout.setVisibility(View.VISIBLE);
                                messagesViewHolder.urlreceiver.setText(messages.getMensaje());
                                Linkify.addLinks(messagesViewHolder.urlreceiver, Linkify.WEB_URLS);
                                messagesViewHolder.urlhorareceiver.setText(messages.getHora());


                            } else {

                                messagesViewHolder.entradaurlLinerLayout.setVisibility(View.VISIBLE);
                                messagesViewHolder.urlreceiver.setText(messages.getMensaje());
                                Linkify.addLinks(messagesViewHolder.urlreceiver, Linkify.WEB_URLS);
                                messagesViewHolder.urlhorareceiver.setText(messages.getHora());


                            }



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
            }

            messagesViewHolder.senderMensajeText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    messagesViewHolder.btn_shared_message.setVisibility(View.VISIBLE);
                    messagesViewHolder.btn_answer_message.setVisibility(View.VISIBLE);

                    Vibrator test = (Vibrator) messagesViewHolder.senderMensajeText.getContext().getSystemService(Context.VIBRATOR_SERVICE);

                    // Vibrate for 400 milliseconds
                    test.vibrate(50);

                    messagesViewHolder.btn_answer_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            messagesViewHolder.btn_shared_message.setVisibility(View.GONE);
                            messagesViewHolder.btn_answer_message.setVisibility(View.GONE);

                            ((ChatActivity) messagesViewHolder.btn_answer_audio.getContext()).ocultar(messages.getFrom(), messages.getMensaje(), "");
                        }
                    });




                    messagesViewHolder.btn_shared_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent reenviar;
                            reenviar = new Intent(messagesViewHolder.btn_shared_publicacion.getContext(), ShareActivity.class);
                            String keypulication1 = "Reenviado:texto "+messages.getMensaje();
                            reenviar.putExtra("key", keypulication1);
                            messagesViewHolder.btn_shared_publicacion.getContext().startActivity(reenviar);

                        }
                    });








                    return false;
                }
            });


            messagesViewHolder.reciverMensajeText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    messagesViewHolder.btn_shared_entradamessage.setVisibility(View.VISIBLE);
                    messagesViewHolder.btn_answer_entradamessage.setVisibility(View.VISIBLE);

                    Vibrator test = (Vibrator) messagesViewHolder.reciverMensajeText.getContext().getSystemService(Context.VIBRATOR_SERVICE);

                    // Vibrate for 400 milliseconds
                    test.vibrate(50);

                    messagesViewHolder.btn_answer_entradamessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            messagesViewHolder.btn_shared_entradamessage.setVisibility(View.GONE);
                            messagesViewHolder.btn_answer_entradamessage.setVisibility(View.GONE);


                            ((ChatActivity) messagesViewHolder.reciverMensajeText.getContext()).ocultar(messages.getFrom(), messages.getMensaje(), "");
                        }
                    });

                    messagesViewHolder.btn_shared_entradamessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent reenviar;
                            reenviar = new Intent(messagesViewHolder.btn_shared_publicacion.getContext(), ShareActivity.class);
                            String keypulication1 = "Reenviado:texto "+messages.getMensaje();
                            reenviar.putExtra("key", keypulication1);
                            messagesViewHolder.btn_shared_publicacion.getContext().startActivity(reenviar);

                        }
                    });



                    return false;
                }
            });


            if (!messages.getAnswer_user().equals("") && messages.getAnswer_user() != null) {
                answer(messagesViewHolder.answer_layout_salidalinear, messages.getAnswer_user(), messages.getAnswer_mensagge(), messages.getAnswer_multimedia(), messagesViewHolder.answer_user, messagesViewHolder.answer_messsage, messagesViewHolder.color_answer, messagesViewHolder.previous_image);
                answer(messagesViewHolder.visualizacion_publicacionsalida, messages.getAnswer_user(), messages.getAnswer_mensagge(), messages.getAnswer_multimedia(), messagesViewHolder.answer_user_salida_pub, messagesViewHolder.answer_messsage_salida_pub, messagesViewHolder.color_answer_salida_pub, messagesViewHolder.previous_image_salida_pub);
            }







        } else if (fromMensajeTipo.equals("imagen")) {
            if (fromUserID.equals(mensajeEnviadoID)) {
                messagesViewHolder.salidaImagenLinerLayout.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getImagen()).networkPolicy(NetworkPolicy.OFFLINE).
                        into(messagesViewHolder.imagenSender, new Callback() {
                            @Override
                            public void onSuccess() {
                                Picasso.get().load(messages.getImagen()).placeholder(R.drawable.profile_image).into(messagesViewHolder.imagenSender);

                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(messages.getImagen()).placeholder(R.drawable.profile_image).into(messagesViewHolder.imagenSender);
                            }
                        });


                messagesViewHolder.imagenSender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // dAudios.EstaSonando();

                        Intent ChatMensajeIntent = new Intent(context, ImagenMensaje.class);
                        ChatMensajeIntent.putExtra("imagen", messages.getImagen());
                        ChatMensajeIntent.putExtra("opcion", 1);
                        ChatMensajeIntent.putExtra("nombre", "Tú");
                        ChatMensajeIntent.putExtra("fecha", messages.getFecha());
                        ChatMensajeIntent.putExtra("hora", messages.getHora());
                        context.startActivity(ChatMensajeIntent);

                    }
                });

                messagesViewHolder.senderhoraimagen.setText(messages.getHora());
            } else {

                messagesViewHolder.entradaImagenLinerLayout.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getImagen()).placeholder(R.drawable.profile_image).into(messagesViewHolder.imagenSender);
                Picasso.get().load(messages.getImagen()).networkPolicy(NetworkPolicy.OFFLINE).
                        into(messagesViewHolder.imagenReciver, new Callback() {
                            @Override
                            public void onSuccess() {
                                Picasso.get().load(messages.getImagen()).placeholder(R.drawable.profile_image).into(messagesViewHolder.imagenSender);
                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(messages.getImagen()).placeholder(R.drawable.profile_image).into(messagesViewHolder.imagenSender);
                            }
                        });

                messagesViewHolder.imagenReciver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //     Toast.makeText(v.getContext(), "click en la imagen: " + messages.getImagen(), Toast.LENGTH_SHORT).show();

                        Intent ChatMensajeIntent = new Intent(context, ImagenMensaje.class);
                        ChatMensajeIntent.putExtra("imagen", messages.getImagen());
                        ChatMensajeIntent.putExtra("opcion", 2);
                        ChatMensajeIntent.putExtra("nombre", messages.getFrom());
                        ChatMensajeIntent.putExtra("fecha", messages.getFecha());
                        ChatMensajeIntent.putExtra("hora", messages.getHora());
                        context.startActivity(ChatMensajeIntent);

                    }
                });

                messagesViewHolder.reciverhoraimagen.setText(messages.getHora());
            }


            //////////////

            messagesViewHolder.salidaImagenLinerLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Get instance of Vibrator from current Context
                    Vibrator test = (Vibrator) messagesViewHolder.salidaaudiolinearlayout.getContext().getSystemService(Context.VIBRATOR_SERVICE);

                    // Vibrate for 400 milliseconds
                    test.vibrate(50);
                    messagesViewHolder.btn_shared_imagen.setVisibility(View.VISIBLE);
                    messagesViewHolder.btn_answer_imagen.setVisibility(View.VISIBLE);


                    messagesViewHolder.btn_answer_imagen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(messagesViewHolder.btn_answer_audio.getContext(), "hoola", Toast.LENGTH_SHORT).show();

                            ((ChatActivity) messagesViewHolder.btn_answer_audio.getContext()).ocultar(messages.getFrom(), "Imagen", messages.getImagen());
                            messagesViewHolder.btn_shared_imagen.setVisibility(View.GONE);
                            messagesViewHolder.btn_answer_imagen.setVisibility(View.GONE);


                        }
                    });


                    messagesViewHolder.btn_shared_imagen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent reenviar;
                            reenviar = new Intent(messagesViewHolder.btn_shared_publicacion.getContext(), ShareActivity.class);
                            String keypulication1 = "Reenviado:imagen "+messages.getImagen();
                            reenviar.putExtra("key", keypulication1);
                            messagesViewHolder.btn_shared_publicacion.getContext().startActivity(reenviar);

                        }
                    });




                    return false;
                }
            });

            messagesViewHolder.entradaImagenLinerLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Get instance of Vibrator from current Context
                    Vibrator test = (Vibrator) messagesViewHolder.entradaImagenLinerLayout.getContext().getSystemService(Context.VIBRATOR_SERVICE);

                    // Vibrate for 400 milliseconds
                    test.vibrate(50);
                    messagesViewHolder.btn_shared_imagenentrada.setVisibility(View.VISIBLE);
                    messagesViewHolder.btn_answer_imagenentrada.setVisibility(View.VISIBLE);


                    messagesViewHolder.btn_answer_imagenentrada.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(messagesViewHolder.btn_answer_audio.getContext(), "hoola", Toast.LENGTH_SHORT).show();

                            ((ChatActivity) messagesViewHolder.btn_answer_audio.getContext()).ocultar(messages.getFrom(), "Imagen", messages.getImagen());
                            messagesViewHolder.btn_shared_imagenentrada.setVisibility(View.GONE);
                            messagesViewHolder.btn_answer_imagenentrada.setVisibility(View.GONE);


                        }
                    });


                    messagesViewHolder.btn_shared_imagenentrada.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent reenviar;
                            reenviar = new Intent(messagesViewHolder.btn_shared_publicacion.getContext(), ShareActivity.class);
                            String keypulication1 = "Reenviado:imagen "+messages.getImagen();
                            reenviar.putExtra("key", keypulication1);
                            messagesViewHolder.btn_shared_publicacion.getContext().startActivity(reenviar);

                        }
                    });




                    return false;
                }
            });




            if (!messages.getAnswer_user().equals("") && messages.getAnswer_user() != null) {
                try {
                    answer(messagesViewHolder.visualizacion_entrada_imagen, messages.getAnswer_user(), messages.getAnswer_mensagge(), messages.getAnswer_multimedia(), messagesViewHolder.answer_user_entrada_imagen, messagesViewHolder.answer_messsage_entrada_imagen, messagesViewHolder.color_answer_entrada_imagen, messagesViewHolder.previous_image_entrada_imagen);
                    answer(messagesViewHolder.visualizacion_salida_imagen, messages.getAnswer_user(), messages.getAnswer_mensagge(), messages.getAnswer_multimedia(), messagesViewHolder.answer_user_salida_imagen, messagesViewHolder.answer_messsage_salida_imagen, messagesViewHolder.color_answer_salida_imagen, messagesViewHolder.previous_image_salida_imagen);
                }catch (Exception e){

                    System.out.println("errroooooooooorr +"+e);
                }


            }










        }

        if (fromMensajeTipo.equals("audio")) {
            messagesViewHolder.salidalinearlayout.setVisibility(View.GONE);
            messagesViewHolder.entradalinearlayout.setVisibility(View.GONE);
            messagesViewHolder.salidaaudiolinearlayout.setVisibility(View.GONE);
            messagesViewHolder.entradaaudiolinearlayout.setVisibility(View.GONE);

            if (fromUserID.equals(mensajeEnviadoID)) {

                DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
                UserRef.child(messages.getFrom()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String currentUserfoto = dataSnapshot.child("image").getValue().toString();
                            Picasso.get().load(currentUserfoto).placeholder(R.drawable.profile_image).into(messagesViewHolder.imagenSenderAudio);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //  Picasso.get().load(ImagenReciver).placeholder(R.drawable.profile_image).into(messagesViewHolder.imagenSenderAudio);


                messagesViewHolder.salidaaudiolinearlayout.setVisibility(View.VISIBLE);
                messagesViewHolder.senderhoraaudio.setText(messages.getHora());

                messagesViewHolder.reproducir_sender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dAudios.Iniciar(context, messages.getAudio(), messages.getFrom(),
                                messages.getNombre(), messagesViewHolder.reproducir_sender,
                                messagesViewHolder.ver_seekBar, messagesViewHolder.reproducir_pause, "Send/");
                    }
                });

                messagesViewHolder.reproducir_pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dAudios.PausarBoton(messagesViewHolder.reproducir_pause, messagesViewHolder.ver_seekBar);

                    }
                });


                messagesViewHolder.ver_seekBar.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        dAudios.seekbarEstado(v);
                        return false;
                    }
                });


            } else {

                DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
                UserRef.child(messages.getFrom()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String currentUserfoto = dataSnapshot.child("image").getValue().toString();
                            Picasso.get().load(currentUserfoto).placeholder(R.drawable.profile_image).into(messagesViewHolder.imagenReciverAudio);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                messagesViewHolder.entradaaudiolinearlayout.setVisibility(View.VISIBLE);
                messagesViewHolder.reciverhoraaudio.setText(messages.getHora());

                messagesViewHolder.reproducir_receiver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dAudios.Iniciar(context, messages.getAudio(), messages.getFrom(),
                                messages.getNombre(), messagesViewHolder.reproducir_receiver,
                                messagesViewHolder.ver_seekbar2, messagesViewHolder.reproducir_pause_r, "Receiver/");
                    }
                });

                messagesViewHolder.reproducir_pause_r.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dAudios.PausarBoton(messagesViewHolder.reproducir_pause_r, messagesViewHolder.ver_seekbar2);

                    }
                });

                messagesViewHolder.ver_seekbar2.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        dAudios.seekbarEstado(v);
                        return false;
                    }
                });

            }


            /////////////////////////////////////////////////


            messagesViewHolder.salidaaudiolinearlayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Get instance of Vibrator from current Context
                    Vibrator test = (Vibrator) messagesViewHolder.salidaaudiolinearlayout.getContext().getSystemService(Context.VIBRATOR_SERVICE);

                    // Vibrate for 400 milliseconds
                    test.vibrate(50);
                    messagesViewHolder.btn_shared_audio.setVisibility(View.VISIBLE);
                    messagesViewHolder.btn_answer_audio.setVisibility(View.VISIBLE);


                    messagesViewHolder.btn_answer_audio.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(messagesViewHolder.btn_answer_audio.getContext(), "hoola", Toast.LENGTH_SHORT).show();

                            ((ChatActivity) messagesViewHolder.btn_answer_audio.getContext()).ocultar(messages.getFrom(), "Mensaje de voz", "");
                            messagesViewHolder.btn_shared_audio.setVisibility(View.GONE);
                            messagesViewHolder.btn_answer_audio.setVisibility(View.GONE);


                        }
                    });



                    messagesViewHolder.btn_shared_audio.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent reenviar;
                            reenviar = new Intent(messagesViewHolder.btn_shared_publicacion.getContext(), ShareActivity.class);
                            String keypulication1 = "Reenviado:audio "+messages.getAudio();
                            reenviar.putExtra("key", keypulication1);
                            messagesViewHolder.btn_shared_publicacion.getContext().startActivity(reenviar);

                        }
                    });



                    return false;
                }
            });


            messagesViewHolder.entradaaudiolinearlayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Get instance of Vibrator from current Context
                    Vibrator test = (Vibrator) messagesViewHolder.btn_shared_audioentrada.getContext().getSystemService(Context.VIBRATOR_SERVICE);

                    // Vibrate for 400 milliseconds
                    test.vibrate(50);
                    messagesViewHolder.btn_shared_audioentrada.setVisibility(View.VISIBLE);
                    messagesViewHolder.btn_answer_audioentrada.setVisibility(View.VISIBLE);


                    messagesViewHolder.btn_answer_audioentrada.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(messagesViewHolder.btn_answer_audioentrada.getContext(), "hoola", Toast.LENGTH_SHORT).show();

                            ((ChatActivity) messagesViewHolder.btn_answer_audio.getContext()).ocultar(messages.getFrom(), "Mensaje de voz", "");
                            messagesViewHolder.btn_shared_audioentrada.setVisibility(View.GONE);
                            messagesViewHolder.btn_answer_audioentrada.setVisibility(View.GONE);


                        }
                    });


                    messagesViewHolder.btn_shared_audioentrada.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent reenviar;
                            reenviar = new Intent(messagesViewHolder.btn_shared_publicacion.getContext(), ShareActivity.class);
                            String keypulication1 = "Reenviado:audio "+messages.getAudio();
                            reenviar.putExtra("key", keypulication1);
                            messagesViewHolder.btn_shared_publicacion.getContext().startActivity(reenviar);

                        }
                    });





                    return false;
                }
            });




            if (!messages.getAnswer_user().equals("") && messages.getAnswer_user() != null) {
                try {
                    answer(messagesViewHolder.visualizacion_audio_salida, messages.getAnswer_user(), messages.getAnswer_mensagge(), messages.getAnswer_multimedia(), messagesViewHolder.answer_user_audio_salida, messagesViewHolder.answer_messsage_audio_salida, messagesViewHolder.color_answer_audio_salida, messagesViewHolder.previous_image_audio_salida);
                    answer(messagesViewHolder.visualizacion_audio_entrada, messages.getAnswer_user(), messages.getAnswer_mensagge(), messages.getAnswer_multimedia(), messagesViewHolder.answer_user_audio_entrada, messagesViewHolder.answer_messsage_audio_entrada, messagesViewHolder.color_answer_audio_entrada, messagesViewHolder.previous_image_audio_entrada);

                }catch (Exception e){

                    System.out.println("errroooooooooorr +"+e);
                }


            }



        }

    }

    private static boolean esFrameYou(String cadena) {
        return cadena.contains("<iframe width=");
    }

    private static boolean esUrl(String s) {
        String regex = "^(https?://)?(([\\w!~*'().&=+$%-]+: )?[\\w!~*'().&=+$%-]+@)?(([0-9]{1,3}\\.){3}[0-9]{1,3}|([\\w!~*'()-]+\\.)*([\\w^-][\\w-]{0,61})?[\\w]\\.[a-z]{2,6})(:[0-9]{1,4})?((/*)|(/+[\\w!~*'().;?:@&=+$,%#-]+)+/*)$";

        try {
            Pattern patt = Pattern.compile(regex);
            Matcher matcher = patt.matcher(s);
            return matcher.matches();

        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }

    public void onBackPressed() {
        dAudios.PararAudio();
    }


    public void answer(View layout, String user_id, String user_mensaje, String multimedia, final TextView txt_user, TextView txt_mensaje, TextView background_color, ImageView imagen) {

        layout.setVisibility(View.VISIBLE);


        if (currentUserID.equals(user_id)) {

            txt_user.setText("Tú");
            txt_mensaje.setText(user_mensaje);
            txt_user.setTextColor(Color.parseColor("#0e84d8"));
            background_color.setBackgroundColor(Color.parseColor("#0e84d8"));

            if (user_mensaje.equals("Mensaje de voz")) {
                imagen.setVisibility(View.GONE);

                txt_mensaje.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_mic_black_18dp, 0, 0, 0);
            } else if (user_mensaje.equals("Imagen")) {

                txt_mensaje.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_photo_camera_black_18dp, 0, 0, 0);

                Glide.with(txt_mensaje.getContext())
                        .load(multimedia)
                        .into(imagen);
                imagen.setVisibility(View.VISIBLE);

            } else if (user_mensaje.equals("Publicacion Compartida")) {


                imagen.setVisibility(View.GONE);

                txt_mensaje.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_share_black_18dp, 0, 0, 0);


            } else {
                imagen.setVisibility(View.GONE);
                txt_mensaje.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            }


        } else {


            DatabaseReference xDatabase = FirebaseDatabase.getInstance().getReference();

            xDatabase.child("Users").child(user_id).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name;
                            ObjetoUser userx = dataSnapshot.getValue(ObjetoUser.class);
                            name = userx.name; // "John Doe"
                            txt_user.setText(name);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


            txt_mensaje.setText(user_mensaje);
            txt_user.setTextColor(Color.parseColor("#8bd80f"));
            background_color.setBackgroundColor(Color.parseColor("#8bd80f"));


            if (user_mensaje.equals("Mensaje de voz")) {
                imagen.setVisibility(View.GONE);

                txt_mensaje.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_mic_black_18dp, 0, 0, 0);
            } else if (user_mensaje.equals("Imagen")) {

                txt_mensaje.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_photo_camera_black_18dp, 0, 0, 0);

                Glide.with(txt_mensaje.getContext())
                        .load(multimedia)
                        .into(imagen);
                imagen.setVisibility(View.VISIBLE);

            } else if (user_mensaje.equals("Publicacion Compartida")) {


                imagen.setVisibility(View.GONE);

                txt_mensaje.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_share_black_18dp, 0, 0, 0);


            } else {
                imagen.setVisibility(View.GONE);
                txt_mensaje.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            }


        }


    }









}
