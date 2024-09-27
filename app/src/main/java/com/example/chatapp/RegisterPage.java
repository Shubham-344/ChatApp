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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterPage extends AppCompatActivity {
    TextInputLayout username,email,password;
    Button RegisterBtn;
    Button ToLoginPage;

    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            Intent tomain = new Intent(RegisterPage.this, MainPage.class);
            startActivity(tomain);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        RegisterBtn = findViewById(R.id.RegisterBtn);
        ToLoginPage = findViewById(R.id.loginhere);

        ToLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tolog = new Intent(RegisterPage.this,LoginPage.class);
                startActivity(tolog);
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();
        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username_input = username.getEditText().getText().toString();
                String email_input = email.getEditText().getText().toString();
                String password_input = password.getEditText().getText().toString();

                if(!username_input.isEmpty()) {
                    username.setError(null);
                    username.setErrorEnabled(false);
                    if(!email_input.isEmpty()) {
                        email.setError(null);
                        email.setErrorEnabled(false);
                        if(!password_input.isEmpty()) {
                            password.setError(null);
                            password.setErrorEnabled(false);

                            //main implementation code
                            register(email_input,username_input,password_input);

                        }else{
                            password.setError("Please Enter Password");
                        }
                    }else{
                        email.setError("Please Enter Email");
                    }
                }else{
                    username.setError("Please Enter Username");
                }
            }
        });



        //auto-login
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            Intent tomain = new Intent(RegisterPage.this, MainPage.class);
            startActivity(tomain);
            finish();



        }
    }



    public void register(String email,String username,String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username",username);
                            hashMap.put("imageURL","default");
                            hashMap.put("emailId",email);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent tomain = new Intent(RegisterPage.this, MainPage.class);
                                        tomain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(tomain);
                                        finish();
                                    }
                                }
                            });
                            Toast.makeText(RegisterPage.this, "success", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RegisterPage.this, "You can't register with this email and password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}