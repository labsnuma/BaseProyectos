package com.numa.cardmax.numapp.Chat.Vista;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.numa.cardmax.numapp.Muro.MuroMainActivity;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MuroMainActivity.class);
        startActivity(intent);
        finish();
    }
}
