package com.example.myliberty;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myliberty.Models.Customer;
import com.example.myliberty.Models.Plan;
import com.example.myliberty.Utils.dateToDaysUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.example.myliberty.Adapter.planAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;


public class PlansFragment extends Fragment {
        RecyclerView recyclerView;
        DatabaseReference mDatabase;
    DatabaseReference mDatabaseCustomer;
        planAdapter planAdapter;
        ArrayList<Plan> planList;
        TextView planName,planType,planCost,planSpeed,planData;
        Button cancel,upgrade;
     Animation animShow, animHide;
Customer customer;  String uid;
    private FirebaseAuth mAuth;
    AlertDialog.Builder builder;
    LinearLayout upgradeDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_plans, container, false);
        recyclerView=view.findViewById(R.id.plans_view);
        planName=view.findViewById(R.id.planName);
        planType=view.findViewById(R.id.planType);
        planCost=view.findViewById(R.id.planCost);
        planSpeed=view.findViewById(R.id.planSpeed);
        planData=view.findViewById(R.id.planData);
        cancel=view.findViewById(R.id.cancel);
        upgrade=view.findViewById(R.id.upgrade);

        upgradeDialog=view.findViewById(R.id.upgradeDialog);
        animShow = AnimationUtils.loadAnimation( getContext(), R.anim.view_show);
        animHide = AnimationUtils.loadAnimation( getContext(), R.anim.view_hide);
        mDatabase= FirebaseDatabase.getInstance().getReference("plans");
        mDatabaseCustomer=FirebaseDatabase.getInstance().getReference("accountInfo");
        builder = new AlertDialog.Builder(getContext());
        mAuth = FirebaseAuth.getInstance();
        uid=mAuth.getUid();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        planList=new ArrayList<>();
        planAdapter=new planAdapter(getContext(),planList,this);
        recyclerView.setAdapter(planAdapter);
        getCustomer(mDatabaseCustomer,uid);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Plan plan=dataSnapshot.getValue(Plan.class);


                    planList.add(plan);
                }
                planAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



     return view;
    }
    public void getCustomer(DatabaseReference mDatabase, String uid){
        mDatabase.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    customer=task.getResult().getValue(Customer.class);






                }
            }
        });
    }
    public void updatePlan(Plan plan){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Customer _customer=new Customer(customer.getAccountNumber(),customer.getName(),customer.getEmail(),plan.getPlanId(),customer.getMobileNumber(),timestamp.toString(), dateToDaysUtility.addDays(timestamp,30).toString(),false,Float.valueOf(plan.getPlanData()),Float.valueOf(plan.getPlanData()));

        if(upgradeDialog.getVisibility()==View.GONE&&recyclerView.getVisibility()==View.VISIBLE) {

            upgradeDialog.setVisibility(View.VISIBLE);
            upgradeDialog.startAnimation(animShow);
            recyclerView.startAnimation(animHide);
            recyclerView.setVisibility(View.GONE);

        }
        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
         if(customer.getBillPaid()){
                mDatabaseCustomer.child(uid).setValue(_customer).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    Toast.makeText(getContext(),"Plan has been updated successfully",
                                            Toast.LENGTH_SHORT).show();
                                    if(upgradeDialog.getVisibility()==View.VISIBLE&&recyclerView.getVisibility()==View.GONE) {
                                        upgradeDialog.startAnimation(animHide);
                                        upgradeDialog.setVisibility(View.GONE);

                                        recyclerView.setVisibility(View.VISIBLE);
                                        recyclerView.startAnimation(animShow);
                                    }


                                }else {
                                    Toast.makeText(getContext(),"Unable to update the plan! Please try again",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                }
            else {
                Toast.makeText(getContext(),"Please clear the due payments to upgrade your plan",Toast.LENGTH_SHORT).show();
         }}
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(upgradeDialog.getVisibility()==View.VISIBLE&&recyclerView.getVisibility()==View.GONE) {
                    upgradeDialog.startAnimation(animHide);
                    upgradeDialog.setVisibility(View.GONE);

                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.startAnimation(animShow);
                }
            }
        });
        planName.setText(plan.getPlanName());
        planCost.setText("• "+"$"+plan.getPlanCost()+"/mo");
        planType.setText("• "+plan.getPlanType()+" coverage all over GTA" );
        planData.setText("• "+plan.getPlanData()+"GB data ");
        planSpeed.setText("• "+plan.getPlanSpeed()+"Mbps");
    }




}