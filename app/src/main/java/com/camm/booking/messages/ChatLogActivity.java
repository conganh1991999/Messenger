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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.camm.booking.R;
import com.camm.booking.models.Message;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatLogActivity extends AppCompatActivity {

    private RecyclerView recyclerviewChatLog;
    private Button btnSendMessage;
    private EditText edtTypeMessage;

    private String friendName;
    private String friendImage;
    private String friendId;

    private String myImage;
    private String myId;

    private GroupAdapter adapter = new GroupAdapter<GroupieViewHolder>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_log);

        Mapping();
        getChatLogInfo();
        setupActionBar();
        manageRecycleView();

        recyclerviewChatLog.setAdapter(adapter);

        listenForMessage();

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSendMessage();
            }
        });
    }

    private void Mapping(){
        btnSendMessage = findViewById(R.id.btnSendMessage);
        edtTypeMessage = findViewById(R.id.edtTypeMessage);
    }

    private void getChatLogInfo(){
        Intent intent = getIntent();
        friendName = intent.getStringExtra("friendName");
        friendImage = intent.getStringExtra("friendImage");
        friendId = intent.getStringExtra("friendId");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser myInfo = mAuth.getCurrentUser();
        assert myInfo != null;
        myId = myInfo.getUid();

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mDatabase.getReference("users");

        // Read from the database
        mRef.child(myId).child("userImage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                myImage = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    private void setupActionBar(){
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(friendName);
    }

    private void manageRecycleView(){
        recyclerviewChatLog = findViewById(R.id.recyclerviewChatLog);
        recyclerviewChatLog.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerviewChatLog.setLayoutManager(manager);
    }

    private void performSendMessage(){
        String text = edtTypeMessage.getText().toString();
        if(text.length() > 0){
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("messages").push();
            Message message = new Message(text, friendId, myId, mRef.getKey(), System.currentTimeMillis()/1000);
            mRef.setValue(message);
        }
    }

    private void listenForMessage(){
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("messages");
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                if(message != null){
                    if(message.getToId().equals(myId) && message.getFromId().equals(friendId)){
                        // groupie automatically refresh adapter elements
                        adapter.add(new ChatToItem(message.getMessage(), myImage));
                    }
                    else if(message.getToId().equals(friendId) && message.getFromId().equals(myId)){
                        // groupie automatically refresh adapter elements
                        adapter.add(new ChatFromItem(message.getMessage(), friendImage));
                    }
                }
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

    class ChatFromItem extends Item<GroupieViewHolder>{

        String message;
        String image;

        ChatFromItem(String message, String image){
            this.message = message;
            this.image = image;
        }

        TextView rowTextChatFrom;
        CircleImageView rowImageChatFrom;

        @Override
        public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
            rowTextChatFrom = viewHolder.itemView.findViewById(R.id.rowTextChatFrom);
            rowImageChatFrom = viewHolder.itemView.findViewById(R.id.rowImageChatFrom);

            rowTextChatFrom.setText(message);
            Picasso.get().load(image).into(rowImageChatFrom);
        }

        @Override
        public int getLayout() {
            return R.layout.chat_from_row;
        }
    }

    class ChatToItem extends Item<GroupieViewHolder>{

        String message;
        String image;

        ChatToItem(String myMessage, String myImage){
            this.message = myMessage;
            this.image = myImage;
        }

        TextView rowTextChatTo;
        CircleImageView rowImageChatTo;

        @Override
        public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
            rowTextChatTo = viewHolder.itemView.findViewById(R.id.rowTextChatTo);
            rowImageChatTo = viewHolder.itemView.findViewById(R.id.rowImageChatTo);

            rowTextChatTo.setText(message);
            Picasso.get().load(image).into(rowImageChatTo);
        }

        @Override
        public int getLayout() {
            return R.layout.chat_to_row;
        }
    }

}


