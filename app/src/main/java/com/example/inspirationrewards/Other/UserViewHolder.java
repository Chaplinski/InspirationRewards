package com.example.inspirationrewards.Other;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.inspirationrewards.Classes.User;
import com.example.inspirationrewards.R;

import androidx.recyclerview.widget.RecyclerView;


public class UserViewHolder extends RecyclerView.ViewHolder {

    public ImageView image;
    public TextView sName;
    public TextView sPosition;
    public TextView iScore;

    public UserViewHolder(View view){
        super(view);

        image = view.findViewById(R.id.lbImage);
        sName = view.findViewById(R.id.lbName);
        sPosition = view.findViewById(R.id.lbPosition);
        iScore = view.findViewById(R.id.lbScore);



    }
}
