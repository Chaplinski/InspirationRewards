package com.example.inspirationrewards.Other;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inspirationrewards.Activities.LeaderboardActivity;
import com.example.inspirationrewards.Classes.User;
import com.example.inspirationrewards.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private static final String TAG ="UserAdapter";
    private List<User> userList;
    private LeaderboardActivity leadAct;

    public UserAdapter(List<User> userList, LeaderboardActivity la) {
        this.userList = userList;
        leadAct = la;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType){
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_leaderboard, parent, false);

        itemView.setOnClickListener(leadAct);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        User user = userList.get(position);

        String sName = user.getLastName() + ", " + user.getFirstName();
        String sPosition = user.getPosition() + ", " + user.getDepartment();
        String sPoints = Integer.toString(user.getPointsAwarded());

        Bitmap bm = StringToBitMap(user.getImage());
        holder.image.setImageBitmap(bm);
        holder.sName.setText(sName);
        holder.sPosition.setText(sPosition);
        holder.iScore.setText(sPoints);

    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }


    public void removeItem(int position) {
        userList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount(){ return userList.size(); }

}
