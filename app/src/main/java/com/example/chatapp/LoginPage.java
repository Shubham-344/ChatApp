package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {
    TextInputLayout email,password;
    Button loginbtn,signup;
    TextView forgotPass;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            Intent tomain = new Intent(LoginPage.this, MainPage.class);
            startActivity(tomain);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        email = findViewById(R.id.email_inp_box);
        password = findViewById(R.id.password_inp_box);
        signup = findViewById(R.id.SignUphere);
        loginbtn= findViewById(R.id.loginBtn);
        forgotPass= findViewById(R.id.forgotpass);

        auth = FirebaseAuth.getInstance();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_input = email.getEditText().getText().toString();
                String password_input = password.getEditText().getText().toString();

                if(!email_input.isEmpty()) {
                    email.setError(null);
                    email.setErrorEnabled(false);
                    if(!password_input.isEmpty()) {
                        password.setError(null);
                        password.setErrorEnabled(false);

                        //main implementation code

                        auth.signInWithEmailAndPassword(email_input,password_input)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Intent tomain = new Intent(LoginPage.this, MainPage.class);
                                            tomain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(tomain);
                                            finish();
                                        }else{
                                            Toast.makeText(LoginPage.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }else{
                        password.setError("Please Enter Password");
                    }
                }else{
                    email.setError("Please Enter Email");
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRegister = new Intent(LoginPage.this,RegisterPage.class);
                startActivity(toRegister);
                finish();
            }
        });
    }
}