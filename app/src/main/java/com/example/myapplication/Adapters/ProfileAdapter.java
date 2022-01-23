package com.example.myapplication.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.AutopayData;
import com.example.myapplication.NewMemberData;
import com.example.myapplication.Profile;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    Context context;
    ArrayList<NewMemberData> memberData;

    public ProfileAdapter(Context context, ArrayList<NewMemberData> memberData) {
        this.context = context;
        this.memberData = memberData;
    }

    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.member_recycle, parent, false);
        return new ProfileAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.memberName.setText(memberData.get(position).getName());
       holder.memberType.setText(memberData.get(position).getAdminaccess());
    }

    @Override
    public int getItemCount() {
        return memberData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView memberName, memberType;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            memberName = itemView.findViewById(R.id.member_name);
            memberType = itemView.findViewById(R.id.member_type);
        }
    }
}
