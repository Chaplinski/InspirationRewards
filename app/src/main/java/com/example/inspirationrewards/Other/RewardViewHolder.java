package com.example.inspirationrewards.Other;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.inspirationrewards.Classes.User;
import com.example.inspirationrewards.R;

import androidx.recyclerview.widget.RecyclerView;


public class RewardViewHolder extends RecyclerView.ViewHolder {

    public TextView date;
    public TextView sName;
    public TextView sScore;
    public TextView sNote;

    public RewardViewHolder(View view){
        super(view);

        date = view.findViewById(R.id.historyDate);
        sName = view.findViewById(R.id.historyName);
        sScore = view.findViewById(R.id.historyScore);
        sNote = view.findViewById(R.id.historyNote);



    }
}
