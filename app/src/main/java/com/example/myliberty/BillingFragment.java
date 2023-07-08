package com.example.myliberty;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myliberty.Models.Customer;
import com.example.myliberty.Models.Message;
import com.example.myliberty.Models.Plan;
import com.example.myliberty.Utils.dateToDaysUtility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillingFragment#} factory method to
 * create an instance of this fragment.
 */
public class BillingFragment extends Fragment {
    TextView name,accountNumber,paymentLastDate,paymentAmount;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    String uid;
    Button makePayment;

    public BillingFragment() {
        // Required empty public constructor

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_billing, container, false);
        mAuth = FirebaseAuth.getInstance();
        uid= mAuth.getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference("accountInfo").child(uid);
        name=view.findViewById(R.id.name);
        makePayment=view.findViewById(R.id.makePayment);
        accountNumber=view.findViewById(R.id.accountNumber);
        paymentLastDate=view.findViewById(R.id.paymentLastDate);
        paymentAmount=view.findViewById(R.id.paymentAmount);
        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Clicked",Toast.LENGTH_SHORT).show();
            }
        });
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM, dd yyyy");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Customer customer=snapshot.getValue(Customer.class);
                    name.setText(customer.getName());
                    accountNumber.setText(customer.getAccountNumber());
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("MMM, dd yyyy");

                String dateString = customer.getCycleEndDate();
                if(!customer.getBillPaid()){
                    makePayment.setEnabled(true);
                }
                try {
                    Date date = inputFormat.parse(dateString);
                    String formattedDate = outputFormat.format(date);
                  paymentLastDate.setText(formattedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("plans").child(customer.getPlanId());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Plan plan=snapshot.getValue(Plan.class);
                        if(customer.getBillPaid()){
                            paymentAmount.setText("$0");

                        }else{
                            paymentAmount.setText("$"+plan.getPlanCost());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}