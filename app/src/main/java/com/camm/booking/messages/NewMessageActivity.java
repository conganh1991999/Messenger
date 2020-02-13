package com.camm.booking.messages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.camm.booking.R;
import com.camm.booking.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewMessageActivity extends AppCompatActivity {

    RecyclerView recyclerviewNewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        setupActionBar();
        manageRecycleView();
        getUsersFromDatabase();

    }

    private void setupActionBar(){
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Select User");
    }

    private void manageRecycleView(){
        recyclerviewNewMessage = findViewById(R.id.recyclerviewNewMessage);
        recyclerviewNewMessage.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerviewNewMessage.setLayoutManager(manager);
    }

    private void updateLatestInfo(final String friendId, String friendName, String friendImage){

        FirebaseUser myInfo = FirebaseAuth.getInstance().getCurrentUser();
        assert myInfo != null;
        final String myId = myInfo.getUid();

        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("latest");

        mRef.child(myId).child(friendId).child("friendName").setValue(friendName);
        mRef.child(myId).child(friendId).child("friendImage").setValue(friendImage);
        mRef.child(myId).child(friendId).child("friendId").setValue(friendId);

        DatabaseReference iRef = FirebaseDatabase.getInstance().getReference("users");
        iRef.child(myId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String myName = dataSnapshot.child("userName").getValue(String.class);
                String myImage = dataSnapshot.child("userImage").getValue(String.class);

                mRef.child(friendId).child(myId).child("friendName").setValue(myName);
                mRef.child(friendId).child(myId).child("friendImage").setValue(myImage);
                mRef.child(friendId).child(myId).child("friendId").setValue(myId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUsersFromDatabase(){

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mDatabase.getReference("users");
        mRef.addChildEventListener(new ChildEventListener() {

            GroupAdapter adapter = new GroupAdapter<GroupieViewHolder>();

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                User user = dataSnapshot.getValue(User.class);
                if(user != null){
                    adapter.add(new UserItem(user));
                }

                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(@NonNull Item item, @NonNull View view) {

                        UserItem userItem = (UserItem) item;

                        updateLatestInfo(userItem.user.getUserId(), userItem.user.getUserName(), userItem.user.getUserImage());

                        // Send friend's data to new ChatLogActivity
                        Intent intent = new Intent(NewMessageActivity.this, ChatLogActivity.class);
                        intent.putExtra("friendName", userItem.user.getUserName());
                        intent.putExtra("friendImage", userItem.user.getUserImage()); // ?? qua hay ko
                        intent.putExtra("friendId", userItem.user.getUserId()); // ?? qua hay ko
                        startActivity(intent);
                        finish();
                    }
                });

                recyclerviewNewMessage.setAdapter(adapter);
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

    class UserItem extends Item<GroupieViewHolder>{

        User user;
        UserItem(User mUser){
            this.user = mUser;
        }

        TextView rowUsername;
        CircleImageView rowUserImage;

        @Override
        public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
            rowUsername = viewHolder.itemView.findViewById(R.id.rowUsername);
            rowUserImage = viewHolder.itemView.findViewById(R.id.rowUserImage);
            rowUsername.setText(user.getUserName());
            Picasso.get().load(user.getUserImage()).into(rowUserImage);
        }

        @Override
        public int getLayout() {
            return R.layout.new_chat_row;
        }
    }
}
