package com.example.myliberty;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.BoringLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.TextAttribute;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myliberty.Models.Customer;
import com.example.myliberty.Utils.dateToDaysUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import me.bastanfar.semicirclearcprogressbar.SemiCircleArcProgressBar;


public class HomeFragment extends Fragment {
    View view;
    ProgressBar progressBar;
    TextView name,remainingData,minData,maxData,daysRemaining,totalData;
    Button upgrade;
    String uid;
    Customer customer;
    SemiCircleArcProgressBar semiCircleArcProgressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public HomeFragment() {
        // Required empty public constructor

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    view=inflater.inflate(R.layout.fragment_home,container,false);

    mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();
    uid=mAuth.getUid();
    name=view.findViewById(R.id.name);
    remainingData=view.findViewById(R.id.remainingData);
    minData=view.findViewById(R.id.minData);
    maxData=view.findViewById(R.id.maxData);
    daysRemaining=view.findViewById(R.id.daysRemaining);
    totalData=view.findViewById(R.id.totalData);
    upgrade=view.findViewById(R.id.upgrade);
    semiCircleArcProgressBar=view.findViewById(R.id.dataRemainingProgressArc);
    progressBar=view.findViewById(R.id.progressBar);

        upgrade.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction fragmentTransaction = getActivity()
                    .getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            fragmentTransaction.replace(R.id.flFragment, new PlansFragment()).addToBackStack(null);
            fragmentTransaction.commit();

        }
    });
    getData(mDatabase,uid);


        return view;
    }



    public void getData(DatabaseReference mDatabase, String uid){
        progressBar.setVisibility(View.VISIBLE);
        mDatabase.child("accountInfo").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());

                }
                else {
                    customer=task.getResult().getValue(Customer.class);
                    progressBar.setVisibility(View.GONE);
                    String[] _name= customer.getName().split(" ");
                    String sourceString = _name[0]+" "+"<b>" + _name[1] + "</b> ";
                    name.setText(Html.fromHtml(sourceString));
                    remainingData.setText(customer.getDataRemaining().toString()+"GB");
                    minData.setText("0GB");
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String days= dateToDaysUtility.calculateDifference(customer.getCycleEndDate(), timestamp.toString(), "days").toString();

                    daysRemaining.setText(days+" days remaining in the billing cycle");
                    maxData.setText(customer.getMaxData().toString()+"GB");
                    totalData.setText(customer.getMaxData().toString()+"GB");
                    int percent= (int) (customer.getDataRemaining()/customer.getMaxData()*100);
                    semiCircleArcProgressBar.setPercentWithAnimation(percent);


                }
            }
        });
    }

}