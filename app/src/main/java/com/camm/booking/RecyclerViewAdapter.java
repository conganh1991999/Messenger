package com.camm.booking;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<User> listUser;


    RecyclerViewAdapter(ArrayList<User> listUser) {
        this.listUser = listUser;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtUsername;
        CircleImageView imgUser;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.rowUsername);
            imgUser = itemView.findViewById(R.id.rowUserImage);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtUsername.setText(listUser.get(position).getUserName());
        Uri uri = Uri.parse(listUser.get(position).getUserImage());
        Picasso.get().load(uri).into(holder.imgUser);
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

}
