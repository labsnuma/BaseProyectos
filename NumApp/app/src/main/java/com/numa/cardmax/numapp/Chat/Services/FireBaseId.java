package com.numa.cardmax.numapp.Chat.Services;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by KevinPiazzoli on 09/02/2017.
 */

public class FireBaseId extends FirebaseInstanceIdService{

    public FireBaseId(){

    }
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
 /*       String refreshdToken = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(refreshdToken);

    }

    private void sendRegistrationToServer(String token) {
        Log.d("Token",token);
    }*/
    }
}
