package com.example.myliberty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myliberty.Models.Customer;
import com.example.myliberty.Models.Plan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileFragment extends Fragment {

    View view;
    EditText name,password;
    TextView titleName,titleAccountnumber,email,accountNumber;

    Button updateprofile;

    String _name,_password;
    Customer customer;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    String uid=mAuth.getUid();

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
        name=view.findViewById(R.id.name);
        email=view.findViewById(R.id.email);
        accountNumber=view.findViewById(R.id.accountNumber);
        password=view.findViewById(R.id.password);
      updateprofile=view.findViewById(R.id.editprofile);

        getData(mDatabase,uid);

        updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNamechanged(mDatabase, uid) ) {
                    Toast.makeText(getContext(), "Name has been updated", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(), "Data update failed, data is either same or not valid", Toast.LENGTH_LONG).show();
                }
            }
        });


        return view;
    }


    public void getData(DatabaseReference mDatabase, String uid){

        mDatabase.child("accountInfo").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    customer = task.getResult().getValue(Customer.class);
                    //assert customer != null;
                    name.setText(customer.getName());
                    email.setText(customer.getEmail());
                    accountNumber.setText(customer.getAccountNumber());


                }
            }
        });


    }

  /*  private boolean isPasswordchanged(DatabaseReference mDatabase, String uid) {


        if (!_password.equals(password.getText().toString())) {

            if(!TextUtils.isEmpty(password.getText().toString())){
                if(password.getText().toString().length()>=6){
                    mDatabase.child("accountInfo").child(uid).child("password").setValue(password.getText().toString());
                    return true;
                }else{
                    Toast.makeText(getContext(), "Length of password must be a minimum of 6", Toast.LENGTH_LONG).show();
                    return false;
                }
            }else{
                Toast.makeText(getContext(), "Password cannot be empty", Toast.LENGTH_LONG).show();
                return false;
            }
        }else{
            return false;
        }
    }*/

    private boolean isNamechanged(DatabaseReference mDatabase, String uid) {

        _name = customer.getName();
        if (!_name.equals(name.getText().toString())) {

            if(!TextUtils.isEmpty(name.getText().toString())){
                if(name.getText().toString().length()>=6){
                    mDatabase.child("accountInfo").child(uid).child("name").setValue(name.getText().toString());
                    return true;
                }else{
                    Toast.makeText(getContext(), "Length of name must be a minimum of 6", Toast.LENGTH_LONG).show();
                    return false;
                }
            }else{
                Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_LONG).show();
                return false;
            }
        }else{
            return false;
        }


    }
    public void changePassword(String oldPassword, String newPassword){
     //  mAuth.up

    }

}
