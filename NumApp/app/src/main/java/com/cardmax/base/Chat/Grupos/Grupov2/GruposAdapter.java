package com.cardmax.base.Chat.Grupos.Grupov2;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cardmax.base.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.cardmax.base.Chat.MensajesChats.ImagenMensaje;
import com.cardmax.base.Chat.Recursos.DAudios;
import com.cardmax.base.Chat.Recursos.DAudiosGrupos;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class GruposAdapter extends RecyclerView.Adapter<GruposAdapter.MensajesViewHolder> {

    private List<MessagesGrupo> userMessagesList;
    private FirebaseAuth mAuth;
    private Context context;
    // private DatabaseReference UserRef;

    /* public GruposAdapter(List<MessagesGrupo> userMessagesList) {
         this.userMessagesList = userMessagesList;
     }*/
    public GruposAdapter(Context context, List<MessagesGrupo> userMessagesList) {
        this.userMessagesList = userMessagesList;
        this.context = context;
    }

    DAudiosGrupos dAudios;

    public class MensajesViewHolder extends RecyclerView.ViewHolder {

        public TextView senderMensajeText, reciverMensajeText, reciverNombretText;
        public TextView senderhora, reciverhora;
        public CircleImageView reciverProfileImage;
        public RelativeLayout principal;
        public ConstraintLayout salidalinearlayout, entradalinearlayout;
        public ConstraintLayout salidaaudiolinearlayout, entradaaudiolinearlayout;
        public ConstraintLayout salidaImagenLinerLayout, entradaImagenLinerLayout;
        private ConstraintLayout salidaurlLinerLayout, entradaurlLinerLayout;
        private ConstraintLayout salidayoutubeLinearLayout, entradayoutubeLinerLayout;
        //   public ConstraintLayout salidaAudioLinerLayout, entradaAudioLinerLayout;

        //public Button salidaaudio, entradaaudio;
        private CircleImageView imagenSenderAudio, imagenReciverAudio;
        private FloatingActionButton reproducir_sender, reproducir_pause;
        private FloatingActionButton reproducir_receiver, reproducir_pause_r;
        private SeekBar ver_seekBar, ver_seekbar2;
        public TextView nombrereciveraudio;

        public TextView senderhoraaudio, reciverhoraaudio;
        public ImageView imagenSender, imagenReciver;
        public TextView nombresenderimagen, senderhoraimagen, reciverhoraimagen;


        private TextView urlsender, urlreceiver;
        private TextView urlhorasender, urlhorareceiver;
        private TextView urlNombresender, urlNombrereceiver;

        private WebView myWebViewSender, myWebViewReceiver;
        private TextView youthorasender, youthorareceiver;
        private TextView youNombrereceiver;


        public MensajesViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMensajeText = (TextView) itemView.findViewById(R.id.sender_message_text);
            reciverMensajeText = (TextView) itemView.findViewById(R.id.reciver_message_text);
            reciverNombretText = (TextView) itemView.findViewById(R.id.reciver_nombre_text);
            reciverProfileImage = (CircleImageView) itemView.findViewById(R.id.message_profile_image);

            principal = (RelativeLayout) itemView.findViewById(R.id.principal_view);
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


            senderhora = (TextView) itemView.findViewById(R.id.sender_hora_text);
            reciverhora = (TextView) itemView.findViewById(R.id.reciver_hora_text);

            // salidaaudio = (Button) itemView.findViewById(R.id.sender_message_audio);
            //  entradaaudio = (Button) itemView.findViewById(R.id.reciver_message_audio);

            senderhoraaudio = (TextView) itemView.findViewById(R.id.sender_hora_audio);
            reciverhoraaudio = (TextView) itemView.findViewById(R.id.receiver_hora_audio);
            nombrereciveraudio = (TextView) itemView.findViewById(R.id.reciver_nombre_audio);

            imagenSender = (ImageView) itemView.findViewById(R.id.sender_imagen);
            imagenReciver = (ImageView) itemView.findViewById(R.id.reciver_imagen);

            nombresenderimagen = (TextView) itemView.findViewById(R.id.nombre_message_imagen);
            senderhoraimagen = (TextView) itemView.findViewById(R.id.sender_hora_imagen);
            reciverhoraimagen = (TextView) itemView.findViewById(R.id.reciver_hora_imagen);

            reproducir_sender = (FloatingActionButton) itemView.findViewById(R.id.sender_message_audio1);
            reproducir_pause = (FloatingActionButton) itemView.findViewById(R.id.sender_message_audio2);

            reproducir_receiver = (FloatingActionButton) itemView.findViewById(R.id.receiver_message_audio1);
            reproducir_pause_r = (FloatingActionButton) itemView.findViewById(R.id.receiver_message_audio2);

            imagenSenderAudio = (CircleImageView) itemView.findViewById(R.id.audio_profile_image_send);
            imagenReciverAudio = (CircleImageView) itemView.findViewById(R.id.audio_profile_image_receiver);

            ver_seekBar = (SeekBar) itemView.findViewById(R.id.seekbar_sender);
            ver_seekbar2 = (SeekBar) itemView.findViewById(R.id.seekbar_receiver);
            ver_seekBar.setMax(99);
            ver_seekbar2.setMax(99);
            dAudios = new DAudiosGrupos(context);

            urlsender = (TextView) itemView.findViewById(R.id.sender_message_url_ir);
            urlhorasender = (TextView) itemView.findViewById(R.id.sender_hora_url);

            urlreceiver = (TextView) itemView.findViewById(R.id.receiver_message_url_ir);
            urlhorareceiver = (TextView) itemView.findViewById(R.id.receiver_hora_url);
            urlNombresender = (TextView) itemView.findViewById(R.id.reciver_nombre_url);


            //youtube david
            myWebViewSender = (WebView) itemView.findViewById(R.id.mWebView1);
            myWebViewReceiver = (WebView) itemView.findViewById(R.id.mWebView2);
            youthorasender = (TextView) itemView.findViewById(R.id.sender_hora_youtube);
            youthorareceiver = (TextView) itemView.findViewById(R.id.receiver_hora_youtube);
            youNombrereceiver = (TextView) itemView.findViewById(R.id.reciver_nombre_you);

        }
    }


    @NonNull
    @Override
    public MensajesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_activity_v_card_grupo, parent, false);

        mAuth = FirebaseAuth.getInstance();

        return new MensajesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MensajesViewHolder messagesViewHolder, final int position) {

        final String mensajeEnviadoID = mAuth.getCurrentUser().getUid();
        final MessagesGrupo messages = userMessagesList.get(position);

        final String fromUserID = messages.getFrom();
        String fromMensajeTipo = messages.getTipo();

        messagesViewHolder.reciverProfileImage.setVisibility(View.GONE);
        messagesViewHolder.salidalinearlayout.setVisibility(View.GONE);
        messagesViewHolder.entradalinearlayout.setVisibility(View.GONE);
        messagesViewHolder.salidaaudiolinearlayout.setVisibility(View.GONE);
        messagesViewHolder.entradaaudiolinearlayout.setVisibility(View.GONE);
        messagesViewHolder.salidaImagenLinerLayout.setVisibility(View.GONE);
        messagesViewHolder.entradaImagenLinerLayout.setVisibility(View.GONE);
        messagesViewHolder.reciverNombretText.setVisibility(View.GONE);
        messagesViewHolder.salidaurlLinerLayout.setVisibility(View.GONE);
        messagesViewHolder.entradaurlLinerLayout.setVisibility(View.GONE);
        messagesViewHolder.salidayoutubeLinearLayout.setVisibility(View.GONE);
        messagesViewHolder.entradayoutubeLinerLayout.setVisibility(View.GONE);

        if (fromMensajeTipo.equals("texto")) {
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
                            Toast.makeText(v.getContext(), "click en: " + messages.getMensaje(), Toast.LENGTH_SHORT).show();

                        }
                    });
                } else if (isValid) {//es url
                    messagesViewHolder.salidaurlLinerLayout.setVisibility(View.VISIBLE);
                    messagesViewHolder.urlsender.setText(messages.getMensaje());
                    Linkify.addLinks(messagesViewHolder.urlsender, Linkify.WEB_URLS);
                    messagesViewHolder.urlhorasender.setText(messages.getHora());
                }

            } else {
                if (esframe) {
                    messagesViewHolder.entradayoutubeLinerLayout.setVisibility(View.VISIBLE);
                    messagesViewHolder.youthorareceiver.setText(messages.getHora());
                    messagesViewHolder.reciverProfileImage.setVisibility(View.VISIBLE);
                    Picasso.get().load(messages.getFoto()).placeholder(R.drawable.profile_image).into(messagesViewHolder.reciverProfileImage);
                    messagesViewHolder.youNombrereceiver.setVisibility(View.VISIBLE);
                    messagesViewHolder.youNombrereceiver.setText(messages.getNombre());
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

                    Picasso.get().load(messages.getFoto()).placeholder(R.drawable.profile_image).into(messagesViewHolder.reciverProfileImage);
                    messagesViewHolder.entradalinearlayout.setVisibility(View.VISIBLE);
                    messagesViewHolder.reciverProfileImage.setVisibility(View.VISIBLE);
                    messagesViewHolder.reciverNombretText.setVisibility(View.VISIBLE);
                    messagesViewHolder.reciverNombretText.setText(messages.getNombre());
                    messagesViewHolder.reciverMensajeText.setText(messages.getMensaje());
                    messagesViewHolder.reciverhora.setText(messages.getHora());
                } else if (isValid) {//es url
                    messagesViewHolder.reciverProfileImage.setVisibility(View.VISIBLE);
                    Picasso.get().load(messages.getFoto()).placeholder(R.drawable.profile_image).into(messagesViewHolder.reciverProfileImage);
                    messagesViewHolder.urlNombresender.setVisibility(View.VISIBLE);
                    messagesViewHolder.urlNombresender.setText(messages.getNombre());
                    messagesViewHolder.entradaurlLinerLayout.setVisibility(View.VISIBLE);
                    messagesViewHolder.urlreceiver.setText(messages.getMensaje());
                    Linkify.addLinks(messagesViewHolder.urlreceiver, Linkify.WEB_URLS);
                    messagesViewHolder.urlhorareceiver.setText(messages.getHora());
                }
            }

        } else if (fromMensajeTipo.equals("audio")) {

            if (fromUserID.equals(mensajeEnviadoID)) {
                messagesViewHolder.salidaaudiolinearlayout.setVisibility(View.VISIBLE);
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

                messagesViewHolder.senderhoraaudio.setText(messages.getHora());
                messagesViewHolder.reproducir_sender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dAudios.Iniciar(context, messages.getAudio(), messages.getFrom(), messages.getNgrupo(),
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
                messagesViewHolder.nombrereciveraudio.setText(messages.getNombre());

                messagesViewHolder.reproducir_receiver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dAudios.Iniciar(context, messages.getAudio(), messages.getFrom(), messages.getNgrupo(),
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
                messagesViewHolder.reciverProfileImage.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getFoto()).placeholder(R.drawable.profile_image).into(messagesViewHolder.reciverProfileImage);
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

                messagesViewHolder.nombresenderimagen.setText(messages.getNombre());
                messagesViewHolder.reciverhoraimagen.setText(messages.getHora());
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

    public void descargar(String url) {
        MediaPlayer mediaPlayerDescarga = new MediaPlayer();
        try {
            mediaPlayerDescarga.setDataSource(url);
            mediaPlayerDescarga.prepare();
            mediaPlayerDescarga.start();
        } catch (IOException e) {
            Log.e("Errorsplay: ", e.getMessage());
        }

    }


    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }

}
