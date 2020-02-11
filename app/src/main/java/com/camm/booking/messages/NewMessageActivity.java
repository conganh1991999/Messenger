package com.camm.booking.messages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.camm.booking.R;
import com.camm.booking.models.RecyclerViewAdapter;
import com.camm.booking.models.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NewMessageActivity extends AppCompatActivity {

    RecyclerView recyclerviewNewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Select User");

        Mapping();
        getUsersFromDatabase();

    }

    private void Mapping(){
        recyclerviewNewMessage = findViewById(R.id.recyclerviewNewMessage);
    }

    private void fetchDataIntoRecycleView(ArrayList<User> listUser){
        recyclerviewNewMessage.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerviewNewMessage.setLayoutManager(manager);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(listUser);
        recyclerviewNewMessage.setAdapter(adapter);
    }

    private void getUsersFromDatabase(){
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mDatabase.getReference("users");

        mRef.addChildEventListener(new ChildEventListener() {

            ArrayList<User> listUser = new ArrayList<>();

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                listUser.add(user);

                fetchDataIntoRecycleView(listUser);
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
}
