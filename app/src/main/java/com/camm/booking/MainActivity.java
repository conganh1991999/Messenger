package com.camm.booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView profile;
    TextView txtHaveAnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profile = findViewById(R.id.profile_image);
        txtHaveAnAccount = findViewById(R.id.txtHaveAnAccount);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "hello, world!", Toast.LENGTH_SHORT).show();
            }
        });

        txtHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentToLogin);
            }
        });
    }
}
