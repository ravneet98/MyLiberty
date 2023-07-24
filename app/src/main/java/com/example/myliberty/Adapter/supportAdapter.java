package com.example.myliberty.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myliberty.Models.Plan;
import com.example.myliberty.Models.SupportQueries;
import com.example.myliberty.R;
import com.example.myliberty.Utils.dateToDaysUtility;

import java.util.ArrayList;

public class supportAdapter extends RecyclerView.Adapter<supportAdapter.MyViewHolder>  {
    Context context;
    ArrayList<SupportQueries> supportQueries;

    public supportAdapter(Context context, ArrayList<SupportQueries> supportQueries) {
        this.context = context;
        this.supportQueries = supportQueries;
    }

    @NonNull
    @Override
    public supportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.support_item,parent,false);
        return new supportAdapter.MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull supportAdapter.MyViewHolder holder, int position) {
        SupportQueries _supportQueries= supportQueries.get(position);
        holder.query.setText(_supportQueries.getQuery());

        if(_supportQueries.getStatus().equals("completed")) {
            holder.isSolved.setBackgroundResource(R.drawable.verified);
            holder.solution.setText(_supportQueries.getSolution());
        }else {
           // holder.isSolved.setBackgroundResource(R.drawable.remove);
            holder.solution.setText("yet to be solved");
            holder.solution.setTypeface(null, Typeface.ITALIC);
        }
        if(_supportQueries.getStatus().equals("completed")) {
            holder.datePosted.setText("Solved " + dateToDaysUtility.calculateDifference(_supportQueries.getSolutionDate().toString(), "days").toString() + " days ago");
        }else{
            holder.datePosted.setText("Created " + dateToDaysUtility.calculateDifference(_supportQueries.getCreatedDate().toString(), "days").toString() + " days ago");

        }


    }

    @Override
    public int getItemCount() {
        Log.e("Count", supportQueries.toString());
        return supportQueries.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView query,solution,datePosted;
        ImageView isSolved;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            query=itemView.findViewById(R.id.query);
            solution=itemView.findViewById(R.id.solution);
            isSolved=itemView.findViewById(R.id.isSolved);
            datePosted=itemView.findViewById(R.id.datePosted);

        }
    }
}
