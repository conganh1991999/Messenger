package com.camm.booking.models;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.camm.booking.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView txtUsername;
    CircleImageView imgUser;

    ViewHolder(@NonNull View itemView) {
        super(itemView);
        txtUsername = itemView.findViewById(R.id.rowUsername);
        imgUser = itemView.findViewById(R.id.rowUserImage);
    }
}
