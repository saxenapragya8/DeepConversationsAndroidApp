package deepconversations.fste.com.deepconversations.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import deepconversations.fste.com.deepconversations.R;

/**
 * Created by Pragya on 3/25/2017.
 */

public class PreferenceManager {

    private static Context ctx;
    private static PreferenceManager instance;

    private PreferenceManager(Context ctx){
        this.ctx = ctx;
    }

    public static PreferenceManager getInstance(Context ctx){
        if(instance == null){
            instance = new PreferenceManager(ctx);
        }
        return instance;
    }

    public String getUserId(){
        SharedPreferences sharedPref = ctx.getSharedPreferences(ctx.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedPref.getString(ctx.getString(R.string.user_id_field), "");
    }

    public String getUserDisplayName(){
        SharedPreferences sharedPref = ctx.getSharedPreferences(ctx.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedPref.getString(ctx.getString(R.string.user_display_name_field), "");
    }

    public String getUserEmail(){
        SharedPreferences sharedPref = ctx.getSharedPreferences(ctx.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedPref.getString(ctx.getString(R.string.user_email), "");
    }

    public void storeUserIdInPrefs(String userId, String userDisplayName, String email){
        SharedPreferences sharedPref = ctx.getSharedPreferences(ctx.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ctx.getString(R.string.user_id_field), userId);
        editor.putString(ctx.getString(R.string.user_display_name_field), userDisplayName);
        editor.putString(ctx.getString(R.string.user_email), email);
        editor.commit();
    }
}
