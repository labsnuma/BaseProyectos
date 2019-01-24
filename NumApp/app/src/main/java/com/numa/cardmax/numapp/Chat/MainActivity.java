package com.numa.cardmax.numapp.Chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.numa.cardmax.numapp.R;


public class MainActivity extends AppCompatActivity {

/*    private static final String PATH_START = "start";
    private static final String PATH_MESSAGE = "message";
    @BindView(R.id.editTextmessage)
    EditText editTextmessage;
    @BindView(R.id.btnsend)
    Button btnsend;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_main1);
        /*      ButterKnife.bind(this);
        final TextView mensaje = findViewById(R.id.mensajes);

  FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference(PATH_START).child(PATH_MESSAGE);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //  String value = dataSnapshot.getValue(String.class);
                mensaje.setText(dataSnapshot.getValue(String.class));
                editTextmessage.setText(dataSnapshot.getValue(String.class));
                // Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //   Log.w(TAG, "Failed to read value.", error.toException());
                Toast.makeText(MainActivity.this, "Error al consultar en firebase.", Toast.LENGTH_LONG).show();
            }
        });*/

    }
}
