package com.camm.booking.messages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.camm.booking.R;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

public class ChatLogActivity extends AppCompatActivity {

    private RecyclerView recyclerviewChatLog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_log);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Chat Log");

        recyclerviewChatLog = findViewById(R.id.recyclerviewChatLog);
        manageRecycleView();
        GroupAdapter adapter = new GroupAdapter< GroupieViewHolder>();
        adapter.add(new ChatItem());
        recyclerviewChatLog.setAdapter(adapter);

    }

    private void manageRecycleView(){
        recyclerviewChatLog.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerviewChatLog.setLayoutManager(manager);
    }

    class ChatItem extends Item<GroupieViewHolder>{

        @Override
        public void bind(@NonNull GroupieViewHolder viewHolder, int position) {

        }

        @Override
        public int getLayout() {
            return R.layout.chat_from_row;
        }
    }

}


