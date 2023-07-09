package com.example.myliberty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myliberty.Models.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    EditText name, email, accountNumber, password;
    Button saveButton;
    String uid;
    Customer customer;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    String nameUser, emailUser, userAccountnumber, passwordUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        reference = FirebaseDatabase.getInstance().getReference("users");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        name = findViewById(R.id.editName);
        uid=mAuth.getUid();
        email = findViewById(R.id.editEmail);
        accountNumber = findViewById(R.id.editUsername);
        password = findViewById(R.id.editPassword);
        saveButton = findViewById(R.id.saveButton);

        getData(mDatabase,uid);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNameChanged() || isEmailChanged() || isPasswordChanged()) {
                    Toast.makeText(EditProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "No Changes Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isNameChanged(){
        if (!nameUser.equals(name.getText().toString())){
            mDatabase.child("accountInfo").child(uid).setValue(name.getText().toString());
            nameUser = name.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    public boolean isEmailChanged(){
        if (!emailUser.equals(email.getText().toString())){
            mDatabase.child("accountInfo").child(uid).setValue(email.getText().toString());
            emailUser = email.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    public boolean isPasswordChanged(){
        if (!passwordUser.equals(password.getText().toString())){
            mDatabase.child("accountInfo").child(uid).setValue(password.getText().toString());
            passwordUser = password.getText().toString();
            return true;
        } else{
            return false;
        }
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
                    password.setText(customer.getPassword());

                }
            }
        });


    }
}