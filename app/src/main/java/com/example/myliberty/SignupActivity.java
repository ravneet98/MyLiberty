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
import com.example.myliberty.Models.Plan;
import com.example.myliberty.Models.account_number;
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
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText accountNumber;
    private Button signup,login;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private DatabaseReference mDatabase;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{8,16}" +                // at least 8 characters and maximum of 16 characters
                    "$");
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
               onBackPressed();
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
                mDatabase.child("accountNumber").child(_accountNumber).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()&&validateInput(_accountNumber,_name,_email)){
                            account_number ac=snapshot.getValue(account_number.class);
                            if(!ac.getRegistered()){
                           createUser(_email, _password, _accountNumber, _name,ac.getNumber());}
                            else {
                                Toast.makeText(getApplicationContext(),"Account already registered",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Please enter valid account details",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }

        });


    }
    public void createUser(String _email,String _password,String _accountNumber,String _name,String _number){
        mAuth.createUserWithEmailAndPassword(_email,_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    String uid=mAuth.getUid();
                    ;
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                    writeData(uid,_accountNumber,_name,_email,"31424",_number,timestamp.toString(),addDays(timestamp,30).toString(),false,20f,20f);
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
    public Boolean validateInput(String _accountNumber,String _name, String _email){
        if(TextUtils.isEmpty(_accountNumber) || _accountNumber.length() != 9||!_accountNumber.matches("[0-9]+")){
            Toast.makeText(SignupActivity.this,"Please enter a valid account Number",Toast.LENGTH_SHORT).show();
            accountNumber.setError("Account number should not be empty and must contain only digits from 0-9 and length must be 9");
            progressBar.setVisibility(View.GONE);
            return false;
        }
        if(TextUtils.isEmpty(_name)||!name.getText().toString().matches("[a-zA-Z ]+")){
            Toast.makeText(SignupActivity.this,"Please enter valid name",Toast.LENGTH_SHORT).show();
            name.setError("Name should not be empty and should contain only alphabetical characters");
            progressBar.setVisibility(View.GONE);
            return false;
        }
        if(!isValidEmail(_email,email)){
            Toast.makeText(SignupActivity.this,"Please enter a valid email",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return false;
        }
        if(!isPasswordValid(password)){
            Toast.makeText(SignupActivity.this,"Please enter a valid password",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return false;
        }
        return true;
    }
    public void writeData(String uid, String accountNumber,String name, String email,String planId,String mobileNumber,String cycleStartDate, String cycleEndDate, Boolean billPaid, Float dataRemaining,Float maxData){
        Customer customer=new Customer(accountNumber,name,email,planId,mobileNumber,cycleStartDate,cycleEndDate,billPaid,dataRemaining,maxData);
        account_number account_number=new account_number(mobileNumber,true);
        mDatabase.child("accountNumber").child(accountNumber).setValue(account_number);
        mDatabase.child("accountInfo").child(uid).setValue(customer);
    }

    public static boolean isValidEmail(CharSequence target, EditText email) {
        if(!TextUtils.isEmpty(target)){
            if(Patterns.EMAIL_ADDRESS.matcher(target).matches()){
                return true;
            }
            else{
                email.setError("Email entered is not in correct format");
                return false;
            }
        }else {
            email.setError("Field cannot be empty");
            return false;
        }
    }

    private boolean isPasswordValid(EditText password){

        if (password.getText().toString().isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        }

        // if password does not matches to the pattern
        // it will display an error message "Password is not valid it should have at least 1 special " +
        //                    "character, no white spaces, and number of characters must be in the range of 8-16"
        else if (!PASSWORD_PATTERN.matcher(password.getText().toString()).matches()) {
            password.setError("Password is not valid it should have at least 1 special " +
                    "character, no white spaces, and number of characters must be in the range of 8-16");
            return false;
        } else {
            password.setError(null);
            return true;
        }

    }
    public static Timestamp addDays(Timestamp date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);// w ww.  j ava  2  s  .co m
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return new Timestamp(cal.getTime().getTime());

    }
}