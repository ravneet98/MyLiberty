package com.example.myliberty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myliberty.Models.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText accountNumber;
    private Button signup,login;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private DatabaseReference mDatabase;
    HashMap<String, String>  mobile_account = new HashMap<String, String>();



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mobile_account.put("987654321","6477734469");
        mobile_account.put("987654322","6477734468");
        mobile_account.put("987654323","6477734467");
        mobile_account.put("987654324","6477734466");
        mobile_account.put("987654325","6477734465");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        accountNumber=findViewById(R.id.accountNumber);
        signup=findViewById(R.id.signup);
        progressBar=findViewById(R.id.progressBar);
        login=findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String _email,_password,_accountNumber,_name;
                _email=String.valueOf(email.getText());
                _password=String.valueOf(password.getText());
                _accountNumber=String.valueOf(accountNumber.getText());
                _name=String.valueOf(name.getText());
                if(TextUtils.isEmpty(_accountNumber) || _accountNumber.length() != 9||!_accountNumber.matches("[0-9]+")){
                    Toast.makeText(SignupActivity.this,"Please enter valid account Number",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if(TextUtils.isEmpty(_name)||_name.length()<6){
                    Toast.makeText(SignupActivity.this,"Please enter valid name",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if(!isValidEmail(_email)){
                    Toast.makeText(SignupActivity.this,"Please enter valid email",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if(TextUtils.isEmpty(_password)||_password.length()<6){
                    Toast.makeText(SignupActivity.this,"Password length must be more than 6 ",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.createUserWithEmailAndPassword(_email,_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            String uid=mAuth.getUid();
                            String mobileNumber=mobile_account.get(_accountNumber);
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                            writeData(uid,_accountNumber,_name,_email,"12345",mobileNumber,timestamp.toString(),addDays(timestamp,30).toString(),true,2.3f,20f);
                           Toast.makeText(SignupActivity.this,"Account Created Successfully",Toast.LENGTH_SHORT).show();


                            Intent i =new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(SignupActivity.this,"Account Creation failed",Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }

        });


    }
    public void writeData(String uid, String accountNumber,String name, String email, String planId, String mobileNumber, String cycleStartDate, String cycleEndDate, Boolean billPaid, Float dataRemaining,Float maxData){
        Customer customer=new Customer(accountNumber,name,email,planId,mobileNumber,cycleStartDate,cycleEndDate,billPaid,dataRemaining,maxData);
        mDatabase.child("accountInfo").child(uid).setValue(customer);

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public static Timestamp addDays(Timestamp date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);// w ww.  j ava  2  s  .co m
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return new Timestamp(cal.getTime().getTime());

    }
}