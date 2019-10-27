package com.example.inspirationrewards.Other;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inspirationrewards.Activities.LeaderboardActivity;
import com.example.inspirationrewards.Activities.ProfileActivity;
import com.example.inspirationrewards.Classes.Reward;
import com.example.inspirationrewards.Classes.User;
import com.example.inspirationrewards.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class RewardAdapter extends RecyclerView.Adapter<RewardViewHolder> {

    private static final String TAG ="RewardAdapter";
    private List<Reward> rewardList;
    private ProfileActivity profAct;

    public RewardAdapter(List<Reward> rewardList, ProfileActivity pa) {
        this.rewardList = rewardList;
        profAct = pa;
    }

    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType){
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_history, parent, false);

        return new RewardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {

        Reward reward = rewardList.get(position);

        String sDate = reward.getDate();
        String sNote = reward.getNote();
        String sName = reward.getSourceName();
        int iPoints = reward.getRewardPoints();
        String sPoints = Integer.toString(iPoints);

        holder.date.setText(sDate);
        holder.sName.setText(sName);
        holder.sNote.setText(sNote);
        holder.sScore.setText(sPoints);

    }




    public void removeItem(int position) {
        rewardList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount(){ return rewardList.size(); }

}
