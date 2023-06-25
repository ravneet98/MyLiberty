package com.example.myliberty.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myliberty.Models.Plan;
import com.example.myliberty.R;

import java.util.ArrayList;

public class planAdapter extends RecyclerView.Adapter<planAdapter.MyViewHolder> {

    Context context;
    ArrayList<Plan> planList;

    public planAdapter(Context context, ArrayList<Plan> planList) {
        this.context = context;
        this.planList = planList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(context).inflate(R.layout.plan_item,parent,false);
       return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Plan plan=planList.get(position);
        holder.planName.setText(plan.getPlanName());
        holder.planData.setText(plan.getPlanData() +"GB");
        holder.planSpeed.setText(plan.getPlanSpeed()+"Mbps");
        holder.planType.setText(plan.getPlanType());
        holder.planCost.setText("$"+ plan.getPlanCost());

    }

    @Override
    public int getItemCount() {
        Log.e("Count",planList.toString());
        return planList.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView planName,planData,planSpeed,planType,planCost;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            planName=itemView.findViewById(R.id.planName);
            planData=itemView.findViewById(R.id.planData);
            planSpeed=itemView.findViewById(R.id.planSpeed);
            planType=itemView.findViewById(R.id.planType);
            planCost=itemView.findViewById(R.id.planCost);

        }
    }

}
