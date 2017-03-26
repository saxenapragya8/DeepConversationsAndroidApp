package deepconversations.fste.com.deepconversations.listeners;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Pragya on 3/25/2017.
 */

public class GoogleSigninFailedListener  implements GoogleApiClient.OnConnectionFailedListener {
    Context ctx;
    public GoogleSigninFailedListener(Context ctx){
        this.ctx = ctx;
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(ctx, "Google Sign in connection failed" + connectionResult.toString(), Toast.LENGTH_LONG).show();
    }
}
