package com.example.myliberty;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myliberty.Models.Customer;
import com.example.myliberty.Models.Plan;
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
import java.util.Date;

public class ProfileFragment extends Fragment {
    View view;
    TextView name,email,password,accountNumber,titleName,titleAccountnumber;
    Button updateprofile;
    String uid;
    Customer customer;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    public ProfileFragment() {
        // Required empty public constructor

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_profile,container,false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        uid=mAuth.getUid();
        titleName=view.findViewById(R.id.titleName);
        titleAccountnumber=view.findViewById(R.id.titleAccountnumber);
        name=view.findViewById(R.id.name);
        email=view.findViewById(R.id.email);
       // password=view.findViewById(R.id.password);
        accountNumber=view.findViewById(R.id.accountNumber);

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
                   // assert customer != null;
                    name.setText(customer.getName());
                    email.setText(customer.getEmail());
                    accountNumber.setText(customer.getAccountNumber());

                    titleName.setText(customer.getName());
                    titleAccountnumber.setText(customer.getAccountNumber());

                }
            }
        });


    }

}

