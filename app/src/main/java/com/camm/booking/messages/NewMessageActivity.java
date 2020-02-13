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
// import com.camm.booking.models.RecyclerViewAdapter;
import com.camm.booking.models.User;
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

// import java.util.ArrayList;

public class NewMessageActivity extends AppCompatActivity {

    RecyclerView recyclerviewNewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Select User");

        manageRecycleView();
        getUsersFromDatabase();

    }

    private void manageRecycleView(){
        recyclerviewNewMessage = findViewById(R.id.recyclerviewNewMessage);
        recyclerviewNewMessage.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerviewNewMessage.setLayoutManager(manager);
    }

    private void getUsersFromDatabase(){
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mDatabase.getReference("users");

        mRef.addChildEventListener(new ChildEventListener() {

            // ArrayList<User> listUser = new ArrayList<>();
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

                        // Intent intent = new Intent(view.getContext(), ChatLogActivity.class);

                        // Send friend's data to new ChatLogActivity
                        Intent intent = new Intent(NewMessageActivity.this, ChatLogActivity.class);
                        intent.putExtra("friendName", userItem.user.getUserName());
                        intent.putExtra("friendImage", userItem.user.getUserImage());
                        intent.putExtra("friendId", userItem.user.getUserId());
                        startActivity(intent);
                        finish();
                    }
                });

                recyclerviewNewMessage.setAdapter(adapter);

                // User user = dataSnapshot.getValue(User.class);
                // listUser.add(user);
                // fetchDataIntoRecycleView(listUser);
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

    //    private void fetchDataIntoRecycleView(ArrayList<User> listUser){
//        recyclerviewNewMessage.setHasFixedSize(true);
//
//        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        recyclerviewNewMessage.setLayoutManager(manager);
//
//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(listUser);
//        recyclerviewNewMessage.setAdapter(adapter);
//    }

}
