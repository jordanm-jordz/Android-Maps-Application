package com.example.horizonmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;
    String emailL, passwordL;
    //ProgressDialog dialog;
    TextInputLayout logEmail, logPassword;

    public String userEmail="";
    public static final String TAG="LOGIN";
    Button registerL, submitL;

    // used for retrieving data from db
    String EmailDB;
    private FirebaseDatabase database;
    private DatabaseReference u_reference;
    private static  final String Users="Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        registerL = findViewById(R.id.l_register);
        submitL = (Button) findViewById(R.id.l_submit);

        logEmail = findViewById(R.id.l_email);
        logPassword = findViewById(R.id.l_password);
        //dialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        userEmail=logEmail.toString();
        database= FirebaseDatabase.getInstance();
        u_reference = database.getReference(Users);

        u_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataS : snapshot.getChildren()) {
                    if (dataS.child("email").getValue().equals(userEmail)) {
                        EmailDB = dataS.child("email").getValue(String.class);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        registerL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
            }
        });

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mUser != null) {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Log.d(TAG, "AuthStateChanged:Logout");
                }

            }
        };

        submitL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Calling EditText is empty or no method.
                userSign();
            }
        });
    }
        @Override
        protected void onStart() {
            super.onStart();
            //removeAuthSateListner is used  in onStart function just for checking purposes,it helps in logging you out.
            mAuth.removeAuthStateListener(mAuthListner);

        }

        @Override
        protected void onStop() {
            super.onStop();
            if (mAuthListner != null) {
                mAuth.removeAuthStateListener(mAuthListner);
            }

        }

        @Override
        public void onBackPressed() {
            Login.super.finish();
        }
    private void userSign() {

        if(!validateEmail() | !validatePassword()){
            return;
        }

       /* dialog.setMessage("Loging in please wait...");
        dialog.setIndeterminate(true);
        dialog.show();*/
        mAuth.signInWithEmailAndPassword(emailL, passwordL).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                //    dialog.dismiss();

                    Toast.makeText(Login.this, "Login not successfull", Toast.LENGTH_SHORT).show();

                } else {
                    //dialog.dismiss();

                    checkIfEmailVerified();

                }
            }
        });
    }
    //This function helps in verifying whether the email is verified or not.
    private void checkIfEmailVerified(){
        FirebaseUser users=FirebaseAuth.getInstance().getCurrentUser();
        boolean emailVerified=users.isEmailVerified();
        if(!emailVerified){
            Toast.makeText(this,"Verify the Email Id",Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            finish();
        }
        else {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            // Sending Email to Dashboard Activity using intent.
            intent.putExtra("email",userEmail);

            startActivity(intent);

        }
    }
    private Boolean validateEmail(){
        emailL= logEmail.getEditText().getText().toString().trim();
        String emailpattern= "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(emailL.isEmpty()){
            logEmail.setError("Field cannot be empty");
            return false;
        }
        else if(!emailL.matches(emailpattern)){
            logEmail.setError("Invalid email address");
            return false;
        }
        else
        {
            logEmail.setError(null);
            logEmail.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword(){
        passwordL= logPassword.getEditText().getText().toString().trim();
        String passwordV= "^(?=.*[a-zA-Z])(?=.*[@#$%^&+=])"+"(?=\\S+$).{4,}$";
        if(passwordL.isEmpty()){
            logPassword.setError("Field cannot be empty");
            return false;
        }
        else if(!passwordL.matches(passwordV)){
            logPassword.setError("Password weak");
            return false;
        }else{
            logPassword.setError(null);
            logPassword.setErrorEnabled(false);
            return true;
        }
    }
}