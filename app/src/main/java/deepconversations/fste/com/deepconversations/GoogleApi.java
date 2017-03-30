package deepconversations.fste.com.deepconversations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;

import deepconversations.fste.com.deepconversations.firebase.RealtimeDbReader;
import deepconversations.fste.com.deepconversations.listeners.GoogleSigninFailedListener;

/**
 * Created by Pragya on 3/27/2017.
 */

public class GoogleApi {

    private GoogleApiClient mGoogleApiClient;

    private static Context ctx;
    private static GoogleApi instance;
    private GoogleApi(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(ctx.getString(R.string.default_web_client_id))
                .requestEmail()
                .requestScopes(new Scope(Scopes.EMAIL), new Scope(Scopes.PROFILE), new Scope(Scopes.PLUS_LOGIN), new Scope(Scopes.PLUS_ME))
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                .enableAutoManage(((AppCompatActivity)ctx )/* FragmentActivity */, new GoogleSigninFailedListener(ctx) /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(AppInvite.API)
                .build();
    }

    public static GoogleApi getInstance(Context ctx){
        GoogleApi.ctx = ctx;
        if(instance == null){
            instance = new GoogleApi();
        }
        return instance;
    }

    public GoogleApiClient getGoogleApiClient(){
        return mGoogleApiClient;
    }
}
