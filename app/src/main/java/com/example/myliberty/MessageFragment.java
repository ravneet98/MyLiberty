package com.example.myliberty;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myliberty.Adapter.messageAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myliberty.Adapter.planAdapter;
import com.example.myliberty.Adapter.supportAdapter;
import com.example.myliberty.Models.Message;
import com.example.myliberty.Models.Plan;
import com.example.myliberty.Models.SupportQueries;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MessageFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference mDatabase;
    messageAdapter messageAdapter;
    ArrayList<Message> messages;

    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_message, container, false);
        recyclerView=view.findViewById(R.id.messageView);

        mDatabase= FirebaseDatabase.getInstance().getReference("messages");
        mDatabase.keepSynced(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messages=new ArrayList<>();
        messageAdapter=new messageAdapter(getContext(),messages);
        recyclerView.setAdapter(messageAdapter);
        progressBar=view.findViewById(R.id.progressBar);

        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Message message=dataSnapshot.getValue(Message.class);
                    messages.add(message);


                }
                messageAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;
    }
    }
