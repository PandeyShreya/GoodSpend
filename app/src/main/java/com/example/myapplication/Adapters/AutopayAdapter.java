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

import com.example.myapplication.AutopayData;
import com.example.myapplication.AutopayEditActivity;
import com.example.myapplication.R;
import com.example.myapplication.ReminderEditActivity;

import java.util.ArrayList;

public class AutopayAdapter extends RecyclerView.Adapter<AutopayAdapter.ViewHolder> {
    Context context;
    ArrayList<AutopayData> autopayData;
    private OnItemClickListener autopayList;

    public AutopayAdapter(Context context, ArrayList<AutopayData> autopayData, OnItemClickListener autopayList){
        this.context = context;
        this.autopayData = autopayData;
        this.autopayList = autopayList;
    }
    @NonNull
    @Override
    public AutopayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.autopay_list_look, parent, false);
//        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_list_look, parent, false));
        return new AutopayAdapter.ViewHolder(v, autopayList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.autopayCategory.setText(autopayData.get(position).getCategory());
        holder.autopayNote.setText(autopayData.get(position).getNote());
        holder.autopayDate.setText(autopayData.get(position).getDate_limit());
        holder.autopayTime.setText(autopayData.get(position).getTime_limit());
        holder.autopayAmount.setText(valueOf((int) autopayData.get(position).getAmount()));

        //calling edit activity and sending data
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, AutopayEditActivity.class);
                intent.putExtra("note", autopayData.get(position).getNote());
                intent.putExtra("repeatTransaction", autopayData.get(position).getRepeatTransaction());
                Double amount= autopayData.get(position).getAmount();
                intent.putExtra("amount", amount.toString());
                intent.putExtra("payment", autopayData.get(position).getPaymentType());
                intent.putExtra("cateogry", autopayData.get(position).getCategory());
                intent.putExtra("dateLimit", autopayData.get(position).getDate_limit());
                intent.putExtra("timeLimit", autopayData.get(position).getTime_limit());
                intent.putExtra("id",autopayData.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return autopayData.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView autopayCategory, autopayAmount, autopayDate, autopayTime, autopayNote;
        //private LinearLayout toplayout;
        private RelativeLayout toplayout;
        OnItemClickListener autopayList;

        public ViewHolder(@NonNull View itemView, OnItemClickListener autopayList) {
            super(itemView);
            autopayCategory = itemView.findViewById(R.id.autopay_category);
            autopayAmount = itemView.findViewById(R.id.autopay_amount);
            autopayDate =  itemView.findViewById(R.id.autopay_date);
            autopayNote = itemView.findViewById(R.id.autopay_note);
            autopayTime = itemView.findViewById(R.id.autopay_time);
            toplayout = itemView.findViewById(R.id.toplayout);
            this.autopayList = autopayList;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            autopayList.onClick(v, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }
}
