package deepconversations.fste.com.deepconversations.uieventhandlers;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import deepconversations.fste.com.deepconversations.NavigationActivity;
import deepconversations.fste.com.deepconversations.firebase.RealtimeDbConstants;
import deepconversations.fste.com.deepconversations.firebase.RealtimeDbReader;
import deepconversations.fste.com.deepconversations.firebase.RealtimeDbWriter;
import deepconversations.fste.com.deepconversations.firebase.model.FriendStatus;

/**
 * Created by Pragya on 4/9/2017.
 *
 */

public class AcceptFriendRequestListener implements View.OnClickListener {

    Context ctx;
    FriendStatus data;
    public AcceptFriendRequestListener(Context ctx, FriendStatus friendStatus){
        this.ctx = ctx;
        this.data = friendStatus;
    }
    @Override
    public void onClick(View v) {
        RealtimeDbWriter.getInstance(ctx, false).acceptFriend(data);
//        NavigationActivity.removeFriend(data);
    }
}
