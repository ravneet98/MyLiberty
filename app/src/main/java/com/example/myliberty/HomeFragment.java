package com.example.myliberty;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myliberty.Models.Customer;
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
    upgrade.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(),"Upgrade",Toast.LENGTH_SHORT).show();
        }
    });
    getData(mDatabase,uid);


        return view;
    }



    public void getData(DatabaseReference mDatabase, String uid){
        mDatabase.child("accountInfo").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    customer=task.getResult().getValue(Customer.class);


                    name.setText(customer.getName());
                    remainingData.setText(customer.getDataRemaining().toString()+"GB");
                    minData.setText("0GB");
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String days=calculateDifference(customer.getCycleEndDate(), timestamp.toString(), "days").toString();

                    daysRemaining.setText(days+" days remaining in the billing cycle");
                    maxData.setText(customer.getMaxData().toString()+"GB");
                    totalData.setText(customer.getMaxData().toString()+"GB");
                    int percent= (int) (customer.getDataRemaining()/customer.getMaxData()*100);
                    semiCircleArcProgressBar.setPercentWithAnimation(percent);


                }
            }
        });
    }
    private Long calculateDifference(String date1, String date2, String value) {
        Timestamp date_1 = stringToTimestamp(date1);
        Timestamp date_2 = stringToTimestamp(date2);
        long milliseconds = date_1.getTime() - date_2.getTime();
        if (value.equals("second"))
            return milliseconds / 1000;
        if (value.equals("minute"))
            return milliseconds / 1000 / 60;
        if (value.equals("hours"))
            return milliseconds / 1000 / 3600;
        if (value.equals("days"))
            return milliseconds / 1000 / 3600/24;
        else
            return new Long(999999999);
    }

    private Timestamp stringToTimestamp(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsedDate = dateFormat.parse(date);
            return new Timestamp(parsedDate.getTime());
        } catch (Exception e) {
            return null;
        }
    }
}