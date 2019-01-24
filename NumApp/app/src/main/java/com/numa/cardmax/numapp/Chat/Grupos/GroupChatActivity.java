package com.numa.cardmax.numapp.Chat.Grupos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.numa.cardmax.numapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class GroupChatActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mTollbar;
    private ImageButton sendMessageBtutton;
    private EditText userMessageInput;
    private ScrollView mScroolView;
    private TextView dispalyTextMessage;
 //   private FirebaseAuth mAuth;
    private DatabaseReference UserRef, GroupNameRef, GroupMessageKeyRef;
    public String currentGroupName, currentUserID, currentUserName, currentDate, currentTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_group_chat);

        currentGroupName = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("groupName")).toString();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName);


        initializeFields();
        GetUserInfo();
        servicio();
        sendMessageBtutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMessageInfoToDatabase();
                userMessageInput.setText("");
                mScroolView.fullScroll(ScrollView.FOCUS_DOWN);

            }
        });


    }

    private void servicio() {
        GroupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DisplayMessages(dataSnapshot);
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

    @Override
    public void onClick(View v) {

    }


    private void initializeFields() {
        mTollbar = (Toolbar) findViewById(R.id.group_chat_bar_layout);
        setSupportActionBar(mTollbar);
        getSupportActionBar().setTitle(currentGroupName);

        sendMessageBtutton = (ImageButton) findViewById(R.id.send_message_button);
        userMessageInput = (EditText) findViewById(R.id.input_group_message);
        dispalyTextMessage = (TextView) findViewById(R.id.group_chat_text_dispaly);
        mScroolView = (ScrollView) findViewById(R.id.my_scroll_view);
    }

    public void GetUserInfo() {
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
        String mensaje = userMessageInput.getText().toString();
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
            mensajeInfoMap.put("name", currentUserName);
            mensajeInfoMap.put("message", mensaje);
            mensajeInfoMap.put("date", currentDate);
            mensajeInfoMap.put("time", currentTime);
            GroupMessageKeyRef.updateChildren(mensajeInfoMap);

        }


    }

    private void DisplayMessages(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()) {
            String chatDate = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatMessage = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot) iterator.next()).getValue();
            dispalyTextMessage.append(chatName + ":\n" + chatMessage +
                    ":\n" + chatTime + "   " + chatDate+"\n\n\n");

            mScroolView.fullScroll(ScrollView.FOCUS_DOWN);

        }
    }



}
