package com.example.myliberty.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myliberty.Models.Message;
import com.example.myliberty.Models.Plan;
import com.example.myliberty.R;
import com.example.myliberty.Utils.dateToDaysUtility;

import java.util.ArrayList;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.MyViewHolder>{
    Context context;
    ArrayList<Message> messages;

    public messageAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public messageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.message_item,parent,false);
        return new messageAdapter.MyViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull messageAdapter.MyViewHolder holder, int position) {
        Message message=messages.get(position);
        holder.messageSubject.setText(message.getMessageSubject());
        holder.messageContent.setText(message.getMessageContent());
        holder.datePosted.setText(dateToDaysUtility.calculateDifference(message.getDatePosted(),"days").toString()+" days ago");


    }

    @Override
    public int getItemCount() {
        Log.e("Count",messages.toString());
        return messages.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView messageSubject,messageContent,datePosted;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            messageSubject=itemView.findViewById(R.id.messageSubject);
            messageContent=itemView.findViewById(R.id.messageContent);
            datePosted=itemView.findViewById(R.id.datePosted);


        }
    }
}
