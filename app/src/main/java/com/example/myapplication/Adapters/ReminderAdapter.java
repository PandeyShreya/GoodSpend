package com.example.myapplication.Adapters;

import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ReminderData;
import com.example.myapplication.ReminderEditActivity;

import java.util.ArrayList;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    Context context;
    ArrayList<ReminderData> reminderData;
    private OnItemClickListener reminderList;

    public ReminderAdapter(Context context, ArrayList<ReminderData> reminderData , OnItemClickListener reminderList){
        this.context = context;
        this.reminderData = reminderData;
        this.reminderList = reminderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reminder_list_look, parent, false);
//        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_list_look, parent, false));
        return new ViewHolder(v, reminderList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.reminderCategory.setText(reminderData.get(position).getCategory());
        holder.reminderNote.setText(reminderData.get(position).getNote());
        holder.reminderDate.setText(reminderData.get(position).getDate());
        holder.reminderTime.setText(reminderData.get(position).getTime());
        holder.reminderAmount.setText(valueOf((int) reminderData.get(position).getAmount()));

        //calling edit activity and sending data
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ReminderEditActivity.class);
                intent.putExtra("note", reminderData.get(position).getNote());
                intent.putExtra("repeatReminder", reminderData.get(position).getRepeatReminder());
                Double amount= reminderData.get(position).getAmount();
                intent.putExtra("amount", amount.toString());
                intent.putExtra("cateogry", reminderData.get(position).getCategory());
                intent.putExtra("date", reminderData.get(position).getDate());
                intent.putExtra("time", reminderData.get(position).getTime());
                intent.putExtra("id",reminderData.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() { return reminderData.size();}

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView reminderCategory, reminderAmount, reminderDate, reminderNote, reminderTime;
        //private LinearLayout toplayout;
        private RelativeLayout toplayout;
        OnItemClickListener reminderList;

        public ViewHolder(@NonNull View itemView, OnItemClickListener reminderList) {
            super(itemView);
            reminderCategory = itemView.findViewById(R.id.reminder_category);
            reminderAmount = itemView.findViewById(R.id.reminder_amount);
            reminderDate =  itemView.findViewById(R.id.reminder_date);
            reminderNote = itemView.findViewById(R.id.reminder_note);
            reminderTime = itemView.findViewById(R.id.reminder_time);
            toplayout = itemView.findViewById(R.id.toplayout);
            this.reminderList = reminderList;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            reminderList.onClick(v, getAdapterPosition());
        }
    }

    //interface to get position of list
    public interface OnItemClickListener {
          void onClick(View view, int position);
    }

}
