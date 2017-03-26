package deepconversations.fste.com.deepconversations;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
}
