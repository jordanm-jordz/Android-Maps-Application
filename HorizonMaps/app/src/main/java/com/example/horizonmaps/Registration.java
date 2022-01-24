package com.example.horizonmaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;


public class Registration extends AppCompatActivity {
    TextInputLayout regFullname, regEmail, regphone, regPassword;
    Button back, submitR;
    String name,email,phone,password;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    //ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        //Hooks
        regFullname = findViewById(R.id.r_fname);
        regEmail = findViewById(R.id.r_email);
        regphone = findViewById(R.id.r_phone);
        regPassword = findViewById(R.id.r_password);

        submitR = findViewById(R.id.rsubmit);
        back = findViewById(R.id.l_rback);


        // for authentication using FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();
       // mDialog = new ProgressDialog(this);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent= new Intent(Registration.this,Login.class);
                startActivity(intent);
            }
        });
        submitR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                UserRegister();
            }
        });
    }
    private void UserRegister(){
        if(!validateFName() | !validateEmail() | !validatePhone() | !validatePassword()){
            return;
        }
     /*   mDialog.setMessage("Creating User please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();*/

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    sendEmailVerification();
                    //mDialog.dismiss();
                    OnAuth(task.getResult().getUser());
                    mAuth.signOut();
                }else{
                    Toast.makeText(Registration.this,"error on creating user",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //Email verification code using FirebaseUser object and using isSucccessful()function.
    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Registration.this,"Check your Email for verification",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
    }

    private void OnAuth(FirebaseUser user) {
        createAnewUser(user.getUid());
    }

    private void createAnewUser(String uid) {
        User user = BuildNewuser();
        reference.child(uid).setValue(user);
    }


    private User BuildNewuser(){
        return new User(
                getUserFullName(),getUserEmail(),getUserPhone(),getUserPassword()
        );
    }

    public String getUserEmail() {
        return email;
    }
    public String getUserFullName() {
        return name;
    }
    public String getUserPhone() {
        return phone;
    }
    public String getUserPassword() {
        return password;
    }


    //x validations
    private Boolean validateFName(){
        name= regFullname.getEditText().getText().toString().trim();
        if(name.isEmpty()){
            regFullname.setError("Field cannot be empty");
            return false;
        } else{
            regFullname.setError(null);
            regFullname.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateEmail(){
        email= regEmail.getEditText().getText().toString().trim();
        String emailpattern= "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(email.isEmpty()){
            regEmail.setError("Field cannot be empty");
            return false;
        }
        else if(!email.matches(emailpattern)){
            regEmail.setError("Invalid email address");
            return false;
        }
        else
        {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePhone(){
        phone= regphone.getEditText().getText().toString().trim();
        if(phone.isEmpty()){
            regphone.setError("Field cannot be empty");
            return false;
        } else{
            regphone.setError(null);
            regphone.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword(){
        password= regPassword.getEditText().getText().toString().trim();
        String passwordV= "^(?=.*[a-zA-Z])(?=.*[@#$%^&+=])"+"(?=\\S+$).{4,}$";
        if(password.isEmpty()){
            regPassword.setError("Field cannot be empty");
            return false;
        }
        else if(!password.matches(passwordV)){
            regPassword.setError("Password weak");
            return false;
        }else{
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }
}