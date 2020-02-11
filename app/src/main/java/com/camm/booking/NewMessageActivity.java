package com.camm.booking;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class NewMessageActivity extends AppCompatActivity {

    RecyclerView recyclerviewNewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        Mapping();

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Select User");
    }

    private void Mapping(){
        recyclerviewNewMessage = findViewById(R.id.recyclerviewNewMessage);
    }
}
