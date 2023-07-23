package com.example.myliberty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText email;
    Button resetPassword,login;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mAuth = FirebaseAuth.getInstance();
        email=findViewById(R.id.email);
        resetPassword=findViewById(R.id.resetPassword);
        login=findViewById(R.id.login);
        progressBar=findViewById(R.id.progressBar);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String _email;
                _email=String.valueOf(email.getText());
                if(!isValidEmail(_email)){
                    Toast.makeText(getApplicationContext(),"Please enter valid email",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
               mAuth.sendPasswordResetEmail(_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       progressBar.setVisibility(View.GONE);
                       if(task.isSuccessful()){
                           Toast.makeText(getApplicationContext(),"Reset Password Link sent to your email",Toast.LENGTH_SHORT).show();
                           email.setText("");
                       }else{
                           Toast.makeText(getApplicationContext(),"Failed! Please try again",Toast.LENGTH_SHORT).show();
                       }
                   }
               });
            }
        });
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}