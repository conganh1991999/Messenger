package com.camm.booking.messages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.camm.booking.R;
import com.camm.booking.models.LatestMessage;
import com.camm.booking.registerlogin.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class LatestMessagesActivity extends AppCompatActivity {

    RecyclerView recyclerviewLatestMessages;

    String myId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_messages);

        verifyUserIsLoggedIn();
        setAppActionBar();
        manageRecycleView();

        setDummyData();

    }

    private void verifyUserIsLoggedIn(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(LatestMessagesActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void setAppActionBar(){
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Messenger");
        actionBar.setSubtitle("Lite");
        actionBar.setLogo(R.drawable.messenger);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
    }

    private void manageRecycleView(){
        recyclerviewLatestMessages = findViewById(R.id.recyclerviewLatestMessages);
        recyclerviewLatestMessages.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerviewLatestMessages.setLayoutManager(manager);
    }

    private void setDummyData(){
        FirebaseUser myInfo = FirebaseAuth.getInstance().getCurrentUser();
        assert myInfo != null;
        myId = myInfo.getUid();

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("latest");
        mRef.child(myId).addChildEventListener(new ChildEventListener() {

            GroupAdapter adapter = new GroupAdapter<GroupieViewHolder>();

            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                final LatestMessage latestMessage = dataSnapshot.child("message").getValue(LatestMessage.class);

                if(latestMessage != null){
                    adapter.add(new LatestMessageItem(
                            latestMessage,
                            dataSnapshot.child("friendName").getValue(String.class),
                            dataSnapshot.child("friendImage").getValue(String.class),
                            dataSnapshot.child("friendId").getValue(String.class)));
                }

                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(@NonNull Item item, @NonNull View view) {
                        // Send friend's data to new ChatLogActivity

                        LatestMessageItem latestMessageItem = (LatestMessageItem) item;

                        Intent intent = new Intent(LatestMessagesActivity.this, ChatLogActivity.class);
                        intent.putExtra("friendName", latestMessageItem.friendName);
                        intent.putExtra("friendImage", latestMessageItem.friendImage);
                        intent.putExtra("friendId", latestMessageItem.friendId);
                        startActivity(intent);
                    }
                });

                recyclerviewLatestMessages.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.menuNewChat:
                intent = new Intent(LatestMessagesActivity.this, NewMessageActivity.class);
                startActivity(intent); // here
                break;
            case R.id.menuSignOut:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(LatestMessagesActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class LatestMessageItem extends Item<GroupieViewHolder> {

        LatestMessage latestMessage;
        String friendName;
        String friendImage;
        String friendId;

        LatestMessageItem(LatestMessage latestMessage, String friendName, String friendImage, String friendId){
            this.latestMessage = latestMessage;
            this.friendName = friendName;
            this.friendImage = friendImage;
            this.friendId = friendId;
        }

        TextView rowLatestUsername, rowLatestTime, rowLatestMessage;
        CircleImageView rowLatestImage;

        @Override
        public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
            rowLatestUsername = viewHolder.itemView.findViewById(R.id.rowLatestUsername);
            rowLatestImage = viewHolder.itemView.findViewById(R.id.rowLatestImage);
            rowLatestMessage = viewHolder.itemView.findViewById(R.id.rowLatestMessage);
            rowLatestTime = viewHolder.itemView.findViewById(R.id.rowLatestTime);

            rowLatestUsername.setText(this.friendName);
            rowLatestMessage.setText(latestMessage.getMessage());
            rowLatestTime.setText(latestMessage.getDate());

            Picasso.get().load(this.friendImage).into(rowLatestImage);
        }

        @Override
        public int getLayout() {
            return R.layout.latest_message_row;
        }
    }
}
