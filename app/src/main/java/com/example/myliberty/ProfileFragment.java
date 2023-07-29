package com.example.myliberty;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myliberty.Models.Customer;
import com.example.myliberty.Models.Plan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class ProfileFragment extends Fragment {

    View view;
    EditText name,password,newPassword,newPassword2;
    TextView email,accountNumber,mobileNumber;

    Button updateprofile,changePasswordBtn,backbtn,changePasswordBtn2;
    ImageButton logoutbutton;

    String _name;
    ProgressBar progressBar;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{8,16}" +                // at least 8 characters and maximum of 16 characters
                    "$");
    Customer customer;
    LinearLayout add_password_layout,linearLayout1;
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
        mDatabase.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();
        uid=mAuth.getUid();
        add_password_layout=view.findViewById(R.id.add_query_layout);
        linearLayout1 = view.findViewById(R.id.linearLayout1);
        name=view.findViewById(R.id.name);
        email=view.findViewById(R.id.email);
        mobileNumber=view.findViewById(R.id.MobileNumber);
        logoutbutton=view.findViewById(R.id.logoutbtn);
        accountNumber=view.findViewById(R.id.accountNumber);
        password=view.findViewById(R.id.currentPassword);
        updateprofile=view.findViewById(R.id.editprofile);
        changePasswordBtn=view.findViewById(R.id.changePasswordBtn);
        changePasswordBtn2=view.findViewById(R.id.changePasswordBtn2);
        newPassword=view.findViewById(R.id.newPassword1);
        newPassword2=view.findViewById(R.id.newPassword2);
        backbtn=view.findViewById(R.id.back);
        progressBar=view.findViewById(R.id.progressBar);

        getData(mDatabase,uid);

        changePasswordBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(linearLayout1.getVisibility()==View.GONE){
                    add_password_layout.setVisibility(View.GONE);
                    linearLayout1.setVisibility(View.VISIBLE);

                }
            }
        });

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(add_password_layout.getVisibility()==View.GONE){
                    linearLayout1.setVisibility(View.GONE);
                    add_password_layout.setVisibility(View.VISIBLE);

                }
            }
        });

        updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNamechanged(mDatabase, uid) ) {
                    Toast.makeText(getContext(), "Name has been updated", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(), "Update failed, Name is either same or not valid", Toast.LENGTH_LONG).show();
                }
            }
        });

        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                Toast.makeText(getContext(), "Logout Successful", Toast.LENGTH_LONG).show();
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
                    mobileNumber.setText(customer.getMobileNumber());


                }
            }
        });


    }

    private void updatePassword(){
        final String Email= mAuth.getCurrentUser().getEmail();
            if (mAuth.getCurrentUser() != null) {
                if(NegativePasswordTests()) {
                    AuthCredential credential = EmailAuthProvider.getCredential(Email, password.getText().toString());
                    mAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener((task) -> {
                        if (task.isSuccessful()) {
                            if (PositivePasswordTests()){
                                mAuth.getCurrentUser().updatePassword(newPassword.getText().toString()).addOnCompleteListener((task2) -> {
                                    if (task2.isSuccessful()) {
                                        Toast.makeText(getContext(), "Password Updated", Toast.LENGTH_LONG).show();
                                        password.setText("");
                                        newPassword.setText("");
                                    } else {
                                        Toast.makeText(getContext(), "Password update failed", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                        } else {
                            Toast.makeText(getContext(), "Current password is incorrect", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
    }

    private boolean isNamechanged(DatabaseReference mDatabase, String uid) {

        _name = customer.getName();
        if (!_name.equals(name.getText().toString())) {

            if(!TextUtils.isEmpty(name.getText().toString())){
                if(name.getText().toString().matches("[a-zA-Z ]+")){
                    mDatabase.child("accountInfo").child(uid).child("name").setValue(name.getText().toString());
                    return true;
                }else{
                    name.setError("Name should contain only alphabetical characters");
                    return false;
                }
            }else{
                Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_LONG).show();
                name.setError("Field cannot be empty");
                return false;
            }
        }else{
            return false;
        }


    }

    public boolean PositivePasswordTests(){
        if (newPassword.getText().toString().equals(newPassword2.getText().toString())) {
            if (!password.getText().toString().equals(newPassword2.getText().toString())) {
                return true;
            }else{
                Toast.makeText(getContext(), "New password and current password cannot be same", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            Toast.makeText(getContext(), "Password confirmation failed, please try again", Toast.LENGTH_LONG).show();
            return false;
        }

    }


    private boolean NegativePasswordTests(){

        if (password.getText().toString().isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        }
        if (newPassword.getText().toString().isEmpty()) {
            newPassword.setError("Field can not be empty");
            return false;
        }
        if (newPassword2.getText().toString().isEmpty()) {
            newPassword2.setError("Field can not be empty");
            return false;
        }
        if (!PASSWORD_PATTERN.matcher(password.getText().toString()).matches()) {
            password.setError("Password is not valid it should have at least 1 special " +
                    "character, no white spaces, and number of characters must be in the range of 8-16");
            return false;
        }
        if (!PASSWORD_PATTERN.matcher(newPassword.getText().toString()).matches()) {
            newPassword.setError("Password is not valid it should have at least 1 special " +
                    "character, no white spaces, and number of characters must be in the range of 8-16");
            return false;
        }
        if (!PASSWORD_PATTERN.matcher(newPassword2.getText().toString()).matches()) {
            newPassword2.setError("Password is not valid it should have at least 1 special " +
                    "character, no white spaces, and number of characters must be in the range of 8-16");
            return false;
        }
        else {
            password.setError(null);
            newPassword.setError(null);
            newPassword2.setError(null);
            return true;
        }

    }

}
