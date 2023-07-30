package com.example.myliberty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    Button login,signup;
    TextView forgetPassword;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{8,16}" +                // at least 8 characters and maximum of 16 characters
                    "$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        signup=findViewById(R.id.signup);
        forgetPassword=findViewById(R.id.forgetPassword);
        progressBar=findViewById(R.id.progressBar);
        if (mAuth.getCurrentUser() != null ) {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair pairs[]=new Pair[4];
                pairs[0]=new Pair<View,String>(email,"emailEt");
                pairs[1]=new Pair<View,String>(findViewById(R.id.textView2),"emailTv");
                pairs[2]=new Pair<View,String>(login,"loginBt");
                pairs[3]=new Pair<View,String>(findViewById(R.id.imageView),"bg");
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
                startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class),options.toBundle());
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair pairs[]=new Pair[7];
                pairs[0]=new Pair<View,String>(email,"emailEt");
                pairs[1]=new Pair<View,String>(password,"passwordEt");
                pairs[2]=new Pair<View,String>(findViewById(R.id.textView2),"emailTv");
                pairs[3]=new Pair<View,String>(findViewById(R.id.textView3),"passwordTv");
                pairs[4]=new Pair<View,String>(signup,"signupBt");
                pairs[5]=new Pair<View,String>(login,"loginBt");
                pairs[6]=new Pair<View,String>(findViewById(R.id.imageView),"bg");
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
                startActivity(new Intent(getApplicationContext(), SignupActivity.class),options.toBundle());


            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String _email,_password;
                _email=String.valueOf(email.getText());
                _password=String.valueOf(password.getText());



                if(!isValidEmail(_email,email)){
                    Toast.makeText(getApplicationContext(),"Please enter a valid email",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if(!isPasswordValid(password)){
                    Toast.makeText(getApplicationContext(),"Please enter a valid password",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.signInWithEmailAndPassword(_email,_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){

                            Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                            Intent i =new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Login failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
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
}