package com.numa.cardmax.numapp.Chat.MensajesChats;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.numa.cardmax.numapp.Chat.Services.ConfigShared;
import com.numa.cardmax.numapp.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivityPruebaFinal extends AppCompatActivity {
    private Toolbar mTollbar;
   // private ImageButton sendMessageBtutton;
    private EditText userMessageInput;

    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, GroupNameRef, GroupMessageKeyRef, MensajeKeyRef2;
    private String currentGroupName, currentUserID, currentUserName, currentDate, currentTime;

    ////
    private DatabaseReference ChatMensajesRef;
    private String reciverUserID, senderUserID;
    private DatabaseReference ref1, ref2;
    private String nombreRecibido;
    private String visit_user_name;
    /// ultima
    private TextView userProfileName, userProfileSatus,userLasSeen;
    private CircleImageView userProfileImage;
    private Toolbar mToolbar;
    private String visit_user_image;

    private ScrollView mScroolView;
    private TextView dispalyTextMessage;
    ///
    private LinearLayout layout;
    private RelativeLayout layout_2;
    private ScrollView scrollView;
    private FloatingActionButton sendMessageBtutton,audio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_chat_prueba_final);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        ChatMensajesRef = FirebaseDatabase.getInstance().getReference().child("Chat Mensajes");
        reciverUserID = getIntent().getExtras().get("visit_user_id").toString();
        senderUserID = mAuth.getCurrentUser().getUid();

        ref1 = ChatMensajesRef.child(senderUserID).child(reciverUserID);
        ref2 = ChatMensajesRef.child(reciverUserID).child(senderUserID);

        currentGroupName = getIntent().getExtras().get("visit_user_name").toString();
        Toast.makeText(this, currentGroupName, Toast.LENGTH_SHORT).show();
        visit_user_name = getIntent().getExtras().get("visit_user_name").toString();
        visit_user_image = getIntent().getExtras().get("visit_user_image").toString();

        SharedPreferences sharedPreferences = getSharedPreferences(ConfigShared.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        nombreRecibido = sharedPreferences.getString(ConfigShared.SHARED_NAME_CARGADO, "No Disponible");

        initializeFields();
        userProfileName.setText(visit_user_name);
        Picasso.get().load(visit_user_image).placeholder(R.drawable.profile_image).into(userProfileImage);

        sendMessageBtutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMessageInfoToDatabase();
                userMessageInput.setText("");
//                mScroolView.fullScroll(ScrollView.FOCUS_DOWN);
                scrollView = findViewById(R.id.scrollView);


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        ref1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                   // DisplayMessages(dataSnapshot);
                    Enburbuja(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    //  DisplayMessages(dataSnapshot);
                    Enburbuja(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                  //  DisplayMessages(dataSnapshot);
                    Enburbuja(dataSnapshot);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    private void initializeFields() {
        userProfileImage = findViewById(R.id.fotoDePerfilSolicitud);
        userProfileName = findViewById(R.id.nombreamigo);
        userLasSeen= findViewById(R.id.fecha);

        mToolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(mToolbar);

        //sendMessageBtutton = (ImageButton) findViewById(R.id.boton_enviar);
        userMessageInput = findViewById(R.id.input_message);
        sendMessageBtutton = findViewById(R.id.boton_enviar);
        audio= findViewById(R.id.boton_micorfono);

        userMessageInput = findViewById(R.id.input_message);
        userMessageInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                siesvacio();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                siesvacio();
            }

            @Override
            public void afterTextChanged(Editable s) {
                siesvacio();
            }
        });

       /* dispalyTextMessage = (TextView) findViewById(R.id.group_chat_text_dispaly_1);
        mScroolView = (ScrollView) findViewById(R.id.my_scroll_view_1);*/


        scrollView = findViewById(R.id.scrollView);
        layout = findViewById(R.id.layout1);
        layout_2 = findViewById(R.id.layout2);

    }
    private void siesvacio() {
        String mensaje =  userMessageInput.getText().toString().trim();//"   hola  " => "hola"

        if (TextUtils.isEmpty(mensaje)||userMessageInput.equals(null)||userMessageInput.equals("")) {

            sendMessageBtutton.setVisibility(View.INVISIBLE);
            audio.setVisibility(View.VISIBLE);
            audio.setEnabled(true);
            sendMessageBtutton.setEnabled(false);
        }
        else{

            sendMessageBtutton.setVisibility(View.VISIBLE);
            audio.setVisibility(View.INVISIBLE);
            audio.setEnabled(false);
            sendMessageBtutton.setEnabled(true);

        }
    }


    private void saveMessageInfoToDatabase() {
        String mensaje = userMessageInput.getText().toString().trim();
        String mensajekey = ref1.push().getKey();
        String mensajekey1 = ref2.push().getKey();
        if (TextUtils.isEmpty(mensaje)) {
            Toast.makeText(this, "Mnesaje vacio no enviado", Toast.LENGTH_SHORT).show();
        } else {
            Calendar ccalForDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MM dd, yyyy");
            currentDate = currentDateFormat.format(ccalForDate.getTime());


            Calendar ccalForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormat.format(ccalForTime.getTime());

            HashMap<String, Object> groupMessageKey = new HashMap<>();
            HashMap<String, Object> MensajeKey2 = new HashMap<>();

            ref1.updateChildren(groupMessageKey);
            ref2.updateChildren(MensajeKey2);

            GroupMessageKeyRef = ref1.child(mensajekey);
            MensajeKeyRef2 = ref2.child(mensajekey1);

            HashMap<String, Object> mensajeInfoMap = new HashMap<>();
            mensajeInfoMap.put("user_send", nombreRecibido);
            mensajeInfoMap.put("user_recived", visit_user_name);
            mensajeInfoMap.put("mensaje", mensaje);
            mensajeInfoMap.put("fecha", currentDate);
            mensajeInfoMap.put("hora", currentTime);
            GroupMessageKeyRef.updateChildren(mensajeInfoMap);

            HashMap<String, Object> mensajeInfoMap1 = new HashMap<>();
            mensajeInfoMap1.put("user_send", nombreRecibido);
            mensajeInfoMap1.put("user_recived", visit_user_name);
            mensajeInfoMap1.put("mensaje", mensaje);
            mensajeInfoMap1.put("fecha", currentDate);
            mensajeInfoMap1.put("hora", currentTime);
            MensajeKeyRef2.updateChildren(mensajeInfoMap1);


        }


    }

    private void Enburbuja(DataSnapshot dataSnapshot) {

     /*   Map map = dataSnapshot.getValue(Map.class);
        String fecha = map.get("fecha").toString();
        String hora = map.get("hora").toString();
        String mensaje = map.get("mensaje").toString();
        String emisor = map.get("user_recived").toString();
        String receptor = map.get("user_send").toString();*/

        Iterator iterator1 = dataSnapshot.getChildren().iterator();
        while (iterator1.hasNext()) {
            String fecha = (String) ((DataSnapshot) iterator1.next()).getValue();
            String hora = (String) ((DataSnapshot) iterator1.next()).getValue();
            String mensaje = (String) ((DataSnapshot) iterator1.next()).getValue();
            String emisor = (String) ((DataSnapshot) iterator1.next()).getValue();
            String receptor = (String) ((DataSnapshot) iterator1.next()).getValue();


            if (!emisor.equals(nombreRecibido)) {
                addMessageBox( mensaje+"\n"+hora, 1);
            } else {
                addMessageBox( mensaje+"\n"+hora, 2);
            }
        }
    }


    private void DisplayMessages(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()) {
            String chatFecha = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatHora = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatMensaje = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatNameEmisor = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatNameReceptor = (String) ((DataSnapshot) iterator.next()).getValue();



            dispalyTextMessage.append("Emisor: " + chatNameReceptor + "\n" +
                    "Receptor: " + chatNameEmisor + "\n" +
                    "Mensaje: " + chatMensaje + "\n" +
                    chatHora + "   " +
                    chatFecha + "\n\n");

            mScroolView.fullScroll(ScrollView.FOCUS_DOWN);

        }
    }



    public void addMessageBox(String message, int type) {
        TextView textView = new TextView(ChatActivityPruebaFinal.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if (type == 1) {
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.in_message_bg);
        } else {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.out_message_bg);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
       scrollView.fullScroll(View.FOCUS_DOWN);
    }



}
