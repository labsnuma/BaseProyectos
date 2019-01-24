package com.cardmax.base.Chat.Grupos;

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

import com.cardmax.base.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivityFinal extends AppCompatActivity {
    private Toolbar mTollbar;
    //private ImageButton sendMessageBtutton;
    private EditText userMessageInput;
    private ScrollView mScroolView;
    private TextView dispalyTextMessage;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, GroupNameRef, GroupMessageKeyRef;
    private String currentGroupName, currentUserID, currentUserName, currentDate, currentTime;



    ///
    private LinearLayout layout;
    private RelativeLayout layout_2;
    private ScrollView scrollView;
    private Toolbar mToolbar;
    private TextView userProfileName;
   // private CircleImageView userProfileImage;
   private FloatingActionButton sendMessageBtutton,audio;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_group_final);

        currentGroupName = getIntent().getExtras().get("groupName").toString();
        //Toast.makeText(this, currentGroupName, Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Grupos").child(currentGroupName);


        initializeFields();
        userProfileName.setText(currentGroupName);


        GetUserInfo();



        sendMessageBtutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                saveMessageInfoToDatabase();
                userMessageInput.setText("");
               // mScroolView.fullScroll(ScrollView.FOCUS_DOWN);
                scrollView = (ScrollView) findViewById(R.id.scrollView);

            }
        });


    }

    private void initializeFields() {
       // userProfileImage = (CircleImageView) findViewById(R.id.fotoDePerfilSolicitud);
        userProfileName = (TextView) findViewById(R.id.nombreamigo);
      //  userLasSeen=(TextView) findViewById(R.id.fecha);

        mToolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(mToolbar);


        //sendMessageBtutton = (ImageButton) findViewById(R.id.boton_enviar);
        //sendMessageBtutton = (Button) findViewById(R.id.boton_enviar);
       sendMessageBtutton = (FloatingActionButton) findViewById(R.id.boton_enviar);
        audio= (FloatingActionButton) findViewById(R.id.boton_micorfono);

        userMessageInput = (EditText) findViewById(R.id.input_message);
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
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout) findViewById(R.id.layout2);

    }


    private void siesvacio() {
        String mensaje =  userMessageInput.getText().toString().trim();//"   hola  " => "hola"
     //   String mensaje =  userMessageInput.getText().toString();
     //  String mensaje= this.userMessageInput.getText().toString().trim();
       // if (!mensaje.isEmpty()) {
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



    @Override
    protected void onStart() {
        super.onStart();
        GroupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
               //     DisplayMessages(dataSnapshot);
                    Enburbuja(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               // Enburbuja(dataSnapshot);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                //    DisplayMessages(dataSnapshot);
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


    private void GetUserInfo() {
        UserRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentUserName = dataSnapshot.child("name").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveMessageInfoToDatabase() {
        String mensaje = userMessageInput.getText().toString().trim();
        String mensajekey = GroupNameRef.push().getKey();
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
            GroupNameRef.updateChildren(groupMessageKey);

            GroupMessageKeyRef = GroupNameRef.child(mensajekey);

            HashMap<String, Object> mensajeInfoMap = new HashMap<>();
            mensajeInfoMap.put("user_send", currentUserName);
            mensajeInfoMap.put("mensaje", mensaje);
            mensajeInfoMap.put("fecha", currentDate);
            mensajeInfoMap.put("hora", currentTime);
            GroupMessageKeyRef.updateChildren(mensajeInfoMap);
        }


    }

    private void Enburbuja(DataSnapshot dataSnapshot) {

        Iterator iterator1 = dataSnapshot.getChildren().iterator();
        while (iterator1.hasNext()) {
            String fecha = (String) ((DataSnapshot) iterator1.next()).getValue();
            String hora = (String) ((DataSnapshot) iterator1.next()).getValue();
            String mensaje = (String) ((DataSnapshot) iterator1.next()).getValue();
            String emisor = (String) ((DataSnapshot) iterator1.next()).getValue();



            if (emisor.equals(currentUserName)) {
                addMessageBox( mensaje+"\n"+hora, 1);
            } else {
                addMessageBox( emisor+": "+"\n"+mensaje+"\n"+hora, 2);
            }
        }
    }


    public void addMessageBox(String message, int type) {
        TextView textView = new TextView(GroupChatActivityFinal.this);
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
