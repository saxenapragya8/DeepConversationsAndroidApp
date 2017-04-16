package deepconversations.fste.com.deepconversations.uieventhandlers;

import android.content.Context;
import android.view.View;

import deepconversations.fste.com.deepconversations.firebase.RealtimeDbConstants;
import deepconversations.fste.com.deepconversations.firebase.RealtimeDbWriter;
import deepconversations.fste.com.deepconversations.firebase.model.FriendStatus;

/**
 * Created by Pragya on 4/15/2017.
 */

public class DeleteFriendRequestListener implements View.OnClickListener {

    Context ctx;
    FriendStatus data;
    public DeleteFriendRequestListener(Context ctx, FriendStatus friendStatus){
        this.ctx = ctx;
        this.data = friendStatus;
    }
    @Override
    public void onClick(View v) {
        RealtimeDbWriter.getInstance(ctx, false).deleteFriendRequest(data);
    }
}
