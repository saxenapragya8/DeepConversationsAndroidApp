package deepconversations.fste.com.deepconversations.listeners;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Pragya on 3/25/2017.
 */

public class FireauthStateChangeListener implements FirebaseAuth.AuthStateListener {

    private Context ctx;
    public FireauthStateChangeListener(Context ctx){
        this.ctx = ctx;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
//            Intent intent = new Intent(ctx, AddNewConversationActivity.class);
//            ctx.startActivity(intent);
        } else {
            // User is signed out
            Log.d("FireauthStateChange", "onAuthStateChanged:signed_out");
        }
    }
}

