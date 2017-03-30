package deepconversations.fste.com.deepconversations.listeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.net.URLEncoder;
import java.util.Iterator;

import deepconversations.fste.com.deepconversations.R;
import deepconversations.fste.com.deepconversations.firebase.AppConstants;
import deepconversations.fste.com.deepconversations.firebase.RealtimeDbConstants;
import deepconversations.fste.com.deepconversations.firebase.RealtimeDbWriter;

/**
 * Created by Pragya on 3/26/2017.
 */

public class AddFriendExistsListener implements ValueEventListener {
    private Context ctx;
    private String email;

    private int REQUEST_INVITE = 1;
    public AddFriendExistsListener(Context ctx, String email){
        this.ctx = ctx;
        this.email = email;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        Iterator friendData = dataSnapshot.getChildren().iterator();
        if(friendData.hasNext()){
            Toast.makeText(ctx, "The friend is already a platform user. Sent the friend request ", Toast.LENGTH_LONG).show();
            DataSnapshot obj = (DataSnapshot) friendData.next();
            String appName = obj.child(RealtimeDbConstants.APP_ID).getKey();

            if(appName != null){
//                Toast.makeText(ctx, "The friend is already using the app " + RealtimeDbConstants.APP_ID, Toast.LENGTH_LONG).show();
                String friendUserId = obj.getKey();
                String friendUserName = (String)obj.child(RealtimeDbConstants.USER_NAME).getValue();
                RealtimeDbWriter.getInstance(ctx, false).addUserToFriendNode(friendUserId, RealtimeDbConstants.ACCEPT_INVITE);
                RealtimeDbWriter.getInstance(ctx, false).addFriend(friendUserId, friendUserName, RealtimeDbConstants.INVITED);
            } else {
//                String title = "The friend is on our platform but not using the app " + RealtimeDbConstants.APP_ID;
//                Toast.makeText(ctx, title, Toast.LENGTH_LONG).show();
//                showYesNoDialog(title);
                showFirebaseInvitePage();
            }
        } else {
            showFirebaseInvitePage();
//            String title = "The friend is not using the platform " + RealtimeDbConstants.APP_ID;
//            showYesNoDialog(title);
        }
    }

    public void showFirebaseInvitePage(){

        Intent intent = new AppInviteInvitation.IntentBuilder(ctx.getString(R.string.invitation_title))
                        .setMessage(ctx.getString(R.string.invitation_message))
                .setDeepLink(Uri.parse("https://zyhh4.app.goo.gl/?link=http://www.whatareyourthoughts.org/&apn=deepconversations.fste.com.deepconversations&afl=http://www.whatareyourthoughts.org/"))
//                .setCustomImage(Uri.parse(ctx.getString(R.string.invitation_custom_image)))
//                .setCallToActionText(ctx.getString(R.string.invitation_cta))
                        .build();
                ((Activity)ctx).startActivityForResult(intent, AppConstants.REQUEST_CODE_FIREBASE_INVITES);
    }

//    private void showYesNoDialog(String title) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
//
//        builder.setTitle(title);
//        builder.setMessage("Do you want to invite via email?");
//
//        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int which) {
//                // Do do my action here
////
//                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
//                emailIntent.setType("plain/text");
//                Intent.createChooser(emailIntent, "Send your email with:");
//                emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[] { email });
//                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Invite to join What are your thoughts");
//                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Please login to the app with the link");
//                ctx.startActivity(emailIntent);
//                dialog.dismiss();
//            }
//        });
//
//        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // I do not need any action here you might
//                dialog.dismiss();
//            }
//        });
//
//        AlertDialog alert = builder.create();
//        alert.show();
//    }
//
    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
