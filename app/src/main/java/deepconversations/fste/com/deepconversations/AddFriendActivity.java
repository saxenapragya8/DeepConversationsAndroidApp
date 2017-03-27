package deepconversations.fste.com.deepconversations;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;

import deepconversations.fste.com.deepconversations.firebase.AppConstants;
import deepconversations.fste.com.deepconversations.firebase.RealtimeDbReader;

public class AddFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
    }

    public void onSendInvite(View view){
        TextView emailInput = (TextView) findViewById(R.id.friendEmail);
        RealtimeDbReader.getInstance(this).getEmailUserId(emailInput.getText().toString());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUEST_CODE_FIREBASE_INVITES) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages

                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                Toast.makeText(this, "Successfully sent invitations " + ids.toString(), Toast.LENGTH_LONG).show();
                for (String id : ids) {
                    Log.d("SentInvite", "onActivityResult: sent invitation " + id);
                }
            } else {
                Toast.makeText(this, "Did not send invitations ", Toast.LENGTH_LONG).show();
            }
        }
    }
}
