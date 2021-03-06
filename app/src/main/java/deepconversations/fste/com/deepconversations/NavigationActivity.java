package deepconversations.fste.com.deepconversations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import deepconversations.fste.com.deepconversations.adapters.NavigationViewAdapter;
import deepconversations.fste.com.deepconversations.firebase.AppConstants;
import deepconversations.fste.com.deepconversations.firebase.RealtimeDbReader;
import deepconversations.fste.com.deepconversations.firebase.RealtimeDbWriter;
import deepconversations.fste.com.deepconversations.firebase.model.FriendStatus;
import deepconversations.fste.com.deepconversations.firebase.model.GroupData;
import deepconversations.fste.com.deepconversations.preferences.PreferenceManager;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int REQUEST_CODE = 1;
    private GoogleApiClient mGoogleApiClientAppInviteOnly;
    private static List<FriendStatus> newFriendStatuses = new ArrayList<>();;
    private static List<GroupData> groupsData = new ArrayList<>();;
    private static NavigationViewAdapter adapter;

    public synchronized static void removeFriend(FriendStatus friend){
        newFriendStatuses.remove(friend);
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

    public synchronized static void friendChangeOrAdd(FriendStatus friend){
        FriendStatus friendInList = exists(friend);
        if(friendInList != null){
            friendInList.setCreatedAt(friend.getCreatedAt());
            friendInList.setStatus(friend.getStatus());
        } else {
            newFriendStatuses.add(friend);
        }
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

    private static FriendStatus exists(FriendStatus friend){
        for(FriendStatus friendStatus: newFriendStatuses){
            if(friendStatus.equals(friend)){
                return friendStatus;
            }
        }
        return null;
    }

    public synchronized static void addGroup(GroupData group){
        groupsData.add(group);
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

    public synchronized static void removeGroup(GroupData group){
        groupsData.remove(group);
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RealtimeDbWriter.getInstance(this, true);
        RealtimeDbReader.getInstance(this, true);
        RealtimeDbReader.addDataReadListeners();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        TextView userName = (TextView)hView.findViewById(R.id.navViewUserName);
        TextView email = (TextView)hView.findViewById(R.id.navViewEmail);
        userName.setText(PreferenceManager.getInstance(this).getUserDisplayName());
        email.setText(PreferenceManager.getInstance(this).getUserEmail());

        getAppInvites();

        //Setup the adapter
        View layout = drawer.findViewById(R.id.appBarNav);
        View navigationViewContent = layout.findViewById(R.id.navigationContent);
        RecyclerView recView = (RecyclerView)navigationViewContent.findViewById(R.id.friendsGroupRecView);
        adapter = new NavigationViewAdapter(newFriendStatuses, groupsData, this);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recView.setLayoutManager(mLayoutManager);

        recView.setAdapter(adapter);

//        TextView topic = (TextView)findViewById(R.id.topic);
//        TextView friend_request = (TextView)findViewById(R.id.statusMessage);
//        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/roboto_slab_regular.ttf");
//        topic.setTypeface(custom_font);
//        friend_request.setTypeface(custom_font);

    }

    public void getAppInvites(){
        // Check for App Invite invitations and launch deep-link activity if possible.
        // Requires that an Activity is registered in AndroidManifest.xml to handle
        // deep-link URLs.
        if(mGoogleApiClientAppInviteOnly == null) {
            mGoogleApiClientAppInviteOnly = new GoogleApiClient.Builder(this)
                    .addApi(AppInvite.API)
                    .enableAutoManage((AppCompatActivity) this, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            Log.w("StartActivity", "onConnectionFailed:" + connectionResult);
                        }
                    })
                    .build();
        }
        boolean autoLaunchDeepLink = false;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClientAppInviteOnly, (Activity)this, autoLaunchDeepLink)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(AppInviteInvitationResult result) {
                                Log.d("appInvites", "getInvitation:onResult:" + result.getStatus());
                                Intent intent = result.getInvitationIntent();
                                if (result.getStatus().isSuccess()) {
                                    String invitationId = AppInviteReferral.getInvitationId(intent);
                                    RealtimeDbReader.getInstance(NavigationActivity.this, false).getInviteIdInviteSentByUser(invitationId);
                                }
                            }
                        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAppInvites();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {
            case R.id.navAddFriend:
                Intent friendIntent = new Intent(Intent.ACTION_PICK);
                friendIntent.setType(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE);
                startActivityForResult(friendIntent, AppConstants.REQUEST_CODE_CONTACTS);
                break;
            case R.id.navInvite:
                Intent inviteIntent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                    .setMessage(getString(R.string.invitation_message))
                        .setDeepLink(Uri.parse("https://zyhh4.app.goo.gl/?link=http://www.whatareyourthoughts.org/&apn=deepconversations.fste.com.deepconversations&afl=http://www.whatareyourthoughts.org/"))
                    .build();
                startActivityForResult(inviteIntent, AppConstants.REQUEST_CODE_FIREBASE_INVITES);
                break;
            case R.id.navCreateGroup:
                Intent showAddGroupActivity = new Intent(this, AddGroupActivity.class);
                startActivity(showAddGroupActivity);
                break;
            case R.id.navArchives:
                Intent showArchivesActivity = new Intent(this, ArchivesActivity.class);
                startActivity(showArchivesActivity);
                break;
            case R.id.navSettings:
                Intent showSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(showSettingsActivity);
                break;
            case R.id.navHelp:
        }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstants.REQUEST_CODE_CONTACTS && resultCode == RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS};
            Cursor cursor = getContentResolver().query(contactUri, projection,
                    null, null, null);
            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                int emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
                String email = cursor.getString(emailIndex);
                // Do something with the phone number
                RealtimeDbReader.getInstance(this, true).getEmailUserId(email);
            }
            cursor.close();
        }
        if (requestCode == AppConstants.REQUEST_CODE_FIREBASE_INVITES) {
            if (resultCode == RESULT_OK) {
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                Toast.makeText(this, "Successfully sent invitations " + ids.toString(), Toast.LENGTH_LONG).show();
                RealtimeDbWriter.getInstance(this, true).addNewInviteIds(ids);
            } else {
                Toast.makeText(this, "Did not send invitations ", Toast.LENGTH_LONG).show();
            }
        }
    }
}
